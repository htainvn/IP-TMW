package org.example.client;

import org.example.models.*;
import org.example.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Component
public class EventHandler implements IEventHandler {

    private Storage storage;

    @Autowired
    public EventHandler(final Storage storage) {
        this.storage = storage;
    }

    @Override
    public void onConnectionRespond(@NotNull ServerMessage msg) {
        System.out.println("onConnectionRespond");
    }

    @Override
    public void onGuessRespond(@NotNull ServerMessage msg) {
        System.out.println("onGuessRespond");
    }

    @Override
    public void onStartGame(@NotNull GameInfoMessage msg) {
        System.out.println("onStartGame");
        storage.setGameID(msg.getGameID());
        storage.setClientName(msg.getClientName());
        storage.setGameOrder(Integer.valueOf(msg.getGameOrder()));
    }

    @Override
    public void onEndGame(@NotNull ServerMessage msg) {
        System.out.println("onEndGame");
    }

    @Override
    public void onRankingAnnounce(@NotNull RankingAnnounce msg) {
        System.out.println("onRankingAnnounce");
        if(!Objects.equals(msg.getGameID(), storage.getGameID())) return;
        storage.setScores(msg.getPlayers());
    }

    @Override
    public void onTurn() {
        System.out.println("onTurn");
        sendGuessRequest('o', "Address Resolution Protocol");
    }

    public void sendRegisterRequest(@NotNull String clientName) {
        System.out.println("sendRegisterRequest");
        RegisterReqMessage registerReqMessage = new RegisterReqMessage(clientName);
        MessageSender.send(SocketClient.client, registerReqMessage);
    }

    public void sendGuessRequest(@NotNull char guessChar, String guessWord) {
        System.out.println("sendGuessRequest");
        GuessReqMessage guessReqMessage = new GuessReqMessage(storage.getGameID(), storage.getClientName(), guessChar, guessWord);
        MessageSender.send(SocketClient.client, guessReqMessage);
    }
}