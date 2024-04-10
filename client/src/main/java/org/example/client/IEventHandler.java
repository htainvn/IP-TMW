package org.example.client;

import org.example.models.ServerMessage;

import javax.validation.constraints.NotNull;

public interface IEventHandler {
    void onConnectionRespond (@NotNull ServerMessage msg);

    void onGuessRespond(@NotNull ServerMessage msg);

    void onStartGame(@NotNull ServerMessage msg);

    void onEndGame(@NotNull ServerMessage msg);

    void onRankingAnnounce(@NotNull ServerMessage msg);
}
