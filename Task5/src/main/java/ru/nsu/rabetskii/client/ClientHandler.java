package ru.nsu.rabetskii.client;

import ru.nsu.rabetskii.xmlmessage.Command;
import ru.nsu.rabetskii.XmlUtility;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        this.xmlUtility = new XmlUtility(Command.class);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        inputUser = new BufferedReader(new InputStreamReader(System.in));
        dt1 = new SimpleDateFormat("HH:mm:ss");

        promptNickname();
        new ReadMsg().start();
        new WriteMsg().start();
    }

    private void promptNickname() {
        System.out.print("Enter your nickname: ");
        try {
            nickname = inputUser.readLine();
            Command loginCommand = new Command("login", nickname);

            System.out.println();
            sendXmlMessage(loginCommand);
        } catch (IOException | JAXBException ignored) {}
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
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
                while (true) {
                    int length = in.readInt();
                    if (length <= 0) continue;
                    byte[] buffer = new byte[length];
                    in.readFully(buffer);
                    String xmlMessage = new String(buffer);
                    Command command = xmlUtility.unmarshalFromString(xmlMessage);

                    if ("message".equals(command.getCommand())) {
                        System.out.println(command.getUserName() + ": " + command.getMessage());
                    } else if ("login".equals(command.getCommand())) {
                        System.out.println(command.getUserName() + " joined the chat");
                    }
                }
            } catch (IOException | JAXBException e) {
                ClientHandler.this.downService();
            }
        }
    }

    private class WriteMsg extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    time = new Date();
                    dtime = dt1.format(time);
                    String userWord = inputUser.readLine();
                    Command messageCommand = new Command("message", nickname, userWord);
                    sendXmlMessage(messageCommand);
                } catch (IOException | JAXBException e) {
                    ClientHandler.this.downService();
                }
            }
        }
    }
}