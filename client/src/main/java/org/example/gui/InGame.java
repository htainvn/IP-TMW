/*
 * Created by JFormDesigner on Thu Apr 11 15:42:23 ICT 2024
 */

package org.example.gui;

import com.intellij.uiDesigner.core.*;
import org.example.models.Player;
import org.example.models.PlayerTableModel;
import org.example.observer.UIObserver;
import org.example.storage.Storage;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import static java.lang.Math.min;

/**
 * @author Nhat Hung
 */
@Component
public class InGame extends JFrame {
    private JToggleButton[] charButtons;
    private JToggleButton currentCharButton = null;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private final int BUTTON_SIZE = 50;
    private final int WORDS_PER_ROW = 15;
    private final int COUNT_DOWN = 10 * 1000;
    private JTable playerTable;
    private UIObserver uiObserver;
    private Storage storage;
    private JOptionPane confirmDialog;

    private void createUIComponents() {
        // TODO: add custom component creation code here
        timerBar.setValue(0);

        initCharPanel();
        initGuessPanel();
        initPlayerTable();
        confirmDialog = new JOptionPane();
        username.setText(storage.getClientName());

        StyledDocument documentStyle = hintText.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);
        timerFill();
    }
    
    public void initData() {
        setHint();
        username.setText(storage.getClientName());
    }

    public void setKeyword() {

        String keyword = storage.getKeyword() == null ? "Hello World" : storage.getKeyword();
//        keyword = "______________________________";
        System.out.println("Keyword Ingame: " + keyword);
        keywordPanel.removeAll();
        keywordPanel.setLayout(new GridLayout((int) Math.ceil((float)keyword.length() / (float)WORDS_PER_ROW), min(WORDS_PER_ROW, keyword.length())));
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

    private void initCharPanel() {
        buttonGroup = new ButtonGroup();
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

    void initPlayerTable() {
        playerTable = new JTable();
        playerTable.setFillsViewportHeight(true);
        playerTable.setFont(new Font("SF Pro Display Regular", Font.PLAIN, 18));
        playerTable.setRowHeight(30);

        playerPanel.removeAll();
        playerPanel.add(new JScrollPane(playerTable));
    }

    private void initGuessPanel() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer choosenOption = null;
                Character guessChar = ' ';
                if (!keywordGuessing.getText().isEmpty()) {
                    Object[] options = {"Yes", "No"};
                    choosenOption = confirmDialog.showOptionDialog(InGame.this,
                            "Would you like to send keyword?",
                            "Send keyword",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[0]);
                }

                if (currentCharButton != null) {
                    guessChar = currentCharButton.getText().charAt(0);
                    currentCharButton.setSelected(false);
                    currentCharButton.setEnabled(false);
                    currentCharButton = null;
                }

                if(choosenOption == null) return;

                if (0 == choosenOption) {
                    // Send keyword
                    System.out.println("In game: condition01");
                    uiObserver.sendGuess(guessChar, keywordGuessing.getText());
                } else {
                    System.out.println("In game: condition02");
                    uiObserver.sendGuess(guessChar, "");

                }
            }
        });
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Hung
        overallPanel = new JPanel();
        globalPanel = new JPanel();
        userInfoPanel = new JPanel();
        usernameDisplay = new JPanel();
        usernameLabel = new JLabel();
        username = new JLabel();
        pointDisplay = new JPanel();
        pointLabel = new JLabel();
        point = new JLabel();
        guessingPanel = new JPanel();
        JLabel gameTitle = new JLabel();
        keywordPanel = new JPanel();
        hintText = new JTextPane();
        guessLabel = new JLabel();
        charPanel = new JPanel();
        keywordGuessingPanel = new JPanel();
        keywordGuessing = new JTextField();
        sendButton = new JButton();
        timerBar = new JProgressBar();
        playerPanel = new JPanel();

        //======== overallPanel ========
        {
            overallPanel.setName("overallPanel");
            overallPanel.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax
            . swing. border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmDes\u0069gner \u0045valua\u0074ion" , javax. swing
            .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .
            Font ( "D\u0069alog", java .awt . Font. BOLD ,12 ) ,java . awt. Color .red
            ) ,overallPanel. getBorder () ) ); overallPanel. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override
            public void propertyChange (java . beans. PropertyChangeEvent e) { if( "\u0062order" .equals ( e. getPropertyName (
            ) ) )throw new RuntimeException( ) ;} } );
            overallPanel.setLayout(new BorderLayout());

            //======== globalPanel ========
            {
                globalPanel.setMaximumSize(new Dimension(480, 2147483647));
                globalPanel.setMinimumSize(new Dimension(480, 92));
                globalPanel.setPreferredSize(new Dimension(440, 509));
                globalPanel.setName("globalPanel");
                globalPanel.setLayout(new GridBagLayout());

                //======== userInfoPanel ========
                {
                    userInfoPanel.setOpaque(false);
                    userInfoPanel.setName("userInfoPanel");
                    userInfoPanel.setLayout(new BorderLayout());

                    //======== usernameDisplay ========
                    {
                        usernameDisplay.setOpaque(false);
                        usernameDisplay.setPreferredSize(new Dimension(150, 27));
                        usernameDisplay.setName("usernameDisplay");
                        usernameDisplay.setLayout(new FlowLayout(FlowLayout.LEFT));

                        //---- usernameLabel ----
                        usernameLabel.setFont(usernameLabel.getFont().deriveFont(Font.PLAIN));
                        usernameLabel.setText("Nickname:");
                        usernameLabel.setName("usernameLabel");
                        usernameDisplay.add(usernameLabel);

                        //---- username ----
                        username.setText("sss");
                        username.setName("username");
                        usernameDisplay.add(username);
                    }
                    userInfoPanel.add(usernameDisplay, BorderLayout.WEST);

                    //======== pointDisplay ========
                    {
                        pointDisplay.setFocusable(false);
                        pointDisplay.setInheritsPopupMenu(false);
                        pointDisplay.setOpaque(false);
                        pointDisplay.setPreferredSize(new Dimension(150, 27));
                        pointDisplay.setName("pointDisplay");
                        pointDisplay.setLayout(new FlowLayout(FlowLayout.RIGHT));

                        //---- pointLabel ----
                        pointLabel.setFont(pointLabel.getFont().deriveFont(Font.PLAIN));
                        pointLabel.setText("Point:");
                        pointLabel.setName("pointLabel");
                        pointDisplay.add(pointLabel);

                        //---- point ----
                        point.setText("10");
                        point.setName("point");
                        pointDisplay.add(point);
                    }
                    userInfoPanel.add(pointDisplay, BorderLayout.EAST);
                }
                globalPanel.add(userInfoPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

                //======== guessingPanel ========
                {
                    guessingPanel.setBackground(new Color(0x5cf227));
                    guessingPanel.setEnabled(false);
                    guessingPanel.setOpaque(false);
                    guessingPanel.setVisible(true);
                    guessingPanel.setName("guessingPanel");
                    guessingPanel.setLayout(new GridBagLayout());

                    //---- gameTitle ----
                    gameTitle.setFont(new Font("Archivo Expanded Black", gameTitle.getFont().getStyle(), 18));
                    gameTitle.setHorizontalAlignment(SwingConstants.CENTER);
                    gameTitle.setHorizontalTextPosition(SwingConstants.CENTER);
                    gameTitle.setText("The Magical Wheel \ud83c\udfa1");
                    gameTitle.setName("gameTitle");
                    guessingPanel.add(gameTitle, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(10, 0, 0, 0), 0, 0));

                    //======== keywordPanel ========
                    {
                        keywordPanel.setAlignmentY(0.0F);
                        keywordPanel.setBackground(new Color(0xf2e00b));
                        keywordPanel.setOpaque(false);
                        keywordPanel.setName("keywordPanel");
                        keywordPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 0, 5), -1, -1));
                    }
                    guessingPanel.add(keywordPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));

                    //---- hintText ----
                    hintText.setAlignmentY(0.0F);
                    hintText.setAutoscrolls(true);
                    hintText.setBackground(new Color(0xf2e00b));
                    hintText.setEditable(false);
                    hintText.setFocusable(false);
                    hintText.setFont(hintText.getFont().deriveFont(14f));
                    hintText.setMargin(new Insets(0, 3, 0, 3));
                    hintText.setOpaque(false);
                    hintText.setText("AFSDFSADDDDDDDDDDDDDDDDDDDDDDDD SADFASDFSADFSA  AS DFSA DFSADFASDF AS ASD FSADFASF ");
                    hintText.setName("hintText");
                    guessingPanel.add(hintText, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 16, 0, 16), 0, 0));
                }
                globalPanel.add(guessingPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- guessLabel ----
                guessLabel.setFont(guessLabel.getFont().deriveFont(Font.BOLD, 14f));
                guessLabel.setHorizontalAlignment(SwingConstants.CENTER);
                guessLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                guessLabel.setText("Guess a Letter");
                guessLabel.setName("guessLabel");
                globalPanel.add(guessLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

                //======== charPanel ========
                {
                    charPanel.setAutoscrolls(false);
                    charPanel.setBackground(new Color(0xf2e00b));
                    charPanel.setOpaque(false);
                    charPanel.setName("charPanel");
                    charPanel.setLayout(new FlowLayout());
                }
                globalPanel.add(charPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 10.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                    new Insets(0, 16, 0, 16), 0, 0));

                //======== keywordGuessingPanel ========
                {
                    keywordGuessingPanel.setEnabled(true);
                    keywordGuessingPanel.setVisible(true);
                    keywordGuessingPanel.setName("keywordGuessingPanel");
                    keywordGuessingPanel.setLayout(new GridLayoutManager(1, 2, new Insets(4, 32, 0, 32), -1, -1));

                    //---- keywordGuessing ----
                    keywordGuessing.setFont(new Font("Google Sans", keywordGuessing.getFont().getStyle(), keywordGuessing.getFont().getSize()));
                    keywordGuessing.setMargin(new Insets(2, 6, 2, 6));
                    keywordGuessing.setText("");
                    keywordGuessing.setName("keywordGuessing");
                    keywordGuessingPanel.add(keywordGuessing, new GridConstraints(0, 0, 1, 1,
                        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                        GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));

                    //---- sendButton ----
                    sendButton.setFont(sendButton.getFont().deriveFont(14f));
                    sendButton.setText("Send");
                    sendButton.setName("sendButton");
                    keywordGuessingPanel.add(sendButton, new GridConstraints(0, 1, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED,
                        null, null, null));
                }
                globalPanel.add(keywordGuessingPanel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.1,
                    GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- timerBar ----
                timerBar.setAlignmentX(0.0F);
                timerBar.setFocusable(false);
                timerBar.setForeground(new Color(0x05e53a));
                timerBar.setIndeterminate(false);
                timerBar.setString("50%");
                timerBar.setStringPainted(false);
                timerBar.setValue(50);
                timerBar.setVerifyInputWhenFocusTarget(false);
                timerBar.setName("timerBar");
                globalPanel.add(timerBar, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(4, 0, 0, 0), 0, 0));
            }
            overallPanel.add(globalPanel, BorderLayout.CENTER);

            //======== playerPanel ========
            {
                playerPanel.setMinimumSize(new Dimension(200, 10));
                playerPanel.setPreferredSize(new Dimension(200, 10));
                playerPanel.setName("playerPanel");
                playerPanel.setLayout(new FlowLayout());
            }
            overallPanel.add(playerPanel, BorderLayout.LINE_END);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Hung
    private JPanel overallPanel;
    private JPanel globalPanel;
    private JPanel userInfoPanel;
    private JPanel usernameDisplay;
    private JLabel usernameLabel;
    private JLabel username;
    private JPanel pointDisplay;
    private JLabel pointLabel;
    private JLabel point;
    private JPanel guessingPanel;
    private JPanel keywordPanel;
    private JTextPane hintText;
    private JLabel guessLabel;
    private JPanel charPanel;
    private JPanel keywordGuessingPanel;
    private JTextField keywordGuessing;
    private JButton sendButton;
    private JProgressBar timerBar;
    private JPanel playerPanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private void setPoint() {
        point.setText(String.valueOf(storage.getPoint(username.getText())));
    }

    private void setPlayerTable() {
        Vector<Pair<String, Integer>> scores = storage.getScores() == null ? new Vector<>() : storage.getScores();
        playerTable.setModel(new PlayerTableModel(scores));
    }

    public void update() {
        setKeyword();
        setPoint();
        setPlayerTable();
    }

    private void setHint() {
        hintText.setText(storage.getHint());
    }

    @Autowired
    public InGame(UIObserver uiObserver, Storage storage) {
        this.uiObserver = uiObserver;
        this.storage = storage;

        initComponents();

        setTitle("The Magical Wheel \ud83c\udfa1");
        setContentPane(overallPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        createUIComponents();
    }
}
