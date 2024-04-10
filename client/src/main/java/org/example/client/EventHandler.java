package org.example.client;

import lombok.NoArgsConstructor;
import org.example.models.ServerMessage;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@NoArgsConstructor
public class EventHandler implements IEventHandler {

    @Override
    public void onConnectionRespond(@NotNull ServerMessage msg) {
        System.out.println("onConnectionRespond");
    }

    @Override
    public void onGuessRespond(@NotNull ServerMessage msg) {
        System.out.println("onGuessRespond");
    }

    @Override
    public void onStartGame(@NotNull ServerMessage msg) {
        System.out.println("onStartGame");
    }

    @Override
    public void onEndGame(@NotNull ServerMessage msg) {
        System.out.println("onEndGame");
    }

    @Override
    public void onRankingAnnounce(@NotNull ServerMessage msg) {
        System.out.println("onRankingAnnounce");
    }
}
