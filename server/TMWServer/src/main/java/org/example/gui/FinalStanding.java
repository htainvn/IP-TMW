package org.example.gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.example.models.PlayerTable;
import org.example.observer.GameObserver;
import org.example.observer.UIObserver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
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
public class FinalStanding extends JFrame {
    private JPanel globalPanel;
    private JTable playerTable;
    private JLabel backLabel;
    private JButton endButton;
    private JScrollPane playerTableScrollPane;
    private Player[] players;

    private UIObserver uiObserver;
    private GameObserver gameObserver;
    private Integer DISPLAY_TIME = 10000;

    @Autowired
    public FinalStanding(UIObserver uiObserver, GameObserver gameObserver) {
        this.uiObserver = uiObserver;
        this.gameObserver = gameObserver;
        setTitle("The Magic Wheel - Final Standing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 425);
        setVisible(false);

        initButtons();
        initTable();
        initGlobalPanel();
        setContentPane(globalPanel);

        players = uiObserver.getPlayers();
        setPlayerTable();
    }

    private void initGlobalPanel() {
        globalPanel = new JPanel();
        FormLayout layout = new FormLayout("fill:default:grow:grow", "4dlu, pref, 4dlu, pref");
        globalPanel.setLayout(layout);
        CellConstraints cc = new CellConstraints();
        backLabel = new JLabel("Back to lobby");
        backLabel.setFont(new Font("SF Pro Display Regular", Font.PLAIN, 18));
        // center text
        backLabel.setHorizontalAlignment(JLabel.CENTER);
        globalPanel.add(backLabel, cc.xy(1, 2));

        JTableHeader header = playerTable.getTableHeader();
        header.setFont(new Font("SF Pro Display Bold", Font.PLAIN, 16));
        header.setPreferredSize(new Dimension(500, 30));

        playerTableScrollPane = new JScrollPane(playerTable);
        playerTableScrollPane.setPreferredSize(new Dimension(500, 333));
        globalPanel.add(playerTableScrollPane, cc.xy(1, 4));
    }

    private void initTable() {
        String[] columnNames = {"Rank", "Player Name", "Score"};

        players = new Player[0];
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
        playerTable.setModel(new PlayerTable(players));
        // center text in columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        playerTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        playerTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        playerTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }

    private void initButtons() {
        endButton = new JButton("End Game");

        Font buttonFont = new Font("SF Pro Display Medium", Font.PLAIN, 18);
        endButton.setFont(buttonFont);

        Dimension buttonSize = new Dimension(200, 40);
        endButton.setPreferredSize(buttonSize);

        endButton.addActionListener(e -> {
            System.out.println("End button clicked");
            try {
                uiObserver.observeStopSignal();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void update(Integer time) {
        if (time >= DISPLAY_TIME) {
            uiObserver.setPhase(1);
        }
        backLabel.setText("Back to lobby in " + (DISPLAY_TIME - time) / 1000 + " seconds");
    }

    public long getWaitingTime() {
        return DISPLAY_TIME;
    }

//    public static void main(String[] args) {
//        new gui();
//    }
}
