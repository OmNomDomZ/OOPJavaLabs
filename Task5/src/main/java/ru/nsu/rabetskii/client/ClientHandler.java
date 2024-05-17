package ru.nsu.rabetskii.client;

import ru.nsu.rabetskii.xmlmessage.Command;
import ru.nsu.rabetskii.XmlUtility;
import ru.nsu.rabetskii.xmlmessage.Event;
import ru.nsu.rabetskii.xmlmessage.Success;
import ru.nsu.rabetskii.xmlmessage.Error;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

class ClientHandler {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private BufferedReader inputUser;
    private String nickname;
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;
    private XmlUtility xmlUtility;

    public ClientHandler(String addr, int port) throws IOException, JAXBException {
        this.socket = new Socket(addr, port);
        this.xmlUtility = new XmlUtility(Command.class, Event.class, Success.class, Error.class);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        inputUser = new BufferedReader(new InputStreamReader(System.in));
        dt1 = new SimpleDateFormat("HH:mm:ss");

        promptNickname();
        new ReadMsg().start();
        new WriteMsg().start();
    }

    private void promptNickname() {
        try {
            System.out.print("Enter your nickname: ");
            nickname = inputUser.readLine();
            System.out.print("Enter your password: ");
            String password = inputUser.readLine();
            Command loginCommand = new Command("login", nickname, password);
            sendXmlMessage(loginCommand);

            // Ожидание ответа от сервера
            int length = in.readInt();
            if (length <= 0) {
                System.out.println("Error");
                return;
            }
            byte[] buffer = new byte[length];
            in.readFully(buffer);
            String xmlMessage = new String(buffer);

            if (xmlMessage.contains("success")) {
                System.out.println("Login successful!");
            } else if (xmlMessage.contains("error")) {
                Error error = xmlUtility.unmarshalFromString(xmlMessage, Error.class);
                System.out.println("Login failed: " + error.getMessage());
            }
        } catch (IOException | JAXBException ignored) {}
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                in.close();
                out.close();
//                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendXmlMessage(Command command) throws JAXBException, IOException {
        String xmlMessage = xmlUtility.marshalToXml(command);
        out.writeInt(xmlMessage.length());
        out.write(xmlMessage.getBytes());
        out.flush();
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {
            try {
                while (!socket.isClosed()) {
                    int length;
                    try {
                        length = in.readInt();
                    } catch (EOFException e) {
                        System.out.println("Server disconnected");
                        break;
                    }
                    if (length <= 0) continue;
                    byte[] buffer = new byte[length];
                    in.readFully(buffer);
                    String xmlMessage = new String(buffer);

                    if (xmlMessage.contains("event")) {
                        Event event = xmlUtility.unmarshalFromString(xmlMessage, Event.class);
                        handleEvent(event);
                    } else if (xmlMessage.contains("success")) {
                        Success success = xmlUtility.unmarshalFromString(xmlMessage, Success.class);
                        System.out.println("Success: " + success);
                    } else if (xmlMessage.contains("error")) {
                        Error error = xmlUtility.unmarshalFromString(xmlMessage, Error.class);
                        System.out.println("Error: " + error.getMessage());
                    }
                }
            } catch (IOException | JAXBException e) {
                ClientHandler.this.downService();
            }
        }

        private void handleEvent(Event event) {
            if ("userlogin".equals(event.getEvent())) {
                System.out.println(event.getUserName() + " joined the chat");
            } else if ("userlogout".equals(event.getEvent())) {
                System.out.println(event.getUserName() + " left the chat");
            } else if ("message".equals(event.getEvent())) {
                System.out.println(event.getFrom() + ": " + event.getMessage());
            }
        }
    }

    private class WriteMsg extends Thread {
        @Override
        public void run() {
            while (!socket.isClosed()) {
                try {
                    String userWord = inputUser.readLine();
                    if (Objects.equals(userWord, "logout")) {
                        Command messageCommand = new Command("logout", nickname);
                        sendXmlMessage(messageCommand);

                        // Ожидание ответа от сервера
                        int length = in.readInt();
                        if (length <= 0) {
                            System.out.println("Error");
                            return;
                        }
                        byte[] buffer = new byte[length];
                        in.readFully(buffer);
                        String xmlMessage = new String(buffer);

                        if (xmlMessage.contains("success")) {
                            System.out.println("Logout successful!");
                            downService();
                            break;
                        } else if (xmlMessage.contains("error")) {
                            Error error = xmlUtility.unmarshalFromString(xmlMessage, Error.class);
                            System.out.println("Logout failed: " + error.getMessage());
                        }
                    } else {
                        time = new Date();
                        dtime = dt1.format(time);
                        Command messageCommand = new Command("message", nickname, userWord);
                        sendXmlMessage(messageCommand);
                    }
                } catch (IOException | JAXBException e) {
                    ClientHandler.this.downService();
                }
            }
        }
    }
}