package ru.nsu.rabetskii.model.client;

import ru.nsu.rabetskii.model.ChatModel;
import ru.nsu.rabetskii.model.xmlmessage.Command;
import ru.nsu.rabetskii.model.XmlUtility;
import ru.nsu.rabetskii.model.xmlmessage.Event;
import ru.nsu.rabetskii.model.xmlmessage.Success;
import ru.nsu.rabetskii.model.xmlmessage.Error;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ClientHandler {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickname;
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;
    private XmlUtility xmlUtility;
    private ChatModel chatModel;

    public ClientHandler(String addr, int port, ChatModel chatModel, String nickname, String password) throws IOException, JAXBException {
        this.socket = new Socket(addr, port);
        this.chatModel = chatModel;
        this.nickname = nickname;
        this.xmlUtility = new XmlUtility(Command.class, Event.class, Success.class, Error.class);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        dt1 = new SimpleDateFormat("HH:mm:ss");

        promptLogin(nickname, password);
        new ReadMsg().start();
    }

    private void promptLogin(String nickname, String password) {
        try {
            Command loginCommand = new Command("login", nickname, password);
            sendXmlMessage(loginCommand);

            int length = in.readInt();
            if (length <= 0) {
                chatModel.receiveMessage("Error: invalid response from server");
                return;
            }
            byte[] buffer = new byte[length];
            in.readFully(buffer);
            String xmlMessage = new String(buffer);

            if (xmlMessage.contains("success")) {
                chatModel.receiveMessage("Login successful!");
            } else if (xmlMessage.contains("error")) {
                Error error = xmlUtility.unmarshalFromString(xmlMessage, Error.class);
                chatModel.receiveMessage("Login failed: " + error.getMessage());
            }
        } catch (IOException | JAXBException e) {
            chatModel.receiveMessage("Login failed: " + e.getMessage());
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                in.close();
                out.close();
                socket.close();
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

    public void sendMessage(String message) {
        try {
            Command messageCommand = new Command("message", nickname, message);
            sendXmlMessage(messageCommand);
        } catch (IOException | JAXBException e) {
            chatModel.receiveMessage("Failed to send message: " + e.getMessage());
        }
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
                        chatModel.receiveMessage("Server disconnected");
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
                        chatModel.receiveMessage("Success: " + success);
                    } else if (xmlMessage.contains("error")) {
                        Error error = xmlUtility.unmarshalFromString(xmlMessage, Error.class);
                        chatModel.receiveMessage("Error: " + error.getMessage());
                    }
                }
            } catch (IOException | JAXBException e) {
                ClientHandler.this.downService();
            }
        }

        private void handleEvent(Event event) {
            if ("userlogin".equals(event.getEvent())) {
                chatModel.receiveMessage(event.getUserName() + " joined the chat");
            } else if ("userlogout".equals(event.getEvent())) {
                chatModel.receiveMessage(event.getUserName() + " left the chat");
            } else if ("message".equals(event.getEvent())) {
                chatModel.receiveMessage(event.getFrom() + ": " + event.getMessage());
            }
        }
    }
}
