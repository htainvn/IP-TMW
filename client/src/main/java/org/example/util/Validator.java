package org.example.util;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.ServerMessage;
import org.springframework.stereotype.Component;

import java.util.Objects;

@NoArgsConstructor
@Component
public class Validator {

    private String connectedHost;
    private Integer connectedPort;

    public void resetValidator() {
        connectedHost = null;
        connectedPort = null;
    }

    public void setupValidator( String host, Integer port ) {
        if( host == null || host.isEmpty() ) {
            connectedPort = port;
            connectedHost = host;
        }
    }

    public boolean validate( String msg ) {

        if( msg == null || msg.isEmpty() ) {
            return false;
        }

        ServerMessage sm = ServerMessage.fromString(msg);
        if( sm == null ) { return false; }

        if( !Objects.equals(sm.getFromHost(), connectedHost) || !Objects.equals(sm.getFromPort(), connectedPort) ) {
            System.out.println("Invalid connect address: " + sm.getFromHost() + ":" + sm.getFromPort());
            System.out.println("Expected: " + connectedHost + ":" + connectedPort);
            return false;
        }
        return true;
    }
}