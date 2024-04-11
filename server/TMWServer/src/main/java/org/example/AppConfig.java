package org.example;

import javax.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.example.gui.GUIManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.example.server.Server;

@Configuration
@NoArgsConstructor
@ComponentScan("org.example")
public class AppConfig {

    @Autowired
    public AppConfig(
        GUIManager guiManager,
        Server server
    ) {
        server.init();
    }
}
