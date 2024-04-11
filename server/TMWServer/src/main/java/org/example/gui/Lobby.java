package org.example.gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.example.models.Registration;
import org.example.models.RegistrationTable;
import org.example.observer.GameObserver;
import org.example.observer.UIObserver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

import org.example.models.Player;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class Lobby extends JFrame {
    private JButton startButton;
    private JPanel globalPanel;
    private JButton endButton;
    private JTable playerTable;
    private JScrollPane playerTableScrollPane;
    private Registration[] players;
    private UIObserver uiObserver;
    private GameObserver gameObserver;

    @Autowired
    public Lobby(UIObserver uiObserver, GameObserver gameObserver) {
        this.uiObserver = uiObserver;
        this.gameObserver = gameObserver;
        setTitle("The Magic Wheel - Lobby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setVisible(false);

        initButtons();
        initTable();
        initGlobalPanel();
        setContentPane(globalPanel);
    }

    private void initGlobalPanel() {
        globalPanel = new JPanel();
        FormLayout layout = new FormLayout("fill:default:grow:grow", "pref, 4dlu, pref, 4dlu, pref");
        globalPanel.setLayout(layout);

        CellConstraints cc = new CellConstraints();
        globalPanel.add(startButton, cc.xy(1, 1));
        globalPanel.add(endButton, cc.xy(1, 3));

        JTableHeader header = playerTable.getTableHeader();
        header.setFont(new Font("SF Pro Display Bold", Font.PLAIN, 16));
        playerTableScrollPane = new JScrollPane(playerTable);
        playerTableScrollPane.setPreferredSize(new Dimension(500, 350));
        globalPanel.add(playerTableScrollPane, cc.xy(1, 5));
    }

    private void initTable() {
        String[] columnNames = {"Player Name"};

        players = new Registration[0];
        playerTable = new JTable(new Object[][]{}, columnNames);
        playerTable.setPreferredScrollableViewportSize(new Dimension(500, 350));
        playerTable.setFillsViewportHeight(true);

        // Increase font size
        playerTable.setFont(new Font("SF Pro Display Regular", Font.PLAIN, 18));
        // Increase height
        playerTable.setRowHeight(30);
    }

    private void setPlayerTable() {
        if (players == null) {
            return;
        }
        playerTable.setModel(new RegistrationTable(players));
        // center text in columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        playerTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        playerTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
    }

    private void initButtons() {
        startButton = new JButton("Start Game");
        endButton = new JButton("End Game");

        Font buttonFont = new Font("SF Pro Display Medium", Font.PLAIN, 18); // Change the values as needed
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
    private void updateButtons() {
        if (players.length >= 2 && !gameObserver.isGamePlayable()) {
            startButton.setEnabled(true);
        } else {
            startButton.setEnabled(false);
        }
    }

    public void update() {
        Player players[] = uiObserver.getPlayers();
        this.players = new Registration[players.length];
        for (int i = 0; i < players.length; i++) {
            this.players[i] = new Registration(i + 1, players[i].getUsername());
        }
        setPlayerTable();
        updateButtons();
    }

//    public static void main(String[] args) {
//        new gui();
//    }
}
