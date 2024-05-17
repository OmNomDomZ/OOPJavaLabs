package ru.nsu.rabetskii.xmlmessage;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
public class Event {
    @XmlAttribute(name = "event")
    private String event;
    @XmlElement(name = "from")
    private String from;
    @XmlElement(name = "message")
    private String message;
    @XmlElement(name = "name")
    private String userName;

    public Event() {}

    public Event(String event, String from, String message) {
        this.event = event;
        this.from = from;
        this.message = message;
    }

    public Event(String event, String userName) {
        this.event = event;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }
}
