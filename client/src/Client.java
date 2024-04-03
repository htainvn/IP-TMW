import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Math.min;
import static java.lang.Math.round;

public class Client extends JFrame {
    private JButton sendButton;
    private JTextField keywordGuessing;
    private JPanel keywordGuessingPanel;
    private JPanel charPanel;
    private JPanel guessingPanel;
    private JPanel globalPanel;
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
    private JProgressBar timerBar;
    private JToggleButton[] charButtons;
    private JToggleButton currentCharButton = null;
    private ButtonGroup buttonGroup;
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

    public Client() {
        this(0);
    }

    public Client(int keywordLength) {
        String name = JOptionPane.showInputDialog("Enter your name: ");
        username.setText(name);

        this.keywordLength = keywordLength;
        buttonGroup = new ButtonGroup();

        setTitle("The Magical Wheel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setKeyword("");

        timerBar.setValue(0);

        initGuessPanel();
        initCharPanel();
        setContentPane(globalPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        StyledDocument documentStyle = hintText.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);
        timerFill();
    }

    public void startRunning() {
        try {
//            status.setText("Attempting Connection ...");
            try {
                connection = new Socket(InetAddress.getByName(serverIP), port);
            } catch (IOException ioEception) {
                JOptionPane.showMessageDialog(null, "Server Might Be Down!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
//            status.setText("Connected to: " + connection.getInetAddress().getHostName());


            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());

            whileChatting();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    private void whileChatting() throws IOException
    {
//        jTextField1.setEditable(true);
//        do{
//            try
//            {
//                message = (String) input.readObject();
//                chatArea.append("\n"+message);
//            }
//            catch(ClassNotFoundException classNotFoundException)
//            {
//            }
//        }while(!message.equals("Client - END"));
    }


    private void sendMessage(String message)
    {
//        try
//        {
//            chatArea.append("\nME(Client) - "+message);
//            String encryptedmsg = encyrDecry.encrypt(message, secretKey);
//            System.out.println(encryptedmsg);
//            output.writeObject("                                                             (enc):" + encryptedmsg);
//            EncryDecry encyrDecry = new EncryDecry();
//            message = encyrDecry.decrypt(encryptedmsg, secretKey);
//            output.writeObject("                                                             Client(decrypt) - " + message);
//            output.flush();
//        }
//        catch(IOException ioException)
//        {
//            chatArea.append("\n Unable to Send Message");
//        }
    }

    private void initGuessPanel() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choosenOption = 0;
                if (!keywordGuessing.getText().isEmpty()) {
                    Object[] options = {"Yes", "No"};
                    choosenOption = JOptionPane.showOptionDialog(Client.this,
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

    public void setKeyword(String keyword) {
        keyword = keyword.replace('*', '_');

        if (keyword.isEmpty()) {
            for (int i = 0; i < keywordLength; i++) {
                keyword += "_";
            }
        }

        keywordPanel.removeAll();
        keywordPanel.setLayout(new GridLayout(Math.ceilDiv(keyword.length(), WORDS_PER_ROW), min(WORDS_PER_ROW, keyword.length())));
//        keywordPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
//        keywordPanel.setPreferredSize(new Dimension(BUTTON_SIZE * wordsPerRow, 300));

        for (int i = 0; i < keyword.length(); i++) {
            JLabel label = new JLabel();
            label.setBorder(BorderFactory.createEmptyBorder(1, 4, 0, 0));
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

                        // set font to Google Sans
//                        button.setEnabled(false);
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

    public static void main(String[] args) {
        new Client(5);
    }
}
