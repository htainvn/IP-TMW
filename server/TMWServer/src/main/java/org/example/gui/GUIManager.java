package org.example.gui;

import org.example.observer.GameObserver;
import org.example.observer.UIObserver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private Lobby lobby;
    private Object currentPhase;

    @Autowired
    public GUIManager(Lobby lobby, InGame inGame, FinalStanding finalStanding) {
        this.lobby = lobby;
        this.inGame = inGame;
        this.finalStanding = finalStanding;

        initFont();
        currentPhase = lobby;
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
        currentPhase = lobby;
        lobby.setVisible(true);
    }

    public void update() {
        new Thread(() -> {
            Integer time = 0;
            while (true) {
                try {
                    Thread.sleep(100);
                    switch (uiObserver.getPhase()) {
                        case 1:
                            gameRegistration();
                            uiObserver.setPhase(-1);
                            break;
                        case 2:
                            gameStart();
                            uiObserver.setPhase(-1);
                            break;
                        case 3:
                            gameFinished();
                            uiObserver.setPhase(-1);
                            break;
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
                            ((Lobby) currentPhase).update();
                        }

                    });
                } catch (InterruptedException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
