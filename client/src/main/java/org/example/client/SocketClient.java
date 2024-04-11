package org.example.client;

import org.example.models.GameInfoMessage;
import org.example.models.RankingAnnounce;
import org.example.models.ServerMessage;
import org.example.util.Decoder;
import org.example.util.Constants;
import org.example.util.ServerInfo.MessageType;
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
    public static SocketChannel client;
    private Boolean stillConnected = false;

    private EventHandler eventHandler;

    @Autowired
    public SocketClient(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        System.out.println("GUI created");
    }

    @Override
    public void init() throws IOException {
        System.out.println("Initializing SocketClient");
        selector = Selector.open();
        connect();
//        sendRegister("lvphuc21");
        while( stillConnected ) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext() && stillConnected) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) onMessage();
                iterator.remove();
            }
        }
    }

    @Override
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

    @Override
    public void stop() throws IOException {
        System.out.println("GUI stopped");
        stillConnected = false;
        client.close();
    }

    @Override
    public void onMessage() throws IOException {

        String raw_message;

        try {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            client.read(buffer);
            raw_message = new String(buffer.array(), 0, buffer.limit());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(raw_message.isEmpty()) return;

        String[] subMessages = raw_message.split("Type");
        for(String subMessage : subMessages) {
            resolveMessage(subMessage);
        }
    }

    @Override
    public void onError(Exception e) {

    }

    private void resolveMessage( String raw_message ) {

        if(raw_message.isEmpty()) return;

        try {
            MessageType messageType = Decoder.decode(raw_message);
            //System.out.println("onMessage: " + messageType);

            switch (Objects.requireNonNull(messageType)) {
                case DISCONNECTED:
                    stop();
                    return;
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
                            Objects.requireNonNull(RankingAnnounce.fromString(raw_message))
                    );
                    break;
                case GAME_START:
                    eventHandler.onStartGame(
                            Objects.requireNonNull(GameInfoMessage.fromString(raw_message))
                    );
                    break;
                case YOUR_TURN:
                    eventHandler.onTurn();
                    break;
                case GAME_END:
                    eventHandler.onEndGame(
                            Objects.requireNonNull(ServerMessage.fromString(raw_message))
                    );
                    break;
                default:
                    return;
            }
        } catch (RuntimeException | IOException e) {
            System.out.println("Could not resolve message: " + raw_message.trim());
            return;
        }
    }

    public void sendRegister(String clientName) {
        eventHandler.sendRegisterRequest(clientName);
    }

    public void sendGuess(char guessChar, String guessWord) {
        eventHandler.sendGuessRequest(guessChar, guessWord);
    }
}
