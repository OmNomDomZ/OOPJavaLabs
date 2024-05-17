package ru.nsu.rabetskii.xmlmessage;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

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

    public Command(String command, String userName){
        this.command = command;
        this.userName = userName;
    }

    public Command(String command, String userName, String action) {
        this.command = command;
        this.userName = userName;
        if (Objects.equals(command, "login")){
            this.password = action;
        } else if (Objects.equals(command, "message")){
            this.message = action;
        }

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
