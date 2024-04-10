package org.example.client;

import org.example.models.Message;
import org.example.models.ServerMessage;
import org.example.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MessageSender {
    public static void send(@NotNull SocketChannel client, @NotNull Message msg) throws RuntimeException {
        try {
            byte[] messageBytes = msg.toString().getBytes(Constants.CHARSET);
            ByteBuffer encode = ByteBuffer.wrap(messageBytes);
            client.write(encode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
