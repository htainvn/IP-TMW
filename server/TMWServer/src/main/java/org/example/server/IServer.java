package org.example.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.example.model.Message;
import org.example.models.ServerMessage;

public interface IServer {

  void start(int port) throws Exception;

  void stop() throws Exception;

  void onMessage(SelectionKey socket) throws Exception;

  void onConnection() throws IOException;

  void onError(Exception e);

}
