package org.example.util;

import org.example.models.GuessReqMessage;
import org.example.models.Message;
import org.example.models.RegisterRequestMessage;
import org.example.util.ServerInfo.MessageType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

public class Decoder {

  public static MessageType decode(@NotNull String message) {
    /*
    Message format:
    - Type of message (REGISTER, GUESS, etc.)
    - Host: the host of the game
    - Port: the port of the game
    --- If it is a register message ---
    - Name: the username of the player
    --- If it is a character-guess message ---
    - Name: the username of the player
    - Guess: the character guessed by the player
    --- If it is a word-guess message ---
    - Name: the username of the player
    - Guess: the word guessed by the player
     */
    System.out.println(message);
    String[] lines = message.split(Constants.DELIMITER);
    try {
      String messageType = lines[0].split(":")[1].trim();
      if (messageType.equals(ClientInfo.REGISTER)) {
        return MessageType.REGISTER;
      } else if (messageType.equals(ClientInfo.GUESS)) {
        return MessageType.GUESS;
      } else {
        return null;
      }
    }
    catch (Exception e) {
      return null;
    }
  }
}