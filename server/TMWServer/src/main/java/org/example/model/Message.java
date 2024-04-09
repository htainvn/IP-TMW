package org.example.model;

import lombok.Getter;
import org.example.util.Constants;

@Getter
public class Message {
    private final String messageHeader;
    private final String messageBody;

    public Message(String messageHeader) {
        this.messageHeader = messageHeader;
        this.messageBody = "";
    }

    public Message(String messageHeader, String messageBody) {
        this.messageHeader = messageHeader;
        this.messageBody = messageBody;
    }

    public String toString() {
        return messageHeader + Constants.DELIMITER + messageBody;
    }
}

