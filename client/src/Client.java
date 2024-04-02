import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Math.min;
import static java.lang.Math.round;

public class Client extends JFrame {
    private JButton sendKeyword;
    private JTextField keywordGuessing;
    private JPanel keywordGuessingPanel;
    private JPanel charPanel;
    private JPanel guessingPanel;
    private JPanel globalPanel;
    private JTextPane hintText;
    private JPanel keywordPanel;
    private JButton[] charButtons;
    private int BUTTON_SIZE = 50;

    public Client() {

        setTitle("The Magical Wheel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initCharPanel();
//        initGlobalPanel();
        setContentPane(globalPanel);
        setKeyword("1 2 3 4 5 6 7 8 9 10 11 11");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        StyledDocument documentStyle = hintText.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);

    }

    private void setKeyword(String keyword) {
        keywordPanel.removeAll();
        int wordsPerRow = 15;
        keywordPanel.setLayout(new GridLayout(Math.ceilDiv(keyword.length(), wordsPerRow), min(wordsPerRow, keyword.length())));
//        keywordPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
//        keywordPanel.setPreferredSize(new Dimension(BUTTON_SIZE * wordsPerRow, 300));

        for (int i = 0; i < keyword.length(); i++) {
            JLabel label = new JLabel();
            label.setBorder(BorderFactory.createEmptyBorder(1, 2, 0, 0));
            label.setText(String.valueOf(keyword.charAt(i)));
            //Matahari Mono 700 Bold
            label.setFont(new Font("JetBrains Mono", Font.PLAIN, 28));
            keywordPanel.add(label);
        }
        keywordPanel.revalidate();
        keywordPanel.repaint();

    }

    private void initCharPanel() {
        // Initialize the toggleButtons array
        charButtons = new JButton[26];

        // Create and add toggle buttons to the panel
        for (int i = 0; i < 26; i++) {
                JButton button = new JButton();
                button.setSize(50, 50);
                button.setPreferredSize(new Dimension(50, 50));
                button.setName("button_" + i);
                button.setText(String.valueOf((char) ('A' + i)));
                button.setFont(new Font("Google Sans", Font.PLAIN, 16));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton button = (JButton) e.getSource();
                        // set font to Google Sans
                        button.setEnabled(false);
                    }
                });
                charPanel.add(button);
                charButtons[i] = button; // Store toggle buttons in the array
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
