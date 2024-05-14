package ru.nsu.rabetskii.server;

import java.io.*;
import java.net.Socket;

class ClientConnection extends Thread {

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    public ClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Server.story.printStory(out);
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            word = in.readLine();
            try {
                out.write(word + "\n");
                out.flush();
            } catch (IOException ignored) {}
            try {
                while (true) {
                    word = in.readLine();
                    if (word.equals("stop")) {
                        this.downService();
                        break;
                    }
                    System.out.println("Echoing: " + word);
                    Server.story.addStoryEl(word);
                    for (ClientConnection clientConnection : Server.serverList) {
                        clientConnection.send(word);
                    }
                }
            } catch (NullPointerException ignored) {}
        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ClientConnection clientConnection : Server.serverList) {
                    if (clientConnection.equals(this)) clientConnection.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}