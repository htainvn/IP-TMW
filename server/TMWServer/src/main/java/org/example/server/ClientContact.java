package org.example.server;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import javax.validation.Valid;
import org.example.util.ServerInfo;
import org.example.util.ServerInfo.UserRegistrationStatus;
import org.springframework.stereotype.Component;

@Component
public class ClientContact {

  private ArrayList<SocketChannel> clients;

  private Dictionary<SocketChannel, String> names;

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
      return UserRegistrationStatus.ACCEPTED;
    }
    else {
      return UserRegistrationStatus.REJECTED;
    }
  }

  public Boolean reset() {
    try {
      while (names.keys().hasMoreElements()) {
        names.remove(names.keys().nextElement());
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Integer getIndex(SocketChannel client) {
    return clients.indexOf(client);
  }

  public String getName(SocketChannel client) { return names.get(client); }

  public SocketChannel get(int index) { return clients.get(index); }

  public Integer getNumberOfClients() { return clients.size(); }

}