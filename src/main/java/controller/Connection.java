package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ObjectType;
import model.ReceivePacket;
import model.SendPacket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.JwtGenerator;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private final JwtGenerator jwtGenerator;

    public Connection() {
        jwtGenerator = new JwtGenerator();
        try {
            Socket socket = new Socket("localhost", 8080);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            Thread receive = new Thread(() -> {
                while(true) {handelReceived(receivePacket());}
            });
            receive.join();
            receive.start();
        }
        catch (IOException | InterruptedException ignored) {}
    }

    public void startNewConnection() {
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("Server");
        SendPacket sendPacket = new SendPacket(Controller.currentUser.getUserName(), usernames, ObjectType.String,Controller.currentUser.getUserName() + " connect to server");
        sendPacket(sendPacket);
    }

    public void sendPacket(SendPacket sendPacket) {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JSONParser parser = new JSONParser();
            String jsonParser = gson.toJson(sendPacket);
            JSONObject jsonMakeObject = (JSONObject) parser.parse(jsonParser);
            dataOutputStream.writeUTF(jwtGenerator.generateJwt(jsonMakeObject.toString()));
        }
        catch (IOException | ParseException ignored) {}
    }

    public ReceivePacket receivePacket() {
        ReceivePacket receivePacket = null;
        try {
            String input = dataInputStream.readUTF();
            input = jwtGenerator.decodeJWT(input);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject =(JSONObject) parser.parse(input);
            receivePacket = gson.fromJson(jsonObject.toString(), ReceivePacket.class);
        }
        catch (IOException | ParseException ignored) {}
        return receivePacket;
    }

    public void handelReceived(ReceivePacket receivePacket) {
        switch (receivePacket.getObjectType()) {
            case Kingdom:break;
            case Chat: Controller.currentUser.setMyChats(receivePacket.getChat()); break;
            case String:
        }
    }
}
