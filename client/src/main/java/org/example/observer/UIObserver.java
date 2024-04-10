package org.example.observer;

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
}
