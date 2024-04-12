/*
 * Created by JFormDesigner on Thu Apr 11 18:36:48 ICT 2024
 */

package org.example.gui;

import java.awt.*;
import javax.swing.*;

import org.example.observer.Phase;
import org.example.observer.UIObserver;
import org.example.storage.Storage;
import org.jdesktop.swingx.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Nhat Hung
 */
@Component
public class Lobby extends JFrame {
    Storage storage;
    UIObserver uiObserver;
    @Autowired
    public Lobby(Storage storage, UIObserver uiObserver) {
        this.storage = storage;
        this.uiObserver = uiObserver;
        initComponents();

        setTitle("The Magical Wheel \ud83c\udfa1");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Hung
        panel1 = new JPanel();
        panel2 = new JPanel();
        label2 = new JLabel();
        nameLabel = new JLabel();
        label1 = new JLabel();
        progressBar = new JProgressBar();

        //======== this ========
        setAlwaysOnTop(true);
        setTitle("The Magical Wheel - Lobby");
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        //======== panel1 ========
        {
            panel1. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .
            beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
            panel1.setLayout(new VerticalLayout(10));

            //======== panel2 ========
            {
                panel2.setLayout(new FlowLayout());

                //---- label2 ----
                label2.setText("Welcome ");
                label2.setHorizontalAlignment(SwingConstants.CENTER);
                panel2.add(label2);

                //---- nameLabel ----
                nameLabel.setText("Welcome ");
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                nameLabel.setFont(nameLabel.getFont().deriveFont(nameLabel.getFont().getStyle() | Font.BOLD));
                panel2.add(nameLabel);
            }
            panel1.add(panel2);

            //---- label1 ----
            label1.setText("Waiting for other players to join...");
            label1.setHorizontalAlignment(SwingConstants.CENTER);
            panel1.add(label1);

            //---- progressBar ----
            progressBar.setPreferredSize(new Dimension(250, 10));
            progressBar.setIndeterminate(true);
            panel1.add(progressBar);
        }
        contentPane.add(panel1);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Hung
    private JPanel panel1;
    private JPanel panel2;
    private JLabel label2;
    private JLabel nameLabel;
    private JLabel label1;
    private JProgressBar progressBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private void setPlayerName() {
        nameLabel.setText(storage.getClientName());
    }

    public void update() {
        setPlayerName();
//        System.out.println(
//                "Lobby: " + storage.getClientName() + " " + storage.getGameID() + " " + storage.getGameOrder() + " " + storage.getScores()
//        );
        if (storage.getGameID() != null) {
            uiObserver.setCurrentPhase(Phase.IN_GAME);
        }
    }
}
