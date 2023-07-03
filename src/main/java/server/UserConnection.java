package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ReceivePacket;
import model.SendPacket;
import model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;

public class UserConnection {
    private User user;
    private Thread receive;
    private Thread heartBeat;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public UserConnection(Socket socket) {
        try {
            this.socket = socket;
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            getUserInfo();
            receive = new Thread(() -> {
                while(true) {handleSentPackets(receivePacket());}
            });
            //Thread send = new Thread(() -> sendPacket());
            heartBeat = new Thread(this::heatBeatSender);
            receive.join();
            heartBeat.join();
            receive.start();
            heartBeat.start();
        }
        catch (IOException ignored) {} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void getUserInfo() throws IOException{
        SendPacket sendPacket = receivePacket();
        user = User.getUserByUsername(sendPacket.getUserSender());
        System.out.println(sendPacket.getObject());
    }

    public SendPacket receivePacket() {
        SendPacket sendPacket = null;
        try {
            String input = dataInputStream.readUTF();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject =(JSONObject) parser.parse(input);
            sendPacket = gson.fromJson(jsonObject.toString(), SendPacket.class);
        }
        catch (IOException | ParseException ignored) {}
        return sendPacket;
    }

    public void handleSentPackets(SendPacket sendPacket) {

    }
    public void sendPacket() {
        ReceivePacket receivePacket = null;
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JSONParser parser = new JSONParser();
            String jsonParser = gson.toJson(receivePacket);
            JSONObject jsonMakeObject = (JSONObject) parser.parse(jsonParser);
            dataOutputStream.writeUTF(jsonMakeObject.toString());
        }
        catch (IOException | ParseException ignored) {}
    }

    public void heatBeatSender() {
        while(true) {
            ReceivePacket receivePacket = new ReceivePacket("Server", "0");
            try {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                JSONParser parser = new JSONParser();
                String jsonParser = gson.toJson(receivePacket);
                JSONObject jsonMakeObject = (JSONObject) parser.parse(jsonParser);
                dataOutputStream.writeUTF(jsonMakeObject.toString());
            } catch (IOException | ParseException exception) {
                if (exception instanceof IOException) {
                    System.out.println(user.getUserName() + " disconnected");
                    receive.interrupt();
                    heartBeat.interrupt();
                    Server.sockets.remove(this);
                    break;
                }
            }
            try {Thread.sleep(1000);} catch (InterruptedException ignored) {}
        }
    }

}
