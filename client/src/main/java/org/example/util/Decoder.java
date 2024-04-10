package org.example.util;

import javax.validation.constraints.NotNull;
import org.example.util.SeverInfo.MessageType;

public class Decoder {
    public static MessageType decode(@NotNull String message) {

        System.out.println(message);
        String messageType = null;
        try {
            String[] lines = message.split(Constants.DELIMITER);
            messageType = lines[0].split(":")[1].trim();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        switch (messageType) {
            case SeverInfo.CONNECTION_ACCEPTED:
            case SeverInfo.CONNECTION_REJECTED:
                return MessageType.CONNECTION;
            case SeverInfo.GUESS_ACCEPTED:
            case SeverInfo.GUESS_REJECTED:
                return MessageType.GUESS;
            case SeverInfo.GAME_START:
                return MessageType.GAME_START;
            case SeverInfo.GAME_END:
                return MessageType.GAME_END;
            case SeverInfo.RANKING_ANNOUNCE:
                return MessageType.RANKING;
        }

        return null;
    }
}
