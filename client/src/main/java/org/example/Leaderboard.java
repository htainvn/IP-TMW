package org.example;

import org.example.models.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.*;

public class Leaderboard extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable leaderboard;
    private JScrollPane leaderboardPane;

    public Leaderboard(Player[] players, String currentPlayer) {
        // add players to leaderboard
        String[] columnNames = {"Rank", "Username", "Point"};
        Object[][] data = new Object[players.length][3];
        for (int i = 0; i < players.length; i++) {
            data[i][0] = players[i].rank;
            data[i][1] = players[i].username;
            data[i][2] = players[i].point;
        }
        leaderboard.setModel(new JTable(data, columnNames).getModel());
        // center the text in column of leaderboard
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        leaderboard.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        leaderboard.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        leaderboard.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        // highlight the current player
        for (int i = 0; i < players.length; i++) {
            if (players[i].username.equals(currentPlayer)) {
                leaderboard.setRowSelectionInterval(i, i);
                break;
            }
        }

        leaderboardPane.setViewportView(leaderboard);


        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setVisible(true);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
