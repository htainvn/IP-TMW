package org.example.gui;

import org.example.observer.GameObserver;
import org.example.observer.ConnectingPhase;
import org.example.observer.UIObserver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ejb.DependsOn;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;

import org.example.models.Player;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@DependsOn("socketClient")
public class GUIManager {
    @Autowired
    private UIObserver uiObserver;
    @Autowired
    private GameObserver gameObserver;
    private Player[] players;
    @Autowired
    private FinalStanding finalStanding;
    @Autowired
    private InGame inGame;
    @Autowired
    private Registration registration;
    private Object currentPhase;

    @Autowired
    public GUIManager(Registration registration, InGame inGame, FinalStanding finalStanding) {
        this.registration = registration;
        this.inGame = inGame;
        this.finalStanding = finalStanding;

        initFont();
        currentPhase = registration;
        gameRegistration();
        this.update();
    }

    private void initFont() {
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        final GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final String AVAILABLE_FONT_FAMILY_NAMES[] = GE.getAvailableFontFamilyNames();
        try {
            final File LIST[] = {
                    new File(root.toString() + "/src/main/resources/fonts/SF-Pro-Display-Regular.otf"),
                    new File(root.toString() + "/src/main/resources/fonts/SF-Pro-Display-Bold.otf"),
                    new File(root.toString() + "/src/main/resources/fonts/SF-Pro-Display-Heavy.otf"),
                    new File(root.toString() + "/src/main/resources/fonts/SF-Pro-Display-Light.otf"),
                    new File(root.toString() + "/src/main/resources/fonts/SF-Pro-Display-Medium.otf"),
                    new File(root.toString() + "/src/main/resources/fonts/SF-Pro-Display-Semibold.otf"),
                    new File(root.toString() + "/src/main/resources/fonts/SF-Pro-Display-Thin.otf"),
                    new File(root.toString() + "/src/main/resources/fonts/SF-Pro-Display-Ultralight.otf"),
            };
            for (File LIST_ITEM : LIST) {
                if (LIST_ITEM.exists()) {
                    Font FONT = Font.createFont(Font.TRUETYPE_FONT, LIST_ITEM);
                    System.out.println(FONT.getFontName());
                    if (!Arrays.toString(AVAILABLE_FONT_FAMILY_NAMES).contains(FONT.getFontName())) {
                        GE.registerFont(FONT);
                    }
                }
            }
        } catch (FontFormatException | IOException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    public void gameStart() {
        ((JFrame) currentPhase).setVisible(false);
        currentPhase = inGame;
        inGame.setVisible(true);
    }

    public void gameFinished() {
        ((JFrame) currentPhase).setVisible(false);
        currentPhase = finalStanding;
        finalStanding.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(finalStanding.getWaitingTime());
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    gameRegistration();
                });
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void gameRegistration() {
        ((JFrame) currentPhase).setVisible(false);
        currentPhase = registration;
        registration.setVisible(true);
    }

    public void update() {
        new Thread(() -> {
            Integer time = 0;
            while (true) {
                try {
                    Thread.sleep(100);
                    if (uiObserver.getCurrentPhase().equals(ConnectingPhase.REGISTRATION)) {
                        gameRegistration();
                        uiObserver.setCurrentPhase(ConnectingPhase.NONE);
                    } else if (uiObserver.getCurrentPhase().equals(ConnectingPhase.IN_GAME)) {
                        gameStart();
                        uiObserver.setCurrentPhase(ConnectingPhase.NONE);
                    } else if (uiObserver.getCurrentPhase().equals(ConnectingPhase.GAME_FINISHED)) {
                        gameFinished();
                        uiObserver.setCurrentPhase(ConnectingPhase.NONE);
                    }

                    if (currentPhase instanceof FinalStanding) {
                        time += 100;
                    } else {
                        time = 0;
                    }

                    Integer finalTime = time;
                    javax.swing.SwingUtilities.invokeAndWait(() -> {
                        if (currentPhase instanceof InGame) {
                            ((InGame) currentPhase).update();
                        } else if (currentPhase instanceof FinalStanding) {
                            ((FinalStanding) currentPhase).update(finalTime);
                        } else {
                            ((Registration) currentPhase).update();
                        }

                    });
                } catch (InterruptedException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
