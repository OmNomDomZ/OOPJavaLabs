package ru.nsu.rabetskii.client;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Client {

    public static String ipAddr = "localhost";
    public static int port = 8080;

    public static void main(String[] args) throws JAXBException, IOException {
        new ClientHandler(ipAddr, port);
    }
}
