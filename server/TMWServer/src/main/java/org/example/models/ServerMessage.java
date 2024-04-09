package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.util.Constants;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerMessage {
  protected String messageHeader;
  protected String fromHost;
  protected Integer fromPort;
  protected String status;
  protected String optionalMessageBody;

  public String toString() {
    return "Type: " + messageHeader
        + Constants.DELIMITER
        + "From: " + fromHost
        + Constants.DELIMITER
        + "Port: " + fromPort
        + Constants.DELIMITER
        + "Status: " + status
        + Constants.DELIMITER
        + "Detail: " + optionalMessageBody;
  }
}