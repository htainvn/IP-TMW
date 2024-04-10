package org.example.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.javatuples.Pair;
import org.example.util.Constants;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Vector;

public class RankingAnnounce extends ServerMessage {

    private JsonObject jsonObject;

    public static @Nullable RankingAnnounce fromString(@NotNull String message) {

        RankingAnnounce msg = new RankingAnnounce();

        try {
            String[] lines = message.split(Constants.DELIMITER);

            msg.messageHeader = lines[0].split(":")[1].trim();
            msg.fromHost = lines[1].split(":")[1].trim();
            msg.fromPort = Integer.parseInt(lines[2].split(":")[1].trim());
            msg.status = lines[3].split(":")[1].trim();

            int index = lines[4].indexOf(":");
            if (index != -1) {
                msg.optionalMessageBody = lines[4].substring(index + 1).trim();
            }

            return msg;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void convertToJson() {
        Gson gson = new Gson();
        jsonObject = gson.fromJson(optionalMessageBody, JsonObject.class);
    }

    private void validate() {
        if(optionalMessageBody == null) return;
        if(jsonObject == null) convertToJson();
    }

    public String getGameID() {
        validate();
        if(jsonObject == null) return null;
        return jsonObject.get("gameid").getAsString();
    }

    public Vector<Pair<String, Integer>> getPlayers() {
        validate();
        if(jsonObject == null) return null;

        JsonObject scores = jsonObject.getAsJsonObject("scores");
        System.out.println(scores);

        Vector<Pair<String, Integer>> scoreVector = new Vector<>();

        for (String player : scores.keySet()) {
            int score = scores.get(player).getAsJsonObject().get("score").getAsInt();
            scoreVector.add(new Pair<>(player, score));
        }

        return scoreVector;
    }
}
