package org.example.client;

import lombok.NoArgsConstructor;
import org.example.util.Constants;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

@Component
@NoArgsConstructor
public class SocketClient {

    private Selector selector;
    private SocketChannel client;

//    private EventHandler

    public void stop() throws IOException {
        client.close();
    }

}
