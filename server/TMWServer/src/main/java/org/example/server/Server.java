package org.example.server;

import static java.lang.String.format;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class Server implements IServer {
  Selector selector;
  ServerSocketChannel socket;
  public Server() {
    try {
      // Code sao cho caí này chạy đúng là oke
      int port = 7777; // change this
      this.start(port);
      while (true) {
        selector.select();

        Set<SelectionKey> selectorKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectorKeys.iterator();

        while (keyIterator.hasNext()) { // need to check if there are enough players
          SelectionKey key = keyIterator.next();
          if (key.isAcceptable()) { // if find new connection
            SocketChannel player = socket.accept(); // new player comes
            this.onConnection(player); // handle when there is a connection
          }
          else if (key.isReadable()) { // if there is a message
            SocketChannel player = (SocketChannel) key.channel();
            this.onMessage(player); // handle when there is a message
          }
        }
      }

    } catch (Exception e) {
      this.onError(e);
    }
  }

  @Override
  public void start(int port) throws Exception {
    selector = Selector.open();
    socket = ServerSocketChannel.open();
    socket.configureBlocking(false);
    InetSocketAddress serverSocketAddr = new InetSocketAddress("localhost", port);
    socket.bind(serverSocketAddr);
    System.out.println(format("Server started on port %d", port));
  }

}
