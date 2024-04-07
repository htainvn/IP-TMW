package org.example.server;

import static java.lang.String.format;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;

import java.util.*;
import java.util.regex.Pattern;

import org.example.model.ClientGuessMessage;
import org.example.model.GameManager;
import org.example.model.Message;
import org.example.util.Constants;
import org.springframework.stereotype.Component;

@Component
public class Server implements IServer {

  // Private variable
  private int clients = 0;
  private Selector selector;
  private ServerSocketChannel socket;
  private ByteBuffer buffer = ByteBuffer.allocate(2048);
  private Map<SocketChannel, String> connectedClients = new HashMap<SocketChannel, String>();
  private SocketChannel[] clientList = new SocketChannel[Constants.MAX_CLIENT_CONNECTIONS];
  private GameManager currentGame = null;

  // Temporary testing variable
  private final String keyword = "Advanced Program in Computer Science";
  private final String hint = "University";

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
  public void onMessage(SelectionKey clientKey) throws IOException {

    SocketChannel client = (SocketChannel) clientKey.channel();

    // Read the message from clients
    buffer.clear();
    client.read(buffer);
    String raw_message = new String(buffer.array(), 0, buffer.limit());
    System.out.println(raw_message);
    Message rcv_message = decodeMessage(raw_message);

    // Handle registering phase ( whether register name valid or not )
    if(Objects.equals(rcv_message.getMessageHeader(), Constants.CLIENT_REGISTER)) {
      Message response_message = checkValidRegisterName(client, rcv_message.getMessageBody());

      try {
        // If register name is valid
        if(response_message.getMessageHeader().equals(Constants.REGISTER_SUCCESS)) {
          // Accept new client
          if(Objects.equals(connectedClients.get(client), "")) {
            ++clients;
            clientList[clients] = client;
          }
          // Update client name
          connectedClients.put(client, rcv_message.getMessageBody());
        }
        send( client, response_message );

        // If there are enough players
        if( clients == Constants.MAX_CLIENT_CONNECTIONS ) {
         currentGame = new GameManager( keyword, hint, getClientList() );
        }

      } catch (IOException e) {
        send( client, new Message( Constants.SEVER_ERROR ) );
        throw new RuntimeException(e);
      }
      return;
    }

    // Handle responds from user ( turn-based, right form, ... )
  }

  @Override
  public void onConnection() throws IOException {

    SocketChannel client = socket.accept();

    if( clients == Constants.MAX_CLIENT_CONNECTIONS ) {
      System.out.printf("Number of clients reach the limitation.%n");
      send( client, new Message( Constants.CONNECTION_REJECT, "Number of clients reach the limitation." ) );
      client.close();
      return;
    }

    if( currentGame != null ) {
      System.out.printf("Current game is on: %s%n");
      send( client, new Message( Constants.CONNECTION_REJECT, "Current game is on" ) );
      client.close();
      return;
    }

    // Register client with the Selector
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_READ);
    send(client, new Message(Constants.CONNECTION_ACCEPT) );

    // Store client into Map
    connectedClients.put(client, "");

    // Send message for register name
    Message registerMessage = new Message( Constants.SEVER_REGISTER );
    send(client, registerMessage);
  }

  @Override
  public void onError(Exception e) {

  }

  @Override
  public void send(SocketChannel client, Message message) throws IOException {
    try {
      byte[] messageBytes = message.toString().getBytes(Constants.CHARSET);
      ByteBuffer encode = ByteBuffer.wrap(messageBytes);
      client.write(encode);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void broadcast(Message message) throws IOException {
    try {
      byte[] messageBytes = message.toString().getBytes(Constants.CHARSET);
      ByteBuffer encode = ByteBuffer.wrap(messageBytes);
      for( SocketChannel client : connectedClients.keySet() ) {
        client.write(encode);
        encode.rewind();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean checkRegex(String input) {
    // Compile the pattern
    Pattern pattern = Pattern.compile(Constants.REGEX);

    // Check if the input string matches the pattern and falls within the specified length range
    return pattern.matcher(input).matches();
  }

  private Message checkValidRegisterName( SocketChannel client, String registerName ) throws IOException {

    if( !checkRegex(registerName) ) {
      return new Message( Constants.REGISTER_ERROR, "Invalid register name: " + registerName);
    }

    for( Map.Entry<SocketChannel, String> entry : connectedClients.entrySet() ) {
      if( registerName.equals( entry.getValue() ) ) {
        return new Message( Constants.REGISTER_ERROR, "This name is already in use!");
      }
    }

    return new Message(Constants.REGISTER_SUCCESS);
  }

  private Message decodeMessage( String message ) throws IOException {
    String[] messagePart = message.split(Constants.DELIMITER);
    if( messagePart.length != 2 ) {
      return new ClientGuessMessage( messagePart[0], messagePart[1], Integer.parseInt(messagePart[2]) );
    }
    return new Message( messagePart[0], messagePart[1] );
  }

  private String[] getClientList() {
    String[] result = new String[clients];
    for( SocketChannel client : clientList ) {
      result[clients] = connectedClients.get(client);
    }
    return result;
  }
}
