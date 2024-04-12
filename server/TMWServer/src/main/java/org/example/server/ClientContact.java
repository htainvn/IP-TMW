package org.example.server;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import lombok.Getter;
import lombok.Setter;
import org.example.util.ServerInfo;
import org.example.util.ServerInfo.UserRegistrationStatus;
import org.springframework.stereotype.Component;

@Component
public class ClientContact {
  @Getter
  @Setter
  private Integer expectedClient = 0;
  private ArrayList<SocketChannel> clients = new ArrayList<>();
  private Dictionary<SocketChannel, String> names = new Hashtable<SocketChannel, String>();
  private Dictionary<String, Boolean> blacklist = new Hashtable<String, Boolean>();
  private UserRegistrationStatus isValid(String name) {
    /*
    The nickname is composed by the following characters ‘a’...’z’, ‘A’...’Z’, ‘0’...’9’, ‘_’  and the length is not longer than 10 characters.
     */
    if (name.length() > 10) {
      return UserRegistrationStatus.NAME_TOO_LONG;
    }
    if (!name.matches("[a-zA-Z0-9_]+")) {
      return UserRegistrationStatus.INVALID_NAME;
    }
    return UserRegistrationStatus.NAME_VALID;
  }

  public Boolean canAcceptNewClient() {
    // reject the client if the client contact is full
    return names.size() != ServerInfo.MAXIMUM_PLAYERS;
  }

  public UserRegistrationStatus addClient(SocketChannel client, String name) {
    if (names.get(client) != null) {
      return UserRegistrationStatus.ALREADY_REGISTERED;
    }
    UserRegistrationStatus status = isValid(name);
    if (status != UserRegistrationStatus.NAME_VALID) {
      return status;
    }
    Enumeration<String> e = names.elements();
    while (e.hasMoreElements()) {
      if (e.nextElement().equals(name)) {
        return UserRegistrationStatus.NAME_DUPLICATED;
      }
    }
    if (canAcceptNewClient()) {
      names.put(client, name);
      clients.add(client);
      blacklist.put(name, false);
      return UserRegistrationStatus.ACCEPTED;
    }
    else {
      return UserRegistrationStatus.REJECTED;
    }
  }

  public void reset() {
    try {
      while (names.keys().hasMoreElements()) {
        names.remove(names.keys().nextElement());
      }
      while (clients.iterator().hasNext()) {
        clients.remove(clients.iterator().next());
      }
      while (blacklist.keys().hasMoreElements()) {
        blacklist.remove(blacklist.keys().nextElement());
      }
      expectedClient = 0;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void disqualify(SocketChannel client) {
    blacklist.put(names.get(client), true);
  }

  public Boolean isDisqualified(SocketChannel client) {
    return blacklist.get(names.get(client));
  }

  public Integer getIndex(SocketChannel client) {
    return clients.indexOf(client);
  }

  public String getName(SocketChannel client) { return names.get(client); }

  public SocketChannel get(int index) { return clients.get(index); }

  public Integer getNumberOfClients() { return clients.size(); }

  public SocketChannel[] getCatalog() {
    SocketChannel[] catalog = new SocketChannel[clients.size()];
    for (int i = 0; i < clients.size(); i++) {
      catalog[i] = clients.get(i);
    }
    return catalog;
  }
}