package org.example;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.example.observer.UIObserver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import org.example.models.Player;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class gui extends JFrame {
    private JButton startButton;
    private JPanel globalPanel;
    private JButton endButton;
    private JTable playerTable;
    private Player[] players;

    private UIObserver uiObserver;

    @Autowired
    public gui(UIObserver uiObserver) {
        this.uiObserver = uiObserver;
        setTitle("GUI");
        initGlobalPanel();
        setContentPane(globalPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setVisible(true);
    }

    private void initGlobalPanel() {
        globalPanel = new JPanel();
        FormLayout layout = new FormLayout("fill:default:grow", "pref, pref, pref");
        globalPanel.setLayout(layout);
        initButtons();
        CellConstraints cc = new CellConstraints();
        globalPanel.add(startButton, cc.xy(1, 1));
        globalPanel.add(endButton, cc.xy(1, 2));
        initTable();
        globalPanel.add(playerTable, cc.xy(1, 3));
    }

    private void initTable() {
        String[] columnNames = {"Rank", "Player Name", "Score"};

        players = new Player[0];
        playerTable = new JTable(new Object[][]{}, columnNames);
        playerTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        playerTable.setFillsViewportHeight(true);
    }

    private void addPlayer(String name, int score) {
        Player[] newPlayers = new Player[players.length + 1];
        for (int i = 0; i < players.length; i++) {
            newPlayers[i] = players[i];
        }
        newPlayers[players.length] = new Player(players.length + 1, name, score);
        players = newPlayers;
    }

    private void initButtons() {
        startButton = new JButton("Start Game");
        endButton = new JButton("End Game");

        Font buttonFont = new Font("Arial", Font.BOLD, 20); // Change the values as needed
        startButton.setFont(buttonFont);
        endButton.setFont(buttonFont);

        Dimension buttonSize = new Dimension(200, 40); // Change the values as needed
        startButton.setPreferredSize(buttonSize);
        endButton.setPreferredSize(buttonSize);

        startButton.addActionListener(e -> {
            System.out.println("Start button clicked");
            try {
                uiObserver.observeStartSignal();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        endButton.addActionListener(e -> {
            System.out.println("End button clicked");
            try {
                uiObserver.observeStopSignal();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

    }

//    public static void main(String[] args) {
//        new gui();
//    }
}
