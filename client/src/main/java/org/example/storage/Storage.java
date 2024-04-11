package org.example.storage;

import org.javatuples.Pair;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Vector;

@Getter
@Setter
@Component
public class Storage {
    private String gameID;
    private String clientName;
    private Integer gameOrder;

    private String hint;
    private String keyword;

    private Vector<Pair<String, Integer>> scores;
}