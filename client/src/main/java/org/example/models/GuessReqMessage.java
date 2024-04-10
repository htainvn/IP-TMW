package org.example.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.example.client.Message;
import org.example.util.ClientInfo;
import org.example.util.Constants;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class GuessReqMessage extends Message {

    private String gameID;
    private String clientName;
    private Character guessChar;
    private String guessWord;

    public GuessReqMessage(
            @NotNull String toHost,
            @NonNull Integer toPort,
            @NonNull String gameID,
            @NonNull String clientName,
            @NonNull Character guessChar,
            @Nullable String guessWord)
    {
        super(ClientInfo.GUESS, toHost, toPort);
        this.gameID = gameID;
        this.clientName = clientName;
        this.guessChar = guessChar;
        this.guessWord = guessWord;
    }

    public String toString() {
        return "Header" + messageHeader
                + Constants.DELIMITER
                + "Host: " + toHost
                + Constants.DELIMITER
                + "Port: " + toPort
                + Constants.DELIMITER
                + "GameID: " + gameID
                + Constants.DELIMITER
                + "ClientName: " + clientName
                + Constants.DELIMITER
                + "GuessChar: " + guessChar
                + Constants.DELIMITER
                + "GuessWord: " + guessWord
                + Constants.DELIMITER;
    }
}
