package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Registration {
    public int id;
    public String username;

    public Registration(int id, String username) {
        this.id = id;
        this.username = username;
    }
}