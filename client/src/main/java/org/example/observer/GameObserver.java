package org.example.observer;

import org.example.client.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
public class GameObserver {
    EventHandler eventHandler;
    @Autowired
    public GameObserver(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        System.out.println("GameObserver created");
    }
}
