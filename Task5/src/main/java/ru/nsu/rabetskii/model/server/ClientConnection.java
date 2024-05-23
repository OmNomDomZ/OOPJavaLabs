package ru.nsu.rabetskii.model.server;

import ru.nsu.rabetskii.model.XmlUtility;
import ru.nsu.rabetskii.model.xmlmessage.Command;
import ru.nsu.rabetskii.model.xmlmessage.Event;
import ru.nsu.rabetskii.model.xmlmessage.Success;
import ru.nsu.rabetskii.model.xmlmessage.Error;

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
        try {
            while (!socket.isClosed()) {
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
                        sendErrorMessage("Unknown command: " + command.getCommand());
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected");
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLogin(Command command) throws IOException, JAXBException {
        String userName = command.getUserName();
        String password = command.getPassword();
        if (userName == null || password == null) {
            sendErrorMessage("Missing username or password");
            return;
        }

        String hashedPassword = Server.hashPassword(password);

        if (!Server.userPasswords.containsKey(userName)) {
            Server.userPasswords.put(userName, hashedPassword);
            Server.activeUsers.add(userName);
            this.userName = userName;
            sendSuccessMessage();
            Server.broadcastMessage(new Event("userlogin", userName));
        } else if (Server.userPasswords.get(userName).equals(hashedPassword)) {
            Server.activeUsers.add(userName);
            this.userName = userName;
            sendSuccessMessage();
            Server.broadcastMessage(new Event("userlogin", userName));
        } else {
            sendErrorMessage("Incorrect password");
        }
    }

    private void handleMessage(Command command) throws IOException, JAXBException {
        String message = command.getMessage();
        if (message == null) {
            sendErrorMessage("No message content");
            return;
        }
        Server.broadcastMessage(new Event("message", userName, message));
    }

    private void handleLogout(Command command) throws IOException, JAXBException {
        String userName = command.getUserName();
        if (userName == null) {
            sendErrorMessage("Missing username");
            return;
        }
        Server.activeUsers.remove(userName);
        Server.broadcastMessage(new Event("userlogout", userName));
        sendSuccessMessage();
        socket.close();
    }

    private void sendSuccessMessage() throws JAXBException, IOException {
        Success success = new Success();
        String xmlMessage = xmlUtility.marshalToXml(success);
        sendMessage(xmlMessage);
    }

    private void sendErrorMessage(String errorMessage) throws JAXBException, IOException {
        Error error = new Error(errorMessage);
        String xmlMessage = xmlUtility.marshalToXml(error);
        sendMessage(xmlMessage);
    }

    public void sendMessage(Event event) throws JAXBException, IOException {
        String xmlMessage = xmlUtility.marshalToXml(event);
        sendMessage(xmlMessage);
    }

    private void sendMessage(String xmlMessage) throws IOException {
        out.writeInt(xmlMessage.length());
        out.write(xmlMessage.getBytes());
        out.flush();
    }
}
