package org.example.server;

import java.util.Dictionary;
import java.util.Hashtable;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ScoreBoard {
  private Dictionary<String, Integer> scores = new Hashtable<>();

  public void onNewPlayer(String playerName) {
    scores.put(playerName, 0);
  }

  public void updateScore(String playerName, int score) {
    scores.put(playerName, scores.get(playerName) + score);
  }

  public void reset() {
    while (scores.keys().hasMoreElements()) {
      scores.remove(scores.keys().nextElement());
    }
  }
}
