package org.example.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {

    // ENCODE DELIMITER
    public static final String DELIMITER = "\r\n";
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    // SEVER CONSTANTS
    public static final int SEVER_PORT = 7777;
    public static final String SERVER_IP = "localhost";

    private Constants() {}
}