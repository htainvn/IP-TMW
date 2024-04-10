package org.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.example")
public class AppConfig {

    public AppConfig() {
        System.out.println("AppConfig created");
    }
}