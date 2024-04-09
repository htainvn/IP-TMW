package org.example.server;

import com.sun.tools.javac.util.Pair;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.example.models.GuessReqMessage;
import org.example.models.RankingAnnounceMessage;
import org.example.models.RegisterRequestMessage;
import org.example.models.ServerMessage;
import org.example.util.ServerInfo;
import org.example.util.ServerInfo.UserRegistrationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventHandler implements IEventHandler {

  @Autowired
  private ClientContact clientContact;

  @Autowired
  private ScoreBoard scoreBoard;

  @Autowired
  public EventHandler(ClientContact clientContact, ScoreBoard scoreBoard) {
    this.clientContact = clientContact;
    this.scoreBoard = scoreBoard;
  }

  @Autowired
  private GameController gameController;

  @Override
  public @NotNull ServerMessage onRegistrationRequest(
      @NotNull SocketChannel client,
      @NotNull RegisterRequestMessage msg
  ) {
    UserRegistrationStatus status = clientContact.addClient(client, msg.getName());
    ServerMessage response;
    if (status.equals(UserRegistrationStatus.ACCEPTED)) {
      response = ServerMessage.builder()
          .messageHeader(ServerInfo.CONNECTION_ACCEPTED)
          .status(ServerInfo.STATUS_OK)
          .optionalMessageBody("Registration Completed Successfully")
          .build();
    }
    else {
      String messageBody;
      switch (status) {
        case NAME_DUPLICATED:
          messageBody = "Name already exists";
          break;
        case NAME_TOO_LONG:
          messageBody = "Name is too long";
          break;
        case INVALID_NAME:
          messageBody = "Invalid name. "
              + "Your name should be composed by alphanumeric characters or '_'"
              + " and the length is not longer than 10 characters";
          break;
        case REJECTED:
          messageBody = "The game is full. Please try again later.";
          break;
        default:
          messageBody = "Unknown error";
          break;
      }
      response = ServerMessage.builder()
          .messageHeader(ServerInfo.CONNECTION_REJECTED)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_ERROR)
          .optionalMessageBody(messageBody)
          .build();
    }
    return response;
  }

  @Override
  public ServerMessage onGuessingRequest(
      @NotNull SocketChannel client,
      @NotNull GuessReqMessage msg)
  {
    if (Objects.equals(clientContact.getIndex(client), ServerInfo.PLAYER_NOT_FOUND)) {
      return ServerMessage.builder()
          .messageHeader(ServerInfo.GUESS_REJECTED)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_ERROR)
          .optionalMessageBody("You are not registered")
          .build();
    }
    if (!Objects.equals(msg.getGameid(), gameController.getGameid())) {
      return ServerMessage.builder()
          .messageHeader(ServerInfo.GUESS_REJECTED)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_ERROR)
          .optionalMessageBody("Invalid game id")
          .build();
    }
    if (!Objects.equals(
        msg.getName(),
        clientContact.getName(
            clientContact.get(
                gameController.getIteration()
            )
        )
    )) {
      return ServerMessage.builder()
          .messageHeader(ServerInfo.GUESS_REJECTED)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_ERROR)
          .optionalMessageBody("You are not yet allowed to guess")
          .build();
    }
    Boolean isCorrect = gameController.guessCharacter(msg.getGuess_char());
    if (isCorrect) {
      scoreBoard.updateScore(
          msg.getName(),
          1
      );
      gameController.setCharacterRevealedMode(true);
      return ServerMessage.builder()
          .messageHeader(ServerInfo.GUESS_ACCEPTED)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_OK)
          .optionalMessageBody("Correct guess")
          .build();
    }
    else {
      gameController.setCharacterRevealedMode(false);
      return ServerMessage.builder()
          .messageHeader(ServerInfo.GUESS_ACCEPTED)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_ERROR)
          .optionalMessageBody("Incorrect guess")
          .build();
    }
  }

  @Override
  public ServerMessage onNewIteration() {
    gameController.nextIteration();
    gameController.startGuessingCountdown(20);
    ServerMessage notif;
    notif = ServerMessage.builder()
        .messageHeader(ServerInfo.YOUR_TURN)
        .fromHost(ServerInfo.SERVER_HOST)
        .fromPort(ServerInfo.SERVER_PORT)
        .status(ServerInfo.STATUS_OK)
        .optionalMessageBody("It's your turn to guess.")
        .build();
    return notif;
  }

  @Override
  public ServerMessage onResultPublished(Boolean isRankingIncluded) {
    RankingAnnounceMessage msg = RankingAnnounceMessage.from(
        gameController.getGameid(),
        scoreBoard.getScores(),
        isRankingIncluded
    );
    gameController.reset();
    return msg;
  }

}
