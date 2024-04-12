/*
 * Created by JFormDesigner on Thu Apr 11 17:42:39 ICT 2024
 */

package org.example.gui;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;
import org.example.observer.Phase;
import org.example.observer.ConnectingState;
import org.example.observer.GameObserver;
import org.example.observer.UIObserver;
import org.example.storage.Storage;
import org.jdesktop.swingx.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author Nhat Hung
 */
@Component
public class Registration extends JFrame {
    private UIObserver uiObserver;
    private GameObserver gameObserver;
    private Storage storage;
    private Dialog connectingDialog;
    @Autowired
    public Registration(UIObserver uiObserver, GameObserver gameObserver, Storage storage) {
        this.uiObserver = uiObserver;
        this.gameObserver = gameObserver;
        this.storage = storage;

        initComponents();
        initButtons();
        setVisible(false);
        progressBar.setVisible(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("The Magic Wheel - Registration");
    }

    private void initButtons() {
        okButton.addActionListener(e -> {
            String dest = destLabelTextField.getText();
            String port = portTextField.getText();
            if (dest.isEmpty() || port.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                okButton.setEnabled(false);
                destLabelTextField.setEnabled(false);
                portTextField.setEnabled(false);
                progressBar.setVisible(true);
                connectingDialog = new Dialog(this, "Connecting to server", true);
                uiObserver.connectServer(dest, port);
            }
        });
    }

    public void update() {
        if (uiObserver.getCurrentState() == ConnectingState.CONNECTED) {
            uiObserver.setCurrentState(ConnectingState.WAITING);
            connectingDialog.setVisible(false);
            setVisible(false);
            JOptionPane.showMessageDialog(null, "Connected to server", "Success", JOptionPane.INFORMATION_MESSAGE);
            String name = JOptionPane.showInputDialog("Enter your name: ");
            uiObserver.setUsername(name);
            storage.setClientName(name);
            uiObserver.setCurrentPhase(Phase.LOBBY);
        } else if (uiObserver.getCurrentState() == ConnectingState.FAILED) {
            uiObserver.setCurrentState(ConnectingState.WAITING);
            connectingDialog.setVisible(false);
            okButton.setEnabled(true);
            destLabelTextField.setEnabled(true);
            portTextField.setEnabled(true);
            progressBar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error connecting to server", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Hung
        globalPanel = new JPanel();
        contentPanel2 = new JPanel();
        destLabel = new JLabel();
        destLabelTextField = new JTextField();
        portLabel = new JLabel();
        portTextField = new JTextField();
        contentPanel = new JPanel();
        panel1 = new JPanel();
        buttonBar = new JPanel();
        okButton = new JButton();
        progressBar = new JProgressBar();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 2));

        //======== globalPanel ========
        {
             globalPanel. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
            ) {if ("\u0062or\u0064er" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} )
            ;
            globalPanel.setLayout(new BorderLayout());

            //======== contentPanel2 ========
            {
                contentPanel2.setLayout(new FormLayout(
                    "default, $lcgap, default:grow",
                    "default:grow, default"));

                //---- destLabel ----
                destLabel.setText("Destination:");
                destLabel.setLabelFor(destLabelTextField);
                destLabel.setFocusable(false);
                contentPanel2.add(destLabel, CC.xy(1, 1));
                contentPanel2.add(destLabelTextField, CC.xy(3, 1));

                //---- portLabel ----
                portLabel.setText("Port: ");
                portLabel.setLabelFor(portTextField);
                portLabel.setHorizontalAlignment(SwingConstants.TRAILING);
                contentPanel2.add(portLabel, CC.xy(1, 2));
                contentPanel2.add(portTextField, CC.xy(3, 2));
            }
            globalPanel.add(contentPanel2, BorderLayout.CENTER);

            //======== contentPanel ========
            {
                contentPanel.setLayout(new HorizontalLayout());
            }
            globalPanel.add(contentPanel, BorderLayout.NORTH);
        }
        contentPane.add(globalPanel, BorderLayout.CENTER);

        //======== panel1 ========
        {
            panel1.setLayout(new BorderLayout());

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton);
            }
            panel1.add(buttonBar, BorderLayout.EAST);

            //---- progressBar ----
            progressBar.setPreferredSize(new Dimension(146, 12));
            progressBar.setIndeterminate(true);
            panel1.add(progressBar, BorderLayout.SOUTH);
        }
        contentPane.add(panel1, BorderLayout.PAGE_END);
        setSize(400, 155);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Hung
    private JPanel globalPanel;
    private JPanel contentPanel2;
    private JLabel destLabel;
    private JTextField destLabelTextField;
    private JLabel portLabel;
    private JTextField portTextField;
    private JPanel contentPanel;
    private JPanel panel1;
    private JPanel buttonBar;
    private JButton okButton;
    private JProgressBar progressBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
