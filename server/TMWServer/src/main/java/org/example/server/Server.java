package org.example.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;

import java.util.*;

import org.example.model.GameManager;
import org.example.model.Message;
import org.example.models.GuessReqMessage;
import org.example.models.RegisterRequestMessage;
import org.example.models.ServerMessage;
import org.example.util.Constants;
import org.example.util.Decoder;
import org.example.util.ServerInfo.MessageType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Server implements IServer {

  // Private variable
  private int clients = 0;
  private Selector selector;
  private ServerSocketChannel socket;
  private final ByteBuffer buffer = ByteBuffer.allocate(2048);
  private Map<SocketChannel, String> connectedClients = new HashMap<SocketChannel, String>();
  private SocketChannel[] clientList = new SocketChannel[Constants.MAX_CLIENT_CONNECTIONS];
  private GameManager currentGame = null;
  // Temporary testing variable
  private final String keyword = "Advanced Program in Computer Science";
  private final String hint = "University";

  @Autowired
  private EventHandler eventHandler;

  @Autowired
  private GameController gameController;

  public Server() throws IOException {
    try {
      // Start the Sever
      this.start(Constants.SEVER_PORT);

      while (true) {

        selector.select();

        Set<SelectionKey> selectorKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectorKeys.iterator();

        while (keyIterator.hasNext()) {
          SelectionKey key = keyIterator.next();

          if (key.isAcceptable()) this.onConnection();
          if (key.isReadable()) this.onMessage(key);
          keyIterator.remove();
        }
      }

    } catch (Exception e) {
      this.onError(e);
    }
  }

  @Override
  public void start(int port) throws Exception {
    // Open Selector
    selector = Selector.open();

    // Open ServerSocket
    socket = ServerSocketChannel.open();

    // Set ServerSocket to non-blocking
    socket.configureBlocking(false);

    // Binding server to given port number
    InetSocketAddress serverSocketAddr = new InetSocketAddress("localhost", port);
    socket.bind(serverSocketAddr);

    // Registering server onto selector
    socket.register( selector, SelectionKey.OP_ACCEPT );

    // Notify that the sever is successfully start
    System.out.printf("Server started on port %d%n", port);
  }

  @Override
  public void stop() throws Exception {

  }

  @Override
  public void onMessage(@NotNull SelectionKey socket) throws Exception {
    SocketChannel client;
    client = (SocketChannel) socket.channel();

    String raw_message;

    // Read the message from clients
    try {
      buffer.clear();
      client.read(buffer);
      raw_message = new String(buffer.array(), 0, buffer.limit());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    MessageType messageType = Decoder.decode(raw_message);

    if (messageType == MessageType.GUESS) {
      ServerMessage resp = eventHandler.onGuessingRequest(
          client,
          GuessReqMessage.fromString(raw_message)
      );
      MessageSender.send(client, resp);

    }
    else if (messageType == MessageType.REGISTER) {
      ServerMessage resp = eventHandler.onRegistrationRequest(
          client,
          Objects.requireNonNull(RegisterRequestMessage.fromString(raw_message))
      );
      MessageSender.send(client, resp);
    }

  }

  @Override
  public void onConnection() throws IOException {

  }

  @Override
  public void onError(Exception e) {

  }

  public void startGame() {
    gameController.setRandomQuiz();
  }
}