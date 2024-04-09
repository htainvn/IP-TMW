package org.example.uikits;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.event.ActionEvent;

public class EndButton extends JButton {
  public EndButton(String text) {
    super(text);
    addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println("End button clicked");
      }
    });
  }
}