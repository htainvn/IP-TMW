package org.example.client;

public interface IClient {
    void init();
    void start() throws Exception;
    void stop() throws Exception;
    void onMessage() throws Exception;
    void onError(Exception e);
}
