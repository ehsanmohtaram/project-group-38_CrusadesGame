package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ObjectType;
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
        System.out.println(sendPacket.getString());
    }

    public SendPacket receivePacket() {
        SendPacket sendPacket = null;
        try {
            String input = dataInputStream.readUTF();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(input);
            sendPacket = gson.fromJson(jsonObject.toString(), SendPacket.class);
        }

        catch (IOException | ParseException ignored) {}
        return sendPacket;
    }

    public void handleSentPackets(SendPacket sendPacket) {
        try {
            if (!sendPacket.getUserReceiver().get(0).equals("Server"))
                for (String usernames : sendPacket.getUserReceiver())
                    if (checkSocketByUsername(usernames) != null) {
                        DataOutputStream sendData = new DataOutputStream(checkSocketByUsername(usernames).getSocket().getOutputStream());
                        Thread thread = new Thread(() -> {
                            try {sendData.writeUTF(sendReceivePacket(sendPacket));}
                            catch (IOException e) {throw new RuntimeException(e);}
                        });
                        try {thread.join();}
                        catch (InterruptedException e) {throw new RuntimeException(e);}
                        thread.start();
                    }
                    else Server.waiting.add(sendPacket);
        }
        catch (IOException ignored) {}
    }

    public String sendReceivePacket(SendPacket sendPacket) {
        ReceivePacket  receivePacket= null;
        if (sendPacket.getChat() != null) receivePacket = new ReceivePacket(sendPacket.getUserSender(), sendPacket.getObjectType(), sendPacket.getChat());
        else if (sendPacket.getString() != null)  receivePacket = new ReceivePacket(sendPacket.getUserSender(), sendPacket.getObjectType(), sendPacket.getString());
        else if (sendPacket.getKingdom() != null)  receivePacket = new ReceivePacket(sendPacket.getUserSender(), sendPacket.getObjectType(), sendPacket.getKingdom());
        JSONObject jsonMakeObject = null;
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JSONParser parser = new JSONParser();
            String jsonParser = gson.toJson(receivePacket);
            jsonMakeObject = (JSONObject) parser.parse(jsonParser);
        }
        catch (ParseException ignored) {}
        return jsonMakeObject.toString();
    }

    public UserConnection checkSocketByUsername (String username) {
       for (UserConnection userConnection : Server.sockets)
           if (userConnection.getUser().equals(User.getUserByUsername(username))) return userConnection;
       return null;
    }

    public void heatBeatSender() {
        while(true) {
            ReceivePacket receivePacket = new ReceivePacket("Server", ObjectType.String, "0");
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

    public User getUser() {
        return user;
    }

    public Socket getSocket() {
        return socket;
    }
}
