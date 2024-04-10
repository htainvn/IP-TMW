package org.example.models;

import lombok.*;
import org.example.client.Message;
import org.example.util.ClientInfo;
import org.example.util.Constants;

@Getter
@NoArgsConstructor
public class RegisterReqMessage extends Message {

    protected String clientName;

    public RegisterReqMessage(@NonNull String clientName) {
        super(ClientInfo.REGISTER, Constants.SERVER_IP, Constants.SEVER_PORT);
        this.clientName = clientName;
    }

    public RegisterReqMessage(String toHost, Integer toPort, @NonNull String clientName) {
        super(ClientInfo.REGISTER, toHost, toPort);
        this.clientName = clientName;
    }

    public String toString() {
        return "Header: " + messageHeader
                + Constants.DELIMITER
                + "Host: " + toHost
                + Constants.DELIMITER
                + "Port: " + toPort
                + Constants.DELIMITER
                + "ClientName: " + clientName
                + Constants.DELIMITER;
    }

}
