package org.example.client;

import org.example.models.GameInfoMessage;
import org.example.models.RankingAnnounce;
import org.example.models.ServerMessage;

import javax.validation.constraints.NotNull;

public interface IEventHandler {
    void onConnectionRespond (@NotNull ServerMessage msg);

    void onGuessRespond(@NotNull ServerMessage msg);

    void onStartGame(@NotNull GameInfoMessage msg);

    void onEndGame(@NotNull ServerMessage msg);

    void onRankingAnnounce(@NotNull RankingAnnounce msg);

    void onTurn();
}
