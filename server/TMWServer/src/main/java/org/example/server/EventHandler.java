package org.example.server;

import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
  private GameController gameController;
  ScheduledFuture<?> f;

  @Autowired
  public EventHandler(ClientContact clientContact, ScoreBoard scoreBoard) {
    this.clientContact = clientContact;
    this.scoreBoard = scoreBoard;
  }

  @Override
  public @NotNull ServerMessage onRegistrationRequest (
      @NotNull SocketChannel client,
      @NotNull RegisterRequestMessage msg
  ) {
    ServerMessage response;
    if (gameController.isGamePlayable()) {
      response = ServerMessage.builder()
          .messageHeader(ServerInfo.CONNECTION_REJECTED)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_ERROR)
          .optionalMessageBody("Game is already started")
          .build();
      return response;
    }
    UserRegistrationStatus status = clientContact.addClient(client, msg.getName());
    if (status.equals(UserRegistrationStatus.ACCEPTED)) {
      response = ServerMessage.builder()
          .messageHeader(ServerInfo.CONNECTION_ACCEPTED)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_OK)
          .optionalMessageBody("Registration Completed Successfully")
          .build();
      scoreBoard.onNewPlayer(msg.getName());
      gameController.setNumPlayer(gameController.getNumPlayer() + 1);
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
              + "Your name should be composed by alphanumeric characters or '_'";
          break;
        case REJECTED:
          messageBody = "The game is full. Please try again later.";
          break;
        case ALREADY_REGISTERED:
          messageBody = "You are already registered";
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
//    if (!Objects.equals(msg.getGameid(), gameController.getGameid())) {
//      return ServerMessage.builder()
//          .messageHeader(ServerInfo.GUESS_REJECTED)
//          .fromHost(ServerInfo.SERVER_HOST)
//          .fromPort(ServerInfo.SERVER_PORT)
//          .status(ServerInfo.STATUS_ERROR)
//          .optionalMessageBody("Invalid game id")
//          .build();
//    }
    System.out.println("Expected client: " + clientContact.getExpectedClient() + " Current client: " + clientContact.getIndex(client));
    if (!Objects.equals(
        msg.getName(),
        clientContact.getName(
            clientContact.get(
                clientContact.getExpectedClient()
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
    System.out.println("Guessing: " + msg.getGuess_char());
    if (!gameController.isPlayersAllowedToGuess()) {
      return ServerMessage.builder()
              .messageHeader(ServerInfo.GUESS_REJECTED)
              .fromHost(ServerInfo.SERVER_HOST)
              .fromPort(ServerInfo.SERVER_PORT)
              .status(ServerInfo.STATUS_ERROR)
              .optionalMessageBody("You are not allowed to guess now.")
              .build();
    }

    if (gameController.getRound() >= 2 && !msg.getGuess_word().isEmpty()) {
      Boolean isCorrect = gameController.guessKeyword(msg.getGuess_word());
      if (isCorrect) {
        scoreBoard.updateScore(
            msg.getName(),
            5
        );
        gameController.setCharacterRevealedMode(true);
        return ServerMessage.builder()
            .messageHeader(ServerInfo.WINNER_ANNOUNCE)
            .fromHost(ServerInfo.SERVER_HOST)
            .fromPort(ServerInfo.SERVER_PORT)
            .status(ServerInfo.STATUS_OK)
            .optionalMessageBody("Congratulations! You have guessed the keyword!")
            .build();
      }
      else {
        clientContact.disqualify(client);
        gameController.setCharacterRevealedMode(false);
        return ServerMessage.builder()
            .messageHeader(ServerInfo.DISQUALIFY_ANNOUNCE)
            .fromHost(ServerInfo.SERVER_HOST)
            .fromPort(ServerInfo.SERVER_PORT)
            .status(ServerInfo.STATUS_ERROR)
            .optionalMessageBody("Oops! You have been disqualified for guessing the wrong keyword.")
            .build();
      }
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
  public void onNewIteration() {
    f = null;
    gameController.nextIteration();
    clientContact.setExpectedClient(gameController.getIteration());
    Integer maxIteration = clientContact.getNumberOfClients();
    if (gameController.getPlayerChanged()) {
      gameController.setRound(gameController.getRound() + 1);
    }
    System.out.println("Current round: " + gameController.getRound());
    if (gameController.ifGameEnds()) {
      this.onGameEnd();
      return;
    }
    if (gameController.getRound() != ServerInfo.STARTING_ITERATION) {
      ServerMessage msg = this.onResultPublished(false);
      MessageSender.broadcast(clientContact.getCatalog(), msg);
    }
    ScheduledThreadPoolExecutor executor1 = new ScheduledThreadPoolExecutor(1);
    executor1.schedule(() -> {
      ServerMessage notif;
      notif = ServerMessage.builder()
          .messageHeader(ServerInfo.YOUR_TURN)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_OK)
          .optionalMessageBody("Time: 30 seconds")
          .build();
      int maxTry = 0;
      SocketChannel nonDisqualifiedClient = clientContact.get(gameController.getIteration());
      while (clientContact.isDisqualified(nonDisqualifiedClient) && maxTry < maxIteration) {
        gameController.nextIteration();
        nonDisqualifiedClient = clientContact.get(gameController.getIteration());
        clientContact.setExpectedClient(gameController.getIteration());
        maxTry++;
      }
      if (maxTry >= maxIteration) {
        this.onGameEnd();
        return;
      }
      gameController.setGuessMode(true);
      String gameState = gameController.getGameState();
      ServerMessage gameStateMessage = ServerMessage.builder()
          .messageHeader(ServerInfo.GAME_STATE)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_OK)
          .optionalMessageBody(gameState)
          .build();
      MessageSender.broadcast(clientContact.getCatalog(), gameStateMessage);
      MessageSender.send(
          nonDisqualifiedClient,
          notif
      );
    }, 2, TimeUnit.SECONDS);
    ScheduledThreadPoolExecutor executor2 = new ScheduledThreadPoolExecutor(1);
    f =  executor2.schedule(() -> {
      gameController.setGuessMode(false);
      this.onNewIteration();
    }, 32, TimeUnit.SECONDS);

//      new Thread(() -> { // Wait 2s to send the statement
//        try {
//          Thread.sleep(2000);
//        } catch (InterruptedException e) {
//          throw new RuntimeException(e);
//        }
////        gameController.startGuessingCountdown(60);
//        gameController.setGuessMode(true);
//        ServerMessage notif;
//        notif = ServerMessage.builder()
//            .messageHeader(ServerInfo.YOUR_TURN)
//            .fromHost(ServerInfo.SERVER_HOST)
//            .fromPort(ServerInfo.SERVER_PORT)
//            .status(ServerInfo.STATUS_OK)
//            .optionalMessageBody(gameController.getGameState())
//            .build();
//        MessageSender.send(
//            clientContact.get(gameController.getIteration()),
//            notif
//        );
//      }).start();
//      new Thread(() -> {
//        try {
//          Thread.sleep(60000);
//          gameController.setGuessMode(false);
//          this.onNewIteration();
//        } catch (InterruptedException e) {
//          throw new RuntimeException(e);
//        }
//      }).start();
  }

  @Override
  public ServerMessage onResultPublished(Boolean isRankingIncluded) {
    RankingAnnounceMessage msg = RankingAnnounceMessage.from(
        gameController.getGameid(),
        scoreBoard.getScores(),
        isRankingIncluded
    );
    return msg;
  }

  @Override
  public void onGameStart() {
    Integer numberOfClients = clientContact.getNumberOfClients();
    SocketChannel[] clientSockets = new SocketChannel[numberOfClients];
    String[] clientNames = new String[numberOfClients];
    ServerMessage[] messages = new ServerMessage[numberOfClients];
    for (int i = 0; i < numberOfClients; i++) {
      clientSockets[i] = clientContact.get(i);
      clientNames[i] = clientContact.getName(clientSockets[i]);
    }

    for (int i = 0; i < numberOfClients; i++) {
      messages[i] = ServerMessage.builder()
          .messageHeader(ServerInfo.GAME_START)
          .fromHost(ServerInfo.SERVER_HOST)
          .fromPort(ServerInfo.SERVER_PORT)
          .status(ServerInfo.STATUS_OK)
          .optionalMessageBody(String.format(
              "GameID: %s\r\nName: %s\r\nOrder: %d\r\nClients: %s",
              gameController.getGameid(),
              clientNames[i],
              i,
              String.join(", ", clientNames)
          ))
          .build();
    }
    MessageSender.deliver(clientSockets, messages);
    gameController.start();
    this.onNewIteration();
  }

  @Override
  public void onGameEnd() {
    ServerMessage msg = this.onResultPublished(true);
    MessageSender.broadcast(clientContact.getCatalog(), msg);
    gameController.stopGame();
    ServerMessage bye_msg = ServerMessage.builder()
        .messageHeader(ServerInfo.GAME_END)
        .fromHost(ServerInfo.SERVER_HOST)
        .fromPort(ServerInfo.SERVER_PORT)
        .status(ServerInfo.STATUS_OK)
        .optionalMessageBody("Game has ended")
        .build();
    MessageSender.broadcast(clientContact.getCatalog(), bye_msg);
    clientContact.reset();
    scoreBoard.reset();
    gameController.newGame();
  }

  public void onGuessBeforeTimeUp() {
    if (f != null) f.cancel(true);
    gameController.setGuessMode(false);
    this.onNewIteration();
  }
}
