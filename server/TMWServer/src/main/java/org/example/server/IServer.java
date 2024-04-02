package org.example.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import org.example.model.Message;

public interface IServer {

  void start(int port) throws Exception;

  void stop() throws Exception;

  void onMessage(SocketChannel socket) throws IOException;

  void onConnection(SocketChannel SocketChannel) throws IOException;

  void onError(Exception e);

  void send(SocketChannel socket, Message message) throws IOException;

}
