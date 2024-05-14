package ru.nsu.rabetskii;

import javax.xml.bind.JAXBException;

public class Main {
    public static void main(String[] args) {
        try {
            XmlUtility utility = new XmlUtility(ClientObject.class);

            // Пример анмаршалинга
            ClientObject clientObject = utility.unmarshalFromFile("src\\main\\resources\\client\\login.xml");
            System.out.println("Command from XML: \n" + clientObject.getCommand());

            // Пример маршалинга
            ClientObject newClientObject = new ClientObject("logout", "user2", "4321");
            String xmlOutput = utility.marshalToXml(newClientObject);
            System.out.println("\nXML from Object:\n" + xmlOutput);

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}