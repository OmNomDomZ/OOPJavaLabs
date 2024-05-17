package ru.nsu.rabetskii;

import ru.nsu.rabetskii.xmlmessage.Command;
import ru.nsu.rabetskii.xmlmessage.Event;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlUtility {

    private final JAXBContext jaxbContext;

    public XmlUtility(Class<?>... classes) throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(classes);
    }

    public <T> T unmarshalFromFile(String filePath, Class<T> clazz) throws JAXBException {
        File file = new File(filePath);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return clazz.cast(unmarshaller.unmarshal(file));
    }

    public <T> T unmarshalFromString(String xml, Class<T> clazz) throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return clazz.cast(unmarshaller.unmarshal(new StringReader(xml)));
    }

    public String marshalToXml(Object obj) throws JAXBException {
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }
}
