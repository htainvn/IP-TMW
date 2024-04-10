package org.example.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;

import java.util.*;

import lombok.NoArgsConstructor;
import org.example.models.GuessReqMessage;
import org.example.models.RegisterRequestMessage;
import org.example.models.ServerMessage;
import org.example.util.Constants;
import org.example.util.Decoder;
import org.example.util.ServerInfo;
import org.example.util.ServerInfo.MessageType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class Server implements IServer {

  // Private variable
  private Selector selector;
  private ServerSocketChannel socket;
  private Vector<SocketChannel> connectedClients = new Vector<>();

  private EventHandler eventHandler;

  private GameController gameController;

  private ClientContact clientContact;

  private Boolean keepServerRunning = true;

  @Autowired
  public Server(
      EventHandler eventHandler,
      GameController gameController,
      ClientContact clientContact
  ) throws IOException {
    this.eventHandler = eventHandler;
    this.gameController = gameController;
    this.clientContact = clientContact;
    System.out.println("Server created");
  }

  public void init() {
    try {
      // Start the Sever
      this.start(Constants.SEVER_PORT);

      while (keepServerRunning) {

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
      e.printStackTrace();
//      this.onError(e);
    }
  }

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
    // Initialize new name
    try {
      gameController.newGame();
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void stop() throws Exception {
    keepServerRunning = false;
    socket.close();
    System.exit(0);
  }

  @Override
  public void onMessage(@NotNull SelectionKey socket) throws Exception {
    SocketChannel client;
    client = (SocketChannel) socket.channel();

    String raw_message;

    // Read the message from clients
    try {
      ByteBuffer buffer = ByteBuffer.allocate(512);
      client.read(buffer);
      raw_message = new String(buffer.array(), 0, buffer.limit());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    MessageType messageType = Decoder.decode(raw_message);

    if (messageType == MessageType.GUESS) {
      ServerMessage resp;
      try {
        resp = eventHandler.onGuessingRequest(
            client,
            Objects.requireNonNull(GuessReqMessage.fromString(raw_message))
        );
      }
      catch (NullPointerException e) {
        resp = ServerMessage.builder()
            .messageHeader(ServerInfo.WRONG_FORMAT)
            .fromHost(ServerInfo.SERVER_HOST)
            .fromPort(ServerInfo.SERVER_PORT)
            .status(ServerInfo.STATUS_ERROR)
            .optionalMessageBody("")
            .build();
      }
      MessageSender.send(client, resp);
      if (resp.getOptionalMessageBody().equals("Correct guess") ||
          resp.getOptionalMessageBody().equals("Incorrect guess")
      ) {
        eventHandler.onGuessBeforeTimeUp();
      }
    }
    else if (messageType == MessageType.REGISTER) {
      ServerMessage resp;
      try {
        resp = eventHandler.onRegistrationRequest(
            client,
            Objects.requireNonNull(RegisterRequestMessage.fromString(raw_message))
        );
      } catch (Exception e) {
        resp = ServerMessage.builder()
            .messageHeader(ServerInfo.WRONG_FORMAT)
            .fromHost(ServerInfo.SERVER_HOST)
            .fromPort(ServerInfo.SERVER_PORT)
            .status(ServerInfo.STATUS_ERROR)
            .build();
      }
      MessageSender.send(client, resp);
    }

  }

  @Override
  public void onConnection() throws IOException {
    try {
      SocketChannel client = socket.accept();
      client.configureBlocking(false);
      client.register(selector, SelectionKey.OP_READ);
      connectedClients.add(client);
      System.out.printf("New client connected: %s%n", client.getRemoteAddress());
    } catch (IOException e) {
      System.out.println(e.getMessage());
//      throw new RuntimeException(e);
    }
  }

  @Override
  public void onError(Exception e) {

  }

  public void startGame() throws IOException {
    for (SocketChannel client : connectedClients) {
      if( clientContact.getName((client)) == null ) {
        connectedClients.remove(client);
        client.close();
      }
    }
    eventHandler.onGameStart();
  }
}