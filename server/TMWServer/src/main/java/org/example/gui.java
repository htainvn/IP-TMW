package org.example;

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
public class gui extends JFrame {
    private JButton startButton;
    private JPanel globalPanel;
    private JButton endButton;
    private JTextArea questionTextArea;
    private JTable playerTable;
    private JScrollPane playerTableScrollPane;
    private Player[] players;

    private UIObserver uiObserver;
    private GameObserver gameObserver;

    @Autowired
    public gui(UIObserver uiObserver, GameObserver gameObserver) {
        this.uiObserver = uiObserver;
        this.gameObserver = gameObserver;
        setTitle("The Magic Wheel Server");
        initFont();
        initGlobalPanel();
        setContentPane(globalPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setVisible(true);

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

    private void initGlobalPanel() {
        globalPanel = new JPanel();
        FormLayout layout = new FormLayout("fill:default:grow:grow", "pref, 4dlu, pref, 4dlu, pref, 4dlu, pref");
        globalPanel.setLayout(layout);
        initButtons();
        CellConstraints cc = new CellConstraints();
        globalPanel.add(startButton, cc.xy(1, 1));
//        globalPanel.add(Box.createVerticalStrut(10));
        globalPanel.add(endButton, cc.xy(1, 3));
//        globalPanel.add(Box.createVerticalStrut(10));
        questionTextArea = new JTextArea("This is a long question that will be displayed here. This is a long question that will be displayed here. This is a long question that will be displayed here. This is a long question that will be displayed here.", 4, 1);
        questionTextArea.setFont(new Font("SF Pro Display Regular", Font.PLAIN, 16));
        questionTextArea.setLineWrap(true);
        globalPanel.add(questionTextArea, cc.xy(1, 5));
        initTable();

        TableRowSorter<PlayerTable> sorter = new TableRowSorter<PlayerTable>(new PlayerTable(players)) {
            @Override
            public void toggleSortOrder(int column) {
                super.toggleSortOrder(column);

                // Step 3: If the points column was clicked, update the rank
                if (column == 2) {
                    for (int i = 0; i < getModelWrapper().getRowCount(); i++) {
                        players[convertRowIndexToModel(i)].rank = i + 1;
                    }
                }
            }
        };
        playerTable.setRowSorter(sorter);

        JTableHeader header = playerTable.getTableHeader();
        header.setFont(new Font("SF Pro Display Bold", Font.PLAIN, 16));

        playerTableScrollPane = new JScrollPane(playerTable);
        playerTableScrollPane.setPreferredSize(new Dimension(500, 350));
        globalPanel.add(playerTableScrollPane, cc.xy(1, 7));
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
//        System.out.println("Setting player table");
//        for (Player player : players) {
//            System.out.println(player.username + " " + player.point);
//        }
        playerTable.setModel(new PlayerTable(players));
        // center text in columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        playerTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        playerTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        playerTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
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
                updateQuestion();
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

    private void updateQuestion() {
        questionTextArea.setText(gameObserver.getHint());
    }

    private void updateButtons() {
        if (players.length >= 2 && !gameObserver.isGamePlayable()) {
            startButton.setEnabled(true);
        } else {
            startButton.setEnabled(false);
        }
    }

    private void highlightPlayer() {
        int playerIndex = gameObserver.getCurrentIteration();
//        System.out.println("Player index: " + playerIndex);
        if (playerIndex < 0) {
            return;
        }
        playerTable.setRowSelectionInterval(playerIndex, playerIndex);
    }

    public void update() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    javax.swing.SwingUtilities.invokeAndWait(() -> {
                        players = uiObserver.getPlayers();
                        setPlayerTable();
                        setPlayerTable();
                        updateButtons();
                        highlightPlayer();
                    });
                } catch (InterruptedException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    public static void main(String[] args) {
//        new gui();
//    }
}
