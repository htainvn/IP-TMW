package org.example.observer;

import org.example.server.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
public class GameObserver {

  @Autowired
  public GameObserver(EventHandler eventHandler) {
    System.out.println("GameObserver created");
  }

}
