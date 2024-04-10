package org.example;

import org.example.observer.UIObserver;
import org.example.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.lang.Math.min;
import static java.lang.Math.round;

@Component
@Order(2)
public class GUI extends JFrame {
    private JButton sendButton = new JButton("Send");
    private JTextField keywordGuessing;
    private JPanel keywordGuessingPanel;
    private JPanel charPanel = new JPanel();
    private JPanel guessingPanel;
    private JPanel globalPanel = new JPanel();
    private JTextPane hintText;
    private JPanel keywordPanel;
    private JPanel userInfoPanel;
    private JPanel usernameDisplay;
    private JPanel myTurnDisplay;
    private JLabel usernameLabel;
    private JLabel username;
    private JLabel myTurnLabel;
    private JLabel myTurnText;
    private JPanel pointDisplay;
    private JLabel pointLabel;
    private JLabel point;
    private JLabel guessLabel;
    private JProgressBar timerBar = new JProgressBar();
    private JToggleButton[] charButtons;
    private JToggleButton currentCharButton = null;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private final int BUTTON_SIZE = 50;
    private final int WORDS_PER_ROW = 15;
    private final int COUNT_DOWN = 10 * 1000;
    private int keywordLength;
    private int countTurn;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message="";
    private String serverIP;
    private Socket connection;
    private int port = 6789;
    final static String secretKey = "secrete";

    private UIObserver uiObserver;
    private Storage storage;

    @Autowired
    public GUI(UIObserver uiObserver, Storage storage) {
        this.uiObserver = uiObserver;
        this.storage = storage;

        setTitle("The Magical Wheel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setKeyword();

        timerBar.setValue(0);

        initGuessPanel();
        initCharPanel();
        setContentPane(globalPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        buttonGroup = new ButtonGroup();

        StyledDocument documentStyle = hintText.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);
        timerFill();

        String name = JOptionPane.showInputDialog("Enter your name: ");
        username.setText(name);
    }

    private void initGuessPanel() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choosenOption = 0;
                if (!keywordGuessing.getText().isEmpty()) {
                    Object[] options = {"Yes", "No"};
                    choosenOption = JOptionPane.showOptionDialog(GUI.this,
                            "Would you like to send keyword?",
                            "Send keyword",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[0]);
                }

                if (currentCharButton != null) {
                    String guess = currentCharButton.getText();
                    currentCharButton.setSelected(false);
                    currentCharButton.setEnabled(false);
                    currentCharButton = null;
                }

                if (0 == choosenOption) {
                    // Send keyword

                }
            }
        });
    }

    public void setKeyword() {

        String keyword = storage.getKeyword() == null ? "Hello World" : storage.getKeyword();

        keywordPanel.removeAll();
        keywordPanel.setLayout(new GridLayout(Math.floorDiv(keyword.length(), WORDS_PER_ROW), min(WORDS_PER_ROW, keyword.length())));
//        keywordPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
//        keywordPanel.setPreferredSize(new Dimension(BUTTON_SIZE * wordsPerRow, 300));

        for (int i = 0; i < keyword.length(); i++) {
            JLabel label = new JLabel();
            label.setBorder(BorderFactory.createEmptyBorder(1, 4, 0, 0));
            label.setText(String.valueOf(keyword.charAt(i)));
            label.setFont(new Font("JetBrains Mono", Font.PLAIN, 28));
            keywordPanel.add(label);
        }
        keywordPanel.revalidate();
        keywordPanel.repaint();

    }

    private void initCharPanel() {
        // Initialize the toggleButtons array
        charButtons = new JToggleButton[26];

        // Create and add toggle buttons to the panel
        for (int i = 0; i < 26; i++) {
                JToggleButton button = new JToggleButton();
                button.setSize(BUTTON_SIZE, BUTTON_SIZE);
                button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                button.setName("button_" + i);
                button.setText(String.valueOf((char) ('A' + i)));
                button.setFont(new Font("Google Sans", Font.PLAIN, 16));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentCharButton = (JToggleButton) e.getSource();
                    }
                });
                charPanel.add(button);
                buttonGroup.add(button);
                charButtons[i] = button; // Store toggle buttons in the array
        }
    }

    public void timerFill() {
        int i = 0;
        final int increment = 100;
        try {
            while (i <= COUNT_DOWN) {
                timerBar.setValue((int) (100.0 - (float)i * 100.0 / (float)COUNT_DOWN));
                Thread.sleep(increment);
                i += increment;
            }
            sendButton.doClick();
        } catch (Exception e) {
        }
    }
}
