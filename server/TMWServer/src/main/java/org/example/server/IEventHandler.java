package org.example.server;

import java.nio.channels.SocketChannel;
import javax.validation.constraints.NotNull;
import org.example.models.GuessReqMessage;
import org.example.models.RegisterRequestMessage;
import org.example.models.ServerMessage;

public interface IEventHandler {
  ServerMessage onRegistrationRequest(@NotNull SocketChannel client, @NotNull RegisterRequestMessage msg);
  ServerMessage onGuessingRequest(@NotNull SocketChannel client, @NotNull GuessReqMessage msg);
  ServerMessage onNewIteration();
  ServerMessage onResultPublished(Boolean isRankingIncluded);
}
