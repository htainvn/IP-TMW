package org.example.observer;

import org.example.server.EventHandler;
import org.example.server.GameController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
public class GameObserver {
  EventHandler eventHandler;
  GameController gameController;

  @Autowired
  public GameObserver(EventHandler eventHandler, GameController gameController) {
    this.eventHandler = eventHandler;
    this.gameController = gameController;
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
}
