package org.example.observer;

import org.example.models.Player;
import org.example.server.EventHandler;
import org.example.server.GameController;
import org.example.server.ScoreBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Dictionary;
import java.util.Enumeration;

@Component
public class GameObserver {
  EventHandler eventHandler;
  GameController gameController;
  ScoreBoard scoreBoard;

  @Autowired
  public GameObserver(EventHandler eventHandler, GameController gameController, ScoreBoard scoreBoard) {
    this.eventHandler = eventHandler;
    this.gameController = gameController;
    this.scoreBoard = scoreBoard;
    System.out.println("GameObserver created");
  }

  public String getHint() {
    return gameController.getHint();
  }

  public Boolean isGamePlayable() {
    return gameController.isGamePlayable();
  }

  public Integer getCurrentIteration() {
    return gameController.getCurrentIteration();
  }
  public Player[] getScoreboard() {
    Dictionary<String, Integer> scores = scoreBoard.getScores();
    Player[] players = new Player[scores.size()];

    int i = 0;
    Enumeration<String> names = scores.keys();
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      players[scores.size() - i - 1] = new Player(1, name, scores.get(name));
      i++;
    }

    // depend on player's score, set their rank but don't change the order
    for (int j = 0; j < players.length; j++) {
      for (int k = 0; k < players.length; k++) {
        if (players[j].getPoint() < players[k].getPoint()) {
          players[j].setRank(players[j].getRank() + 1);
        }
      }
    }

    return players;
  }
}
