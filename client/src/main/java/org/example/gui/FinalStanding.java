/*
 * Created by JFormDesigner on Thu Apr 11 17:55:38 ICT 2024
 */

package org.example.gui;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.example.models.Player;
import org.example.models.PlayerTableModel;
import org.example.storage.Storage;
import org.javatuples.Pair;
import org.jdesktop.swingx.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * @author Nhat Hung
 */
@Component
public class FinalStanding extends JFrame {
    private Integer DISPLAY_TIME = 10000;
    private Storage storage;
    private JTable playerTable;
    private JScrollPane playerTableScrollPane;
    private Vector<Pair<String, Integer>> scores;
    @Autowired
    public FinalStanding(Storage storage) {
        this.storage = storage;
        initComponents();
        initTable();
        initGlobalPanel();

        okButton.addActionListener(e -> {
            storage.resetStorage();
        });
    }
    private void initGlobalPanel() {
    }

    public void initTable() {
        scores = storage.getScores() == null ? new Vector<>() : storage.getScores();

        // sort scores and set rank
        scores.sort((a, b) -> b.getValue1().compareTo(a.getValue1()));

        playerTable = new JTable();
        playerTable.setModel(new PlayerTableModel(scores));
        playerTable.setPreferredScrollableViewportSize(new Dimension(500, 350));
        playerTable.setFillsViewportHeight(true);

        // Increase font size
        playerTable.setFont(new Font("SF Pro Display Regular", Font.PLAIN, 18));
        // Increase height
        playerTable.setRowHeight(30);

        JTableHeader header = playerTable.getTableHeader();
        header.setFont(new Font("SF Pro Display Bold", Font.PLAIN, 16));
        header.setPreferredSize(new Dimension(500, 30));

        contentPanel.removeAll();

        playerTableScrollPane = new JScrollPane(playerTable);
        playerTableScrollPane.setPreferredSize(new Dimension(500, 333));
        contentPanel.add(playerTableScrollPane);
    }

    public void update(Integer time) {
        System.out.println("FinalStanding: " + scores.size() + " " + scores.toString());
        playerTable.setModel(new PlayerTableModel(scores));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        playerTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        playerTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        playerTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }

    public Integer getWaitingTime() {
        return DISPLAY_TIME;
    }
    public void reset() {
        scores = new Vector<>();
        playerTable.setModel(new PlayerTableModel(scores));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Hung
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane. addPropertyChangeListener( new java. beans .
            PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "bord\u0065r" .
            equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
            dialogPane.setLayout(new VerticalLayout());

            //======== contentPanel ========
            {
                contentPanel.setPreferredSize(new Dimension(314, 200));
                contentPanel.setLayout(new VerticalLayout());
            }
            dialogPane.add(contentPanel);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Hung
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
