package ru.nsu.rabetskii.server;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ClientConnection> serverList = new LinkedList<>();
    public static Story story;

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            story = new Story();
            System.out.println("Server Started");
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ClientConnection(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }
}