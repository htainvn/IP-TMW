package org.example.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.example.models.ServerMessage;
import org.example.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
  public static void send(@NotNull SocketChannel client, @NotNull ServerMessage msg) throws RuntimeException {
    try {
      byte[] messageBytes = msg.toString().getBytes(Constants.CHARSET);
      ByteBuffer encode = ByteBuffer.wrap(messageBytes);
      client.write(encode);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void broadcast(@NotNull SocketChannel[] clients, @NotNull ServerMessage msg) throws RuntimeException {
    for (SocketChannel client : clients) {
      send(client, msg);
    }
  }
}