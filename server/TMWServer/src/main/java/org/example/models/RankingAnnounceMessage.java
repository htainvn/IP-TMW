package org.example.models;

import com.sun.tools.javac.util.Pair;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import lombok.Builder;
import lombok.Value;
import org.example.util.ServerInfo;

@Builder
public class RankingAnnounceMessage extends ServerMessage {

  /// @documentation
  // Write the scores in JSON format
    /*
    {
      gameid: 1234,
      scores: {
        "player1_name": {
          "rank": 1, (only if isRankingIncluded is true)
          "score": 100
        },
        "player2_name": {
          "rank": 2, (only if isRankingIncluded is true)
          "score": 90
        },
        ...
      },
     */

  private static String convertToJSONString(
      @NotNull String gameId,
      @NotNull ArrayList<Pair<String, Integer>> scores,
      Boolean isRankingIncluded
  ) {
    StringBuilder messageBody = new StringBuilder();
    messageBody.append("{\n");
    messageBody.append("  \"gameid\": ").append(gameId).append(",\n");
    messageBody.append("  \"scores\": {\n");
    for (int i = 0; i < scores.size(); i++) {
      Pair<String, Integer> score = scores.get(i);
      messageBody.append("    \"").append(score.fst).append("\": {\n");
      if (isRankingIncluded) {
        messageBody.append("      \"rank\": ").append(i + 1).append(",\n");
      }
      messageBody.append("      \"score\": ").append(score.snd).append("\n");
      messageBody.append("    }");
      if (i < scores.size() - 1) {
        messageBody.append(",");
      }
      messageBody.append("\n");
    }
    messageBody.append("  }\n");
    messageBody.append("}");
    return messageBody.toString();
  }

  public static RankingAnnounceMessage from(
      @NotNull String gameId,
      @NotNull Dictionary<String, Integer> scores,
      Boolean isRankingIncluded
  ) {
    ArrayList<Pair<String, Integer>> scores_array = new ArrayList<>();
    Enumeration<String> keys = scores.keys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      scores_array.add(new Pair<>(key, scores.get(key)));
    }
    scores_array.sort((o1, o2) -> o2.snd - o1.snd);
    return (RankingAnnounceMessage) ServerMessage.builder()
        .messageHeader(ServerInfo.RANKING_ANNOUNCE)
        .fromHost(ServerInfo.SERVER_HOST)
        .fromPort(ServerInfo.SERVER_PORT)
        .status(ServerInfo.STATUS_OK)
        .optionalMessageBody(convertToJSONString(gameId, scores_array, isRankingIncluded))
        .build();
  }
}