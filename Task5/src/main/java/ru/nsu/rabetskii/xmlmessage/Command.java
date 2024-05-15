package ru.nsu.rabetskii.xmlmessage;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "command")
public class Command {
    @XmlAttribute(name = "name")
    private String command;
    @XmlElement(name = "userName")
    private String userName;
    @XmlElement(name = "password")
    private String password;
    @XmlElement(name = "message")
    private String message;

    public Command() {}

    public Command(String command, String userName) {
        this.command = command;
        this.userName = userName;
    }

    public Command(String command, String userName, String message) {
        this.command = command;
        this.userName = userName;
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }
}
