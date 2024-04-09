package org.example;

import lombok.NoArgsConstructor;
import org.example.server.EventHandler;
import org.example.server.GameController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.example.server.Server;

@Configuration
@NoArgsConstructor
@ComponentScan("org.example")
public class AppConfig {

    @Autowired
    public AppConfig(Server server) {
        System.out.println("AppConfig created");
    }
}
