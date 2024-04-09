package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Player {
    public int rank;
    public String username;
    public int point;

    public Player(int rank, String username, int point) {
        this.rank = rank;
        this.username = username;
        this.point = point;
    }
}