package org.example.storage;

import org.example.observer.ConnectingState;
import org.javatuples.Pair;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Vector;

@Getter
@Setter
@Component
public class Storage {
    private String gameID;
    private String clientName;
    private Integer gameOrder = 0;

    private String hint;
    private String keyword;
    private Integer time;

    private Integer currentPlayer;
    private Boolean isDisqualified = false;

    private Boolean isAccepted = false;

    private Vector<Pair<String, Integer>> scores;

    private Enum<GamePhase> currentPhase = GamePhase.WAITING;
    private Boolean isAuthenticated = null;

    public void resetStorage() {
        gameID = null;
        clientName = null;
        gameOrder = 0;
        hint = null;
        keyword = null;
        scores = null;
        time = null;
        currentPlayer = null;
        isDisqualified = false;
        isAuthenticated = null;
        currentPhase = GamePhase.WAITING;
        isAccepted = false;
    }

    public Number getPoint(String name) {
        if(scores == null) return 0;
        for (Pair<String, Integer> score : scores) {
            if (score.getValue0().equals(name)) {
                return score.getValue1();
            }
        }
        return 0;
    }

    public void setInitialScores(String[] players) {
        scores = new Vector<>();
        for (String player : players) {
            scores.add(new Pair<>(player, 0));
        }
    }
}
