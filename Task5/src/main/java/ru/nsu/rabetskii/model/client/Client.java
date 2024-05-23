package ru.nsu.rabetskii.model.client;

import ru.nsu.rabetskii.model.ChatModel;
import ru.nsu.rabetskii.view.LoginView;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Client {

    public static String ipAddr = "localhost";
    public static int port = 8080;

    public static void main(String[] args) throws JAXBException, IOException {
        ChatModel chatModel = new ChatModel();
        new LoginView(chatModel);
    }
}
