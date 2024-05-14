package ru.nsu.rabetskii;

import java.io.File;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

//public class XMLParser {
//    public XMLParser(String fileName){
//        try {
//            File file = new File(fileName);
//            JAXBContext jaxbContext = JAXBContext.newInstance(Client.class);
//
//            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//            Client client = (Client) unmarshaller.unmarshal(file);
//
//            System.out.println(client.getCommand());
//
//        } catch (JAXBException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}

public class XmlUtility {

    private final JAXBContext jaxbContext;

    public XmlUtility(Class<?> clazz) throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(clazz);
    }

    public ClientObject unmarshalFromFile(String filePath) throws JAXBException {
        File file = new File(filePath);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (ClientObject) unmarshaller.unmarshal(file);
    }

    public String marshalToXml(ClientObject clientObject) throws JAXBException {
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(clientObject, writer);
        return writer.toString();
    }


}
