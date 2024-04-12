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
            case ServerInfo.GAME_STATE:
                return MessageType.GAME_STATE;
            case ServerInfo.WINNER_ANNOUNCE:
                return MessageType.WINNER_ANNOUNCE;
            case ServerInfo.DISQUALIFY_ANNOUNCE:
                return MessageType.DISQUALIFY_ANNOUNCE;
        }

        return null;
    }
}
