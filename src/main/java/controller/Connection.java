package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ReceivePacket;
import model.SendPacket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public Connection() {
        try {
            socket = new Socket("localhost", 8080);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            Thread receive = new Thread(() -> {
                while(true) {handelReceived(receivePacket());}
            });
//            Thread send = new Thread(() -> {
//                while(true) {handleSentPackets(receivePacket());}
//            });
            receive.join();
            receive.start();
        }
        catch (IOException | InterruptedException ignored) {}
    }

    public void startNewConnection() {
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("Server");
        SendPacket sendPacket = new SendPacket(Controller.currentUser.getUserName(), usernames, Controller.currentUser.getUserName() + " connect to server");
        sendPacket(sendPacket);
    }

    public void sendPacket(SendPacket sendPacket) {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JSONParser parser = new JSONParser();
            String jsonParser = gson.toJson(sendPacket);
            JSONObject jsonMakeObject = (JSONObject) parser.parse(jsonParser);
            dataOutputStream.writeUTF(jsonMakeObject.toString());
        }
        catch (IOException | ParseException ignored) {}
    }

    public ReceivePacket receivePacket() {
        ReceivePacket receivePacket = null;
        try {
            String input = dataInputStream.readUTF();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject =(JSONObject) parser.parse(input);
            receivePacket = gson.fromJson(jsonObject.toString(), ReceivePacket.class);
        }
        catch (IOException | ParseException ignored) {}
        return receivePacket;
    }

    public void handelReceived(ReceivePacket receivePacket) {

    }
}
