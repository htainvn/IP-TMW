package org.example.util;

import javax.validation.constraints.NotNull;
import org.example.util.ServerInfo.MessageType;

public class Decoder {
    public static MessageType decode(@NotNull String message) {

        //System.out.println("Decoding: " + message);
        String messageType = null;
        try {
            String[] lines = message.split(Constants.DELIMITER);
            messageType = lines[0].split(":")[1].trim();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        switch (messageType) {
            case ServerInfo.DISCONNECTED:
                return MessageType.DISCONNECTED;
            case ServerInfo.CONNECTION_ACCEPTED:
            case ServerInfo.CONNECTION_REJECTED:
                return MessageType.CONNECTION;
            case ServerInfo.GUESS_ACCEPTED:
            case ServerInfo.GUESS_REJECTED:
                return MessageType.GUESS;
            case ServerInfo.GAME_START:
                return MessageType.GAME_START;
            case ServerInfo.GAME_END:
                return MessageType.GAME_END;
            case ServerInfo.YOUR_TURN:
                return MessageType.YOUR_TURN;
            case ServerInfo.RANKING_ANNOUNCE:
                return MessageType.RANKING;
        }

        return null;
    }
}
