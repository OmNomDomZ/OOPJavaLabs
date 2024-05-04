package ru.nsu.rabetskii;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "command")
public class Client {
    @XmlAttribute(name = "name")
    private String command;
    @XmlElement(name = "userName")
    private String userName;
    @XmlElement(name = "password")
    private String password;
    private String message;

    public Client(){};
    public Client(String command, String userName, String password) {
        this.command = command;
        this.userName = userName;
        this.password = password;
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
