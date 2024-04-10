package org.example.client;

import org.example.models.GameInfoMessage;
import org.example.models.ServerMessage;
import org.example.util.Decoder;
import org.example.util.SeverInfo;
import org.example.util.Constants;
import org.example.util.SeverInfo.MessageType;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Set;
import java.util.Iterator;

import java.io.IOException;

import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

@Component
@NoArgsConstructor
public class SocketClient implements IClient {

    private final InetSocketAddress serverAddress = new InetSocketAddress("localhost", Constants.SEVER_PORT);

    private Selector selector;
    private SocketChannel client;
    private Boolean stillConnected = false;

    private EventHandler eventHandler;

    @Autowired
    public SocketClient(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        System.out.println("Client created");
    }

    public void init() throws IOException {
        System.out.println("Initializing SocketClient");
        selector = Selector.open();
        connect();
        while( stillConnected ) {
            if(selector.select() == -1) stop();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) onMessage();
                iterator.remove();
            }
        }
    }

    public void connect() throws IOException {
        System.out.println("Connecting to " + serverAddress);
        try {
            client = SocketChannel.open(serverAddress);
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            stillConnected = true;
            System.out.println("Connected");
        } catch (IOException e) {
            System.out.println("Could not connect to " + serverAddress);
            throw new RuntimeException(e);
        }
    }

    public void stop() throws IOException {
        client.close();
        stillConnected = false;
        System.out.println("Client stopped");
    }

    public void onMessage() throws IOException {

        String raw_message;

        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            raw_message = new String(buffer.array(), 0, buffer.limit());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
        MessageType messageType = Decoder.decode(raw_message);

            switch (messageType) {
                case CONNECTION:
                    eventHandler.onConnectionRespond(
                            Objects.requireNonNull(ServerMessage.fromString(raw_message))
                    );
                    break;
                case GUESS:
                    eventHandler.onGuessRespond(
                            Objects.requireNonNull(ServerMessage.fromString(raw_message))
                    );
                    break;
                case RANKING:
                    eventHandler.onRankingAnnounce(
                            Objects.requireNonNull(ServerMessage.fromString(raw_message))
                    );
                    break;
                case GAME_START:
                    eventHandler.onStartGame(
                            Objects.requireNonNull(GameInfoMessage.fromString(raw_message))
                    );
                    break;
                case GAME_END:
                    eventHandler.onEndGame(
                            Objects.requireNonNull(ServerMessage.fromString(raw_message))
                    );
                    break;
                default:
                    throw new RuntimeException("Unknown message type: " + messageType);
            }
        } catch (RuntimeException e) {
            System.err.println("Client exception: " + e.getMessage());
        }
    }

    @Override
    public void onError(Exception e) {

    }

}
