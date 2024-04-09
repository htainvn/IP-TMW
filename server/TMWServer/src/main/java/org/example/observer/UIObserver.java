package org.example.observer;

import org.example.server.IServer;
import org.example.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@DependsOn("server")
@Order(Ordered.LOWEST_PRECEDENCE)
public class UIObserver {
  private Server server;

  @Autowired
  public UIObserver(Server server) {
    this.server = server;
    System.out.println("GameObserver created");
  }

  public void observeStartSignal() throws Exception {
    server.startGame();
  }

  public void observeStopSignal() throws Exception {
    server.stop();
  }
}
