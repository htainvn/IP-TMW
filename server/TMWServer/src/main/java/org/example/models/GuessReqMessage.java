package org.example.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.util.ClientInfo;
import org.example.util.Constants;
import org.jetbrains.annotations.Nullable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuessReqMessage extends Message {

  private String name;
  private String gameid;
  private Character guess_char;
  private String guess_word;

  public GuessReqMessage (
      @NotNull String toHost,
      @NotNull Integer toPort,
      @NotNull String gameid,
      @NotNull String name,
      @Nullable Character guess_char,
      @Nullable String guess_word
  )
  {
    super(Constants.KEYWORD_GUESS_REQ, toHost, toPort);
    this.gameid = gameid;
    this.name = name;
    this.guess_char = guess_char;
    this.guess_word = guess_word;
  }

  public static @Nullable GuessReqMessage fromString(@NotNull String message) {
    GuessReqMessage msg = new GuessReqMessage();
    try {
      String[] lines = message.split(Constants.DELIMITER);
      if (lines.length != ClientInfo.GUESS_LENGTH) {
        throw new Exception("Invalid message format for a keyword guess request: Expected 8 lines");
      }
      msg.messageHeader = lines[0].split(":")[1].trim();
      if (!msg.messageHeader.equals(ClientInfo.GUESS)) {
        throw new Exception("Invalid message header: Not a keyword guess request");
      }
      msg.toHost = lines[1].split(":")[1].trim();
      msg.toPort = Integer.parseInt(lines[2].split(":")[1].trim());
      msg.gameid = lines[3].split(":")[1].trim();
      msg.name = lines[4].split(":")[1].trim();
      try {
        msg.guess_char = lines[5].split(":")[1].trim().charAt(0);
      }
      catch (Exception e) {
        throw new Exception("Invalid character guess: " + e.getMessage());
      }
      try {
        msg.guess_word = lines[6].split(":")[1].trim();
      }
      catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("No word guess");
        msg.guess_word = "";
      }
      return msg;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
}
