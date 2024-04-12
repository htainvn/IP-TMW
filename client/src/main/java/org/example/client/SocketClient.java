package org.example.client;

import lombok.Getter;
import org.example.models.*;
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
    public static Boolean stillConnected;

    private EventHandler eventHandler;
    private Validator validator;

    @Autowired
    public SocketClient(EventHandler eventHandler, Validator validator) throws IOException {
        selector = Selector.open();
        stillConnected = false;
        this.eventHandler = eventHandler;
        this.validator = validator;
    }

    private void recall() throws IOException {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init();
    }

    @Override
    public void init() throws IOException {
        //System.out.println("Initializing SocketClient: " + stillConnected);
        if (!stillConnected) recall();

        while( stillConnected ) {

            if(selector.selectNow() == 0) recall();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext() && stillConnected) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) onMessage();
                iterator.remove();
            }

            if (!stillConnected) recall();
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
            System.out.println("Selector: " + selector);
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
            ByteBuffer buffer = ByteBuffer.allocate(8192);
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

        System.out.println("===================================");
        System.out.println("Type" + raw_message.trim());
        System.out.println("==================================");

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
                    eventHandler.onTurn(Objects.requireNonNull(MyTurnMessage.fromString(raw_message).getTime()));
                    break;
                case GAME_END:
                    eventHandler.onEndGame(
                            Objects.requireNonNull(ServerMessage.fromString(raw_message))
                    );
                    break;
                case WINNER_ANNOUNCE:
                    eventHandler.onWinnerAnnounce(
                            Objects.requireNonNull(ServerMessage.fromString(raw_message))
                    );
                    break;
                case DISQUALIFY_ANNOUNCE:
                    eventHandler.onDisqualifyAnnounce(
                            Objects.requireNonNull(ServerMessage.fromString(raw_message))
                    );
                    break;
                case GAME_STATE:
                    eventHandler.onGameState(
                            Objects.requireNonNull(GameStateMessage.fromString(raw_message))
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
//        eventHandler.sendRegisterRequest("abcd");
    }

    public Boolean isConnected() {
        return stillConnected;
    }
}
