package org.example.model;

import lombok.Getter;
import lombok.Setter;
import org.example.util.Constants;

import java.nio.channels.SocketChannel;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

public class GameManager {
    private final String keyword;
    private final String hint;
    private int currentTurn;
    private int currentPlayer;
    private int[] scores;
    private String[] playerList;

    public GameManager(String keyword, String hint, String[] clientList) {
        this.keyword = keyword.toUpperCase();
        this.hint = hint;
        this.currentTurn = 0;
        this.currentPlayer = 0;
        this.playerList = clientList;
        scores = new int[playerList.length];
    }

    public GameResponse takeTurn( String clientName, String playerGuess ) {
        if(!Objects.equals(playerList[currentPlayer], clientName)) {
            return new GameResponse(Constants.NOT_YOUR_TURN);
        }

        playerGuess = playerGuess.toUpperCase();
        return new GameResponse(1);
    }
}