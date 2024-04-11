package org.example.observer;

import org.example.models.Player;
import org.example.server.ClientContact;
import org.example.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.channels.SocketChannel;

@Component
@DependsOn("server")
@Order(Ordered.LOWEST_PRECEDENCE)
public class UIObserver {
  private Server server;
  private ClientContact clientContact;
  private int phase = 0;

  @Autowired
  public UIObserver(Server server, ClientContact clientContact) {
    this.server = server;
    this.clientContact = clientContact;
    System.out.println("GameObserver created");
  }

  public void observeStartSignal() throws Exception {
    server.startGame();
    setPhase(2);
  }

  public void observeStopSignal() throws Exception {
    server.stop();
  }

  public Player[] getPlayers() {
    SocketChannel[] socketChannels = clientContact.getCatalog();
    Player[] players = new Player[socketChannels.length];
    for (int i = 0; i < socketChannels.length; i++) {
      players[i] = new Player(i, clientContact.getName(socketChannels[i]), 0);
    }
    return players;
  }

  public void setPhase(int phase) {
    System.out.println("Phase set to " + phase);
    this.phase = phase;
  }

    public int getPhase() {
        return this.phase;
    }
}
