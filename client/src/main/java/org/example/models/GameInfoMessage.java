package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.util.Constants;
import org.example.util.SeverInfo;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameInfoMessage extends ServerMessage {

    protected String gameID;
    protected String clientName;
    protected String gameOrder;

    public static @Nullable GameInfoMessage fromString(@NotNull String message) {

        GameInfoMessage msg = new GameInfoMessage();

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
                if (lines.length > SeverInfo.SERVER_MESSAGE_LENGTH) {
                    msg.gameID = msg.optionalMessageBody;
                    msg.clientName = lines[5].split(":")[1].trim();
                    msg.gameOrder = lines[6].split(":")[1].trim();
                }
            } catch (Exception e) {
                msg.gameID = "null";
                msg.clientName = "null";
                msg.gameOrder = "null";
            }

            return msg;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
