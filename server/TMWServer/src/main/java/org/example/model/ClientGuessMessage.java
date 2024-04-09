package org.example.model;

import lombok.Getter;
import org.example.util.Constants;

@Getter
public class ClientGuessMessage extends Message {
    private final int guessType;

    public ClientGuessMessage(String messageHeader, String messageBody, int guessType) {
        super(messageHeader, messageBody);
        this.guessType = guessType;
    }

    public String toString() {
        return getMessageHeader() + Constants.DELIMITER + getMessageBody() + Constants.DELIMITER + guessType;
    }
}
