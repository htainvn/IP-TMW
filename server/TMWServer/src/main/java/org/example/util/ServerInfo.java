package org.example.util;

public class ServerInfo {
  public static final String DELIMITER = "\r\n";
  public static final String RANKING_ANNOUNCE = "RANKING_ANNOUNCE";
  public static final String YOUR_TURN = "YOUR_TURN";
  public static final String GAME_START = "GAME_START";
  public static final String GAME_END = "GAME_END";
  public static final String CONNECTION_REJECTED = "REGISTERED_REJECTED";
  public static final String CONNECTION_ACCEPTED = "REGISTERED_ACCEPTED";
  public static final String GUESS_ACCEPTED = "GUESS_ACCEPTED";
  public static final String GUESS_REJECTED = "GUESS_REJECTED";
  public static final String WINNER_ANNOUNCE = "WINNER_ANNOUNCE";
  public static final String DISQUALIFY_ANNOUNCE = "DISQUALIFY_ANNOUNCE";
  public static final String GAME_STATE = "GAME_STATE";
  public static final String STATUS_OK = "OK";
  public static final String STATUS_ERROR = "ERROR";
  public static final Integer MAXIMUM_PLAYERS = 10;
  public static final Integer KEYWORD_GUESS_ALLOWANCE = 2;
  public static final Integer PLAYER_NOT_FOUND = -1;
  public static final String SERVER_HOST = "localhost";
  public static final Integer SERVER_PORT = 7777;

  public static final String WRONG_FORMAT = "WRONG_FORMAT";
  public static final String GAME_ALREADY_STARTED = "GAME_ALREADY_STARTED";
  public static final String DISCONNECTED = "DISCONNECTED";

  public static final int STARTING_ITERATION = 0;

  public enum UserStatus {
    REGISTERED,
    GUESSING,
    WAITING,
    GAME_OVER
  }

  public enum UserRegistrationStatus {
    ACCEPTED,
    REJECTED,
    NAME_DUPLICATED,
    INVALID_NAME,
    NAME_TOO_LONG,
    NAME_VALID,
    ALREADY_REGISTERED,
  }

  public enum MessageType {
    REGISTER,
    GUESS,
    DISCONNECT
  }
}