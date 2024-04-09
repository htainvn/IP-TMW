package org.example.util;

import java.nio.charset.Charset;

public class Constants {

    // ENCODE DELIMITER
    public static final String DELIMITER = "\r\n";
    public static final Charset CHARSET = Charset.forName("UTF-8");

    // SEVER CONSTANTS
    public static final int SEVER_PORT = 7777;
    public static final String SERVER_IP = "127.0.0.1";

    // GAME PLAY CONSTANTS
    public static final int MAX_CLIENT_CONNECTIONS = 10;

    // REGISTER NAME REGEX
    public static final String REGEX                =   "^[a-zA-Z0-9_]{1,10}$";

    // MESSAGE TYPE
    public static final String SEVER_ERROR          =   "SEVER_ERROR";

    public static final String CONNECTION_ACCEPT    =   "CONNECTION_ACCEPT";
    public static final String CONNECTION_REJECT    =   "CONNECTION_REJECT";

    public static final String SEVER_REGISTER       =   "SEVER_REGISTER";
    public static final String CLIENT_REGISTER      =   "CLIENT_REGISTER";
    public static final String REGISTER_ERROR       =   "REGISTER_ERROR";
    public static final String REGISTER_SUCCESS     =   "REGISTER_SUCCESS";

    public static final String GUESS_RESPONSE       =   "SEVER_GUESS";
    public static final String GUESS_REQUEST        =   "CLIENT_GUESS";

    public static final String GUESS_CORRECT        =   "GUESS_CORRECT";
    public static final String GUESS_WRONG          =   "GUESS_WRONG";
    public static final String GUESS_ERROR          =   "GUESS_ERROR";
    public static final String GUESS_DENY           =   "GUESS_DENY";
    public static final String GUESS_ABORTED        =   "GUESS_ABORTED";

    public static final String KEYWORD_GUESS_REQ    =   "KEYWORD_GUESS_REQ";

    public static final String KEYWORD_GUESS_RES    =   "KEYWORD_GUESS_RES";

    // GAMEPLAY STATUS
    public static final String GAME_UPDATE          =   "GAME_UPDATE";
    public static final String GAME_FINISHED        =   "GAME_FINISHED";

    // GAME RESPONSE TYPE
    public static final int NOT_YOUR_TURN           =   1;
    public static final int WRONG_GUESS             =   2;

    private Constants() {}
}
