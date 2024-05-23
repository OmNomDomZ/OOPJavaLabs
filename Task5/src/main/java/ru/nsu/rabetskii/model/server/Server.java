package ru.nsu.rabetskii.model.server;

import ru.nsu.rabetskii.model.xmlmessage.Event;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ClientConnection> serverList = new LinkedList<>();
    public static Map<String, String> userPasswords = new HashMap<>();
    public static Set<String> activeUsers = new HashSet<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server Started");
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    ClientConnection clientConnection = new ClientConnection(socket);
                    serverList.add(clientConnection);
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void broadcastMessage(Event event) throws IOException, JAXBException {
        for (ClientConnection client : serverList) {
            client.sendMessage(event);
        }
    }
}
