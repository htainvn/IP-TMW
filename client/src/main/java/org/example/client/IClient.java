package org.example.client;

import java.io.IOException;

public interface IClient {
    void init() throws IOException;
    void stop() throws Exception;
    void onMessage() throws Exception;
    void onError(Exception e);
}
