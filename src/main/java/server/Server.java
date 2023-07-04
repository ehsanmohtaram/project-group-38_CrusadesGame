package server;


import model.Database;
import model.SendPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static ArrayList<UserConnection> sockets = new ArrayList<>();
    public static ArrayList<SendPacket> waiting = new ArrayList<>();
    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("new Connection");
                UserConnection userConnection = new UserConnection(socket);
                sockets.add(userConnection);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Database.setArrayOfUsers();
        new Server();
    }
}
