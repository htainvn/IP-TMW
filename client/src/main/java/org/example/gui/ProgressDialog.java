/*
 * Created by JFormDesigner on Thu Apr 11 18:36:48 ICT 2024
 */

package org.example.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.jdesktop.swingx.*;

/**
 * @author Nhat Hung
 */
public class ProgressDialog extends JDialog {
    public ProgressDialog(Window owner) {
        super(owner);
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Hung
        progressBar1 = new JProgressBar();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        //---- progressBar1 ----
        progressBar1.setPreferredSize(new Dimension(250, 10));
        progressBar1.setIndeterminate(true);
        contentPane.add(progressBar1);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Hung
    private JProgressBar progressBar1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
