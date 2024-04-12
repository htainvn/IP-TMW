package org.example.observer;

import lombok.Getter;
import lombok.Setter;
import org.example.client.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@DependsOn("socketClient")
@Order(Ordered.LOWEST_PRECEDENCE)
public class UIObserver {
    private SocketClient socketClient;

    @Getter
    @Setter
    Enum<Phase> currentPhase = Phase.REGISTRATION;

    @Getter
    @Setter
    Enum<ConnectingState> currentState = ConnectingState.WAITING;

    public void reset() {
        currentPhase = Phase.REGISTRATION;
        currentState = ConnectingState.WAITING;
    }

    @Autowired
    public UIObserver(SocketClient socketClient) {
        this.socketClient = socketClient;
        System.out.println("UIObserver started");
    }

    public void observeRegisterSignal(@NotNull String clientName) {
        socketClient.sendRegister(clientName);
    }

    public void observeGuessSignal(char guessChar, String guessWord) {
        socketClient.sendGuess(guessChar, guessWord);
    }

    public void connectServer(String dest, String port) {
        try {
            socketClient.connect(dest, port);
            currentState = ConnectingState.CONNECTED;
        } catch (Exception e) {
            System.out.println("Error connecting to server " + dest + ":" + port);
            currentState = ConnectingState.FAILED;
        }
    }

    public Boolean isConnected() {
        return socketClient.isConnected();
    }

    public void setUsername(String username) {
        socketClient.sendRegister(username);

    }
    public void sendGuess(char guessChar, String guessWord) {
        socketClient.sendGuess(guessChar, guessWord);
    }
}
