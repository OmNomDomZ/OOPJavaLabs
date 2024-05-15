package ru.nsu.rabetskii.server;

import ru.nsu.rabetskii.XmlUtility;
import ru.nsu.rabetskii.xmlmessage.Command;

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
            xmlUtility = new XmlUtility(Command.class);
        } catch (JAXBException e) {
            throw new IOException("Failed to initialize JAXB", e);
        }
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                int length = in.readInt();
                if (length <= 0) continue;
                byte[] buffer = new byte[length];
                in.readFully(buffer);
                String xmlMessage = new String(buffer);
                Command command = xmlUtility.unmarshalFromString(xmlMessage);

                switch (command.getCommand()) {
                    case "login":
                        handleLogin(command);
                        break;
                    case "message":
                        handleMessage(command);
                        break;
                    default:
                        System.out.println("Unknown command: " + command.getCommand());
                }
            }
        } catch (IOException | JAXBException e) {
            this.downService();
        }
    }

    private void handleLogin(Command command) throws IOException, JAXBException {
        String userName = command.getUserName();
        if (userName == null) {
            System.out.println("Missing username or password");
            return;
        }

        String password = userName;

        String hashedPassword = Server.hashPassword(password);

        if (!Server.userPasswords.containsKey(userName)) {
            Server.userPasswords.put(userName, hashedPassword);
            Server.activeUsers.add(userName);
            this.userName = userName;
            broadcastMessage(new Command("login", userName, null));
        } else if (Server.userPasswords.get(userName).equals(hashedPassword)) {
            Server.activeUsers.add(userName);
            this.userName = userName;
            broadcastMessage(new Command("login", userName, null));
        } else {
            System.out.println("Invalid username or password");
            this.downService();
        }
    }

    private void handleMessage(Command command) throws IOException, JAXBException {
        String message = command.getMessage();
        if (message == null) {
            System.out.println("No message content");
            return;
        }
        broadcastMessage(new Command("message", userName, message));
    }

    private void broadcastMessage(Command command) throws JAXBException, IOException {
        for (ClientConnection client : Server.serverList) {
            client.sendXmlMessage(command);
        }
    }

    private void sendXmlMessage(Command command) throws JAXBException, IOException {
        String xmlMessage = xmlUtility.marshalToXml(command);
        out.writeInt(xmlMessage.length());
        out.write(xmlMessage.getBytes());
        out.flush();
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                Server.serverList.remove(this);
            }
        } catch (IOException ignored) {}
    }
}