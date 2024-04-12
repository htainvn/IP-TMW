package org.example.storage;

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
    private Integer gameOrder;

    private String hint;
    private String keyword;
    private Integer time;

    private Integer currentPlayer;
    private Boolean isDisqualified = false;

    private Boolean isAccepted = false;

    private Vector<Pair<String, Integer>> scores;

    private Enum<GamePhase> currentPhase = GamePhase.WAITING;

    public void resetStorage() {
        gameID = null;
        clientName = null;
        gameOrder = null;
        hint = null;
        keyword = null;
        scores = null;
        time = null;
        currentPlayer = null;
        isDisqualified = false;
    }

    public Number getPoint(String name) {
        if(scores == null) return -1;
        for (Pair<String, Integer> score : scores) {
            if (score.getValue0().equals(name)) {
                return score.getValue1();
            }
        }
        return -1;
    }

    public void setInitialScores(String[] players) {
        scores = new Vector<>();
        for (String player : players) {
            scores.add(new Pair<>(player, 0));
        }
    }
}
