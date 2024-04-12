package org.example.client;

import org.example.models.*;
import org.example.storage.GamePhase;
import org.example.storage.Storage;
import org.example.util.ServerInfo;
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
        if(Objects.equals(msg.getMessageHeader().trim(), ServerInfo.CONNECTION_ACCEPTED)) storage.setIsAccepted(true);
        else {
            if(storage.getIsAccepted()) return;
            SocketClient.stillConnected = false;
        }
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
        storage.setInitialScores(msg.getAllClientNames());
    }

    @Override
    public void onEndGame(@NotNull ServerMessage msg) {
        System.out.println("onEndGame");
        storage.resetStorage();
    }

    @Override
    public void onRankingAnnounce(@NotNull RankingAnnounce msg) {
        System.out.println("onRankingAnnounce");
        if(!Objects.equals(msg.getGameID(), storage.getGameID())) return;
        storage.setScores(msg.getPlayers());
    }

    @Override
    public void onTurn(@NotNull Integer time) {
        System.out.println("onTurn");
        storage.setTime(time);
        storage.setCurrentPhase(GamePhase.MY_TURN);
//        sendGuessRequest('o', "Address Resolution Protocol");
    }

    public void sendRegisterRequest(@NotNull String clientName) {
        System.out.println("sendRegisterRequest");
        RegisterReqMessage registerReqMessage = new RegisterReqMessage(clientName);
        //System.out.println("sendRegisterRequest: " + registerReqMessage);
        MessageSender.send(SocketClient.client, registerReqMessage);
    }

    public void sendGuessRequest( char guessChar, String guessWord) {
        System.out.println("sendGuessRequest");
        GuessReqMessage guessReqMessage = new GuessReqMessage(storage.getGameID(), storage.getClientName(), guessChar, guessWord);
        MessageSender.send(SocketClient.client, guessReqMessage);
    }

    public void onWinnerAnnounce(@NotNull ServerMessage serverMessage) {
        System.out.println("onWinnerAnnounce");
    }

    public void onDisqualifyAnnounce(@NotNull ServerMessage serverMessage) {
        System.out.println("onDisqualifyAnnounce");
        storage.setIsDisqualified(true);
    }

    public void onGameState(@NotNull GameStateMessage gameStateMessage) {
        System.out.println("onGameState");
        if( storage.getGameID() != null && !gameStateMessage.getGameID().equals(storage.getGameID())) return;
        storage.setGameID(gameStateMessage.getGameID());
        storage.setHint(gameStateMessage.getHint());
        storage.setKeyword(gameStateMessage.getCurrentKeyword());
        storage.setCurrentPlayer(gameStateMessage.getCurrentPlayer());
    }
}
