package org.example;

import lombok.NoArgsConstructor;
import org.example.client.SocketClient;
import org.example.gui.GUIManager;
import org.example.gui.InGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@NoArgsConstructor
@ComponentScan("org.example")
public class AppConfig {

    @Autowired
    public AppConfig(SocketClient socketClient, GUIManager guiManager)  {
        System.out.println("AppConfig created");
        try {
            socketClient.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}