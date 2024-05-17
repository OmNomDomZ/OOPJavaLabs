package ru.nsu.rabetskii.xmlmessage;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
public class Event {
    @XmlAttribute(name = "name")
    private String name;
    @XmlElement(name = "from")
    private String from;
    @XmlElement(name = "message")
    private String message;
    @XmlElement(name = "name")
    private String userName;

    public Event() {}

    public Event(String name, String from, String message) {
        this.name = name;
        this.from = from;
        this.message = message;
    }

    public Event(String name, String userName) {
        this.name = name;
        this.userName = userName;
    }

    public String getName() {
        return name;
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
