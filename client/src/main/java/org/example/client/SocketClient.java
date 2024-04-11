package org.example.client;

import org.example.models.GameInfoMessage;
import org.example.models.RankingAnnounce;
import org.example.models.ServerMessage;
import org.example.util.Decoder;
import org.example.util.ServerInfo.MessageType;
import lombok.NoArgsConstructor;
import org.example.util.Validator;
import org.springframework.core.annotation.Order;
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
@Order(1)
public class SocketClient implements IClient {

    private InetSocketAddress serverAddress;// = new InetSocketAddress("localhost", Constants.SEVER_PORT);

    private Selector selector;
    public static SocketChannel client;
    private Boolean stillConnected = false;

    private EventHandler eventHandler;
    private Validator validator;

    @Autowired
    public SocketClient(EventHandler eventHandler, Validator validator) {
        this.eventHandler = eventHandler;
        this.validator = validator;
    }

    @Override
    public void init() throws IOException {
        System.out.println("Initializing SocketClient");
        selector = Selector.open();
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
    public void connect(String dest, String port) throws IOException {
        serverAddress = new InetSocketAddress(dest, Integer.parseInt(port));
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
        validator.setupValidator(dest, Integer.valueOf(port));
    }

    @Override
    public void stop() throws IOException {
        System.out.println("GUI stopped");
        validator.resetValidator();
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
            if(!validator.validate(raw_message)) {return;}

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

    public Boolean isConnected() {
        return stillConnected;
    }
}
