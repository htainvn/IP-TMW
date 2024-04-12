package org.example.models;

import lombok.Getter;
import org.example.util.Constants;
import org.example.util.ServerInfo;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
public class GameStateMessage extends ServerMessage {

    protected String gameID;
    protected String hint;
    protected String currentKeyword;

    public static @Nullable GameStateMessage fromString(@NotNull String message) {

        GameStateMessage msg = new GameStateMessage();

        try {
            String[] lines = message.split(Constants.DELIMITER);

            msg.messageHeader = lines[0].split(":")[1].trim();
            msg.fromHost = lines[1].split(":")[1].trim();
            msg.fromPort = Integer.parseInt(lines[2].split(":")[1].trim());
            msg.status = lines[3].split(":")[1].trim();

            try {
                msg.optionalMessageBody = lines[4].split(":")[1].trim();
            } catch (Exception e) {
                msg.optionalMessageBody = "null";
            }

            try {
                if (lines.length > ServerInfo.SERVER_MESSAGE_LENGTH) {
                    msg.gameID = lines[4].split(":")[2].trim();
                    msg.hint = lines[5].split(":")[1].trim();
                    String tmp = lines[6].split(":")[1].trim();
                    msg.currentKeyword = "";
                    for (int i = 1; i < tmp.length(); i += 3) {
                        msg.currentKeyword += tmp.charAt(i);
                    }
                    msg.currentKeyword = msg.currentKeyword.toUpperCase();
                    System.out.println("Current keyword: " + msg.currentKeyword);
                }
            } catch (Exception e) {
                msg.gameID = "null";
                msg.hint = "null";
                msg.currentKeyword = "null";
            }

            return msg;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
