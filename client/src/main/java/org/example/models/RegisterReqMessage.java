package org.example.models;

import lombok.*;
import org.example.util.ClientInfo;
import org.example.util.Constants;
import org.example.util.Validator;

@Getter
@NoArgsConstructor
public class RegisterReqMessage extends Message {

    protected String clientName;

    public RegisterReqMessage(@NonNull String clientName) {
        super(ClientInfo.REGISTER, Validator.connectedHost, Validator.connectedPort);
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
