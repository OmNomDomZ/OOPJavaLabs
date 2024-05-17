package ru.nsu.rabetskii.server;

import ru.nsu.rabetskii.XmlUtility;
import ru.nsu.rabetskii.xmlmessage.Command;
import ru.nsu.rabetskii.xmlmessage.Event;
import ru.nsu.rabetskii.xmlmessage.Success;

import java.io.*;
import java.net.Socket;
import javax.xml.bind.JAXBException;

public class ClientConnection extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private XmlUtility xmlUtility;
    private String userName;

    public ClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        try {
            xmlUtility = new XmlUtility(Command.class, Event.class, Success.class, Error.class);
        } catch (JAXBException e) {
            throw new IOException("Failed to initialize JAXB", e);
        }
        start();
    }

    @Override
    public void run() {

        while (!socket.isClosed()) {
            try {
                int length = in.readInt();
                if (length <= 0) continue;
                byte[] buffer = new byte[length];
                in.readFully(buffer);
                String xmlMessage = new String(buffer);
                Command command = xmlUtility.unmarshalFromString(xmlMessage, Command.class);

                switch (command.getCommand()) {
                    case "login":
                        handleLogin(command);
                        break;
                    case "message":
                        handleMessage(command);
                        break;
                    case "logout":
                        handleLogout(command);
                        break;
                    default:
                        sendErrorMessage();
                        System.out.println("Unknown command: " + command.getCommand());
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected");
                break;
            } catch (IOException | JAXBException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void handleLogout(Command command) throws JAXBException, IOException {
        String userName = command.getUserName();
        if (userName == null) {
            System.out.println("Missing username");
        }
        broadcastMessage(new Event("userlogout", userName));
        sendSuccessMessage();
    }

    private void handleLogin(Command command) throws IOException, JAXBException {
        String userName = command.getUserName();
        String password = command.getPassword();
        if (userName == null || password == null) {
            System.out.println("Missing username or password");
            return;
        }

        String hashedPassword = Server.hashPassword(password);

        if (!Server.userPasswords.containsKey(userName)) {
            Server.userPasswords.put(userName, hashedPassword);
            Server.activeUsers.add(userName);
            this.userName = userName;
            sendSuccessMessage();
            broadcastMessage(new Event("userlogin", userName));
        } else if (Server.userPasswords.get(userName).equals(hashedPassword)) {
            Server.activeUsers.add(userName);
            this.userName = userName;
            sendErrorMessage();
            broadcastMessage(new Event("userlogin", userName));
        } else {
            sendErrorMessage();
//            this.downService();
        }
    }

    private void sendSuccessMessage() throws JAXBException, IOException {
        System.out.println("success");
        Success success = new Success();
        String xmlMessage = xmlUtility.marshalToXml(success);
        out.writeInt(xmlMessage.length());
        out.write(xmlMessage.getBytes());
        out.flush();
    }

    private void sendErrorMessage() throws JAXBException, IOException {
        System.out.println("error");
        Error error = new Error("Login failed");
        String xmlMessage = xmlUtility.marshalToXml(error);
        out.writeInt(xmlMessage.length());
        out.write(xmlMessage.getBytes());
        out.flush();
    }

    private void handleMessage(Command command) throws IOException, JAXBException {
        String message = command.getMessage();
        if (message == null) {
            System.out.println("No message content");
            return;
        }
        broadcastMessage(new Event("message", userName, message));
    }

    private void broadcastMessage(Event event) throws JAXBException, IOException {
        for (ClientConnection client : Server.serverList) {
            client.sendXmlMessage(event);
        }
    }

    private void sendXmlMessage(Event event) throws JAXBException, IOException {
        String xmlMessage = xmlUtility.marshalToXml(event);
        out.writeInt(xmlMessage.length());
        out.write(xmlMessage.getBytes());
        out.flush();
    }

//    private void downService() {
//        try {
//            if (!socket.isClosed()) {
////                socket.close();
////                in.close();
////                out.close();
//            }
////        } catch (IOException ignored) {}
//    }
}