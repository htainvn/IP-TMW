package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.util.Constants;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerMessage {
    protected String messageHeader;
    protected String fromHost;
    protected Integer fromPort;
    protected String status;
    protected String optionalMessageBody;

    public static @Nullable ServerMessage fromString(@NotNull String message) {

        ServerMessage msg = new ServerMessage();

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

            return msg;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
