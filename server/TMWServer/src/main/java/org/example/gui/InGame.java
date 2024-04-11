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
public class InGame extends JFrame {
    private JPanel globalPanel;
    private JButton endButton;
    private JTextArea questionTextArea;
    private JTable playerTable;
    private JScrollPane playerTableScrollPane;
    private Player[] players;

    private UIObserver uiObserver;
    private GameObserver gameObserver;

    @Autowired
    public InGame(UIObserver uiObserver, GameObserver gameObserver) {
        this.uiObserver = uiObserver;
        this.gameObserver = gameObserver;
        setTitle("The Magic Wheel - Game On");
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

        globalPanel.add(endButton, cc.xy(1, 1));

        questionTextArea = new JTextArea("This is a long question that will be displayed here. This is a long question that will be displayed here. This is a long question that will be displayed here. This is a long question that will be displayed here.", 4, 1);
        questionTextArea.setFont(new Font("SF Pro Display Regular", Font.PLAIN, 16));
        questionTextArea.setLineWrap(true);
        globalPanel.add(questionTextArea, cc.xy(1, 3));

        playerTableScrollPane = new JScrollPane(playerTable);
        playerTableScrollPane.setPreferredSize(new Dimension(500, 350));
        globalPanel.add(playerTableScrollPane, cc.xy(1, 5));
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

//        TableRowSorter<PlayerTable> sorter = new TableRowSorter<PlayerTable>(new PlayerTable(players)) {
//            @Override
//            public void toggleSortOrder(int column) {
//                super.toggleSortOrder(column);
//
//                // Step 3: If the points column was clicked, update the rank
//                if (column == 2) {
//                    for (int i = 0; i < getModelWrapper().getRowCount(); i++) {
//                        players[convertRowIndexToModel(i)].rank = i + 1;
//                    }
//                }
//            }
//        };
//        playerTable.setRowSorter(sorter);

        JTableHeader header = playerTable.getTableHeader();
        header.setFont(new Font("SF Pro Display Bold", Font.PLAIN, 16));
        header.setPreferredSize(new Dimension(500, 30));
    }

    private void setPlayerTable() {
        if (players == null) {
            return;
        }

        playerTable.setModel(new PlayerTable(players));

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

    private void updateQuestion(String hint) {
        questionTextArea.setText(hint);
    }

    private void highlightPlayer() {
        int playerIndex = gameObserver.getCurrentIteration();
        if (playerIndex < 0) {
            return;
        }
        playerTable.setRowSelectionInterval(playerIndex, playerIndex);
    }

    public Boolean isGamePlayable() {
        return gameObserver.isGamePlayable();
    }

    public void update() {
        players = uiObserver.getPlayers();
        setPlayerTable();
        highlightPlayer();
        if (!isGamePlayable()) {
            uiObserver.setPhase(3);
        }
    }

//    public static void main(String[] args) {
//        new gui();
//    }
}
