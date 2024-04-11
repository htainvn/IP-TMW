package org.example.models;

import lombok.Getter;
import lombok.Setter;

public class Player {
    @Getter
    @Setter
    public int rank;
    @Getter
    public String username;
    @Getter
    public int point;

    public Player(int rank, String username, int point) {
        this.rank = rank;
        this.username = username;
        this.point = point;
    }

}