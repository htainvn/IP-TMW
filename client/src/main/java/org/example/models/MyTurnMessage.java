package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.util.Constants;
import org.example.util.ServerInfo;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyTurnMessage extends ServerMessage {

    protected Integer time;

    public static @Nullable MyTurnMessage fromString(@NotNull String message) {

        MyTurnMessage msg = new MyTurnMessage();

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
                if (lines.length > ServerInfo.MY_TURN_MESSAGE_LENGTH) {
                    String timeStr[] = lines[4].split(":")[2].trim().split(" ");
                    msg.time = Integer.parseInt(timeStr[0]);
                    if (timeStr[1].trim().equals("seconds")) {
                        System.out.println("Time in seconds: " + msg.time);
                        msg.time *= 1000;
                    } else {
                    }
                }
            } catch (Exception e) {
                msg.time = 0;
            }

            return msg;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
