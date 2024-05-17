package ru.nsu.rabetskii.xmlmessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class Error {
    @XmlElement(name = "message")
    private String message;

    public Error() {}

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
