package org.example.models;

import javax.validation.constraints.NotNull;
import javax.ws.rs.client.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.util.ClientInfo;
import org.example.util.Constants;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestMessage extends Message {

  private String name;

  public RegisterRequestMessage(String toHost, Integer toPort, String name) {
    super(Constants.CLIENT_REGISTER, toHost, toPort);
    this.name = name;
  }

  public static @Nullable RegisterRequestMessage fromString(@NotNull String message) {
    RegisterRequestMessage msg = new RegisterRequestMessage();
    try {
      String[] lines = message.split(Constants.DELIMITER);
      if (lines.length != ClientInfo.REGISTER_LENGTH) {
        throw new Exception("Invalid message format for a register request: Expected 4 lines");
      }
      msg.messageHeader = lines[0].split(":")[1].trim();
      if (!msg.messageHeader.equals(ClientInfo.REGISTER)) {
        throw new Exception("Invalid message header: Not a register request");
      }
      msg.toHost = lines[1].split(":")[1].trim();
      msg.toPort = Integer.parseInt(lines[2].split(":")[1].trim());
      msg.name = lines[3].split(":")[1].trim();
      return msg;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
}