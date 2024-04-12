package org.example.gui;

import org.example.observer.GameObserver;
import org.example.observer.Phase;
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
    @Autowired
    private FinalStanding finalStanding;
    @Autowired
    private InGame inGame;
    @Autowired
    private Registration registration;
    @Autowired
    private Lobby lobby;
    private Object currentPhase;

    @Autowired
    public GUIManager(Registration registration, InGame inGame, FinalStanding finalStanding, Lobby lobby, UIObserver uiObserver, GameObserver gameObserver) {
        this.registration = registration;
        this.inGame = inGame;
        this.finalStanding = finalStanding;
        this.lobby = lobby;

        this.uiObserver = uiObserver;
        this.gameObserver = gameObserver;

        initFont();
        currentPhase = registration;
//        uiObserver.setCurrentPhase(Phase.IN_GAME);
//        gameStart();
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
        inGame.initData();
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

    public void gameLobby() {
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
                    if (uiObserver.getCurrentPhase().equals(Phase.REGISTRATION)) {
                        gameRegistration();
                        uiObserver.setCurrentPhase(Phase.NONE);
                    } else if (uiObserver.getCurrentPhase().equals(Phase.IN_GAME)) {
                        gameStart();
                        uiObserver.setCurrentPhase(Phase.NONE);
                    } else if (uiObserver.getCurrentPhase().equals(Phase.GAME_FINISHED)) {
                        gameFinished();
                        uiObserver.setCurrentPhase(Phase.NONE);
                    } else if (uiObserver.getCurrentPhase().equals(Phase.LOBBY)) {
                        gameLobby();
                        uiObserver.setCurrentPhase(Phase.NONE);
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
                        } else if (currentPhase instanceof Registration) {
                            ((Registration) currentPhase).update();
                        } else if (currentPhase instanceof Lobby) {
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
