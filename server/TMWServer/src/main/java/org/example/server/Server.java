package org.example.server;

import static java.lang.String.format;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.example.model.Message;
import org.springframework.stereotype.Component;

@Component
public class Server implements IServer {

  // Constant variable
  private static final int PORT = 7777;
  private static final int MAX_CLIENTS = 10;

  // Private variable
  private int clients = 0;
  private Selector selector;
  private ServerSocketChannel socket;
  private ByteBuffer buffer = ByteBuffer.allocate(2048);
  private Map<SocketChannel, String> connectedClients = new HashMap<SocketChannel, String>();

  public Server() throws IOException {

    try {
      // Start the Sever
      this.start(PORT);

      while (true) {


        selector.select();

        Set<SelectionKey> selectorKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectorKeys.iterator();

        while (keyIterator.hasNext()) { // need to check if there are enough players
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
  public void onMessage(SelectionKey clientKey) throws IOException {

    SocketChannel client = (SocketChannel) clientKey.channel();

    // Read the message from clients  ( Done )
    buffer.clear();
    String msg = new String(buffer.array(), 0, buffer.limit());
    System.out.println(msg);

    // Handle registering phase ( register name not duplicate )

    // If all players are ready initialize the game

    // Handle responds from user ( turn-based, right form, ... )
  }

  @Override
  public void onConnection() throws IOException {

    if( clients == MAX_CLIENTS ) {
      System.out.printf("Number of clients reach the limitation.%n");
      return;
    }

    ++clients;
    SocketChannel client = socket.accept();

    // Register client with the Selector
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_READ);

    // Store client into Map
    connectedClients.put(client, "");

    // Send message for register name

  }

  @Override
  public void onError(Exception e) {

  }

  @Override
  public void send(SocketChannel socket, Message message) throws IOException {

  }

}
