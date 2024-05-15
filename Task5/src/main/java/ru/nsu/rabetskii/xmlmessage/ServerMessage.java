package ru.nsu.rabetskii.xmlmessage;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
public class ServerMessage {
    @XmlAttribute(name = "name")
    private String event;
    @XmlElement(name = "from")
    private String from;
    @XmlElement(name = "message")
    private String message;

    public ServerMessage() {}

    public ServerMessage(String event, String from, String message) {
        this.event = event;
        this.from = from;
        this.message = message;
    }

    public String getEvent() {
        return event;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }
}
