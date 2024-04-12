package org.example.util;

public class ServerInfo {
    public static final String DELIMITER = "\r\n";

    public static final String RANKING_ANNOUNCE = "RANKING_ANNOUNCE";
    public static final String YOUR_TURN = "YOUR_TURN";

    public static final String GAME_START = "GAME_START";
    public static final String GAME_STATE = "GAME_STATE";
    public static final String GAME_END = "GAME_END";

    public static final String CONNECTION_REJECTED = "REGISTERED_REJECTED";
    public static final String CONNECTION_ACCEPTED = "REGISTERED_ACCEPTED";

    public static final String GUESS_ACCEPTED = "GUESS_ACCEPTED";
    public static final String GUESS_REJECTED = "GUESS_REJECTED";

    public static final String STATUS_OK = "OK";
    public static final String STATUS_ERROR = "ERROR";

    public static final String WRONG_FORMAT = "WRONG_FORMAT";
    public static final String GAME_ALREADY_STARTED = "GAME_ALREADY_STARTED";

    public static final String WINNER_ANNOUNCE = "WINNER_ANNOUNCE";
    public static final String DISQUALIFY_ANNOUNCE = "DISQUALIFY_ANNOUNCE";

    public static final String DISCONNECTED = "DISCONNECTED";

    public static final Integer SERVER_MESSAGE_LENGTH = 6;

    public static final Integer MY_TURN_MESSAGE_LENGTH = 4;

    public enum MessageType {
        CONNECTION,
        GUESS,
        GAME_START,
        GAME_STATE,
        YOUR_TURN,
        GAME_END,
        WINNER_ANNOUNCE,
        DISQUALIFY_ANNOUNCE,
        RANKING,
        DISCONNECTED,
        REGISTERED_REJECTED,
    }

    private ServerInfo() {}
}
