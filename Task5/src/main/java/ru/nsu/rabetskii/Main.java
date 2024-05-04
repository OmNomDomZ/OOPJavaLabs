package ru.nsu.rabetskii;

import javax.xml.bind.JAXBException;

public class Main {
    public static void main(String[] args) {
        try {
            XmlUtility utility = new XmlUtility(Client.class);

            // Пример анмаршалинга
            Client client = utility.unmarshalFromFile("src\\main\\resources\\client\\login.xml");
            System.out.println("Command from XML: \n" + client.getCommand());

            // Пример маршалинга
            Client newClient = new Client("logout", "user2", "4321");
            String xmlOutput = utility.marshalToXml(newClient);
            System.out.println("\nXML from Object:\n" + xmlOutput);

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}