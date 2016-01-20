/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sweng431.lab2;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 *
 * @author yha5009
 */
class MyAWTEventListener implements AWTEventListener {
    
    JTextField number1JTF;
    JTextField number2JTF;
    JTextField sumJTF;
    JButton calcJB;

    @Override
    public void eventDispatched(AWTEvent event) {
        //System.out.println(event);
        MouseEvent me = (MouseEvent) event;
        if (me.getID() == MouseEvent.MOUSE_CLICKED) {
            try {
                JTextField jtf = (JTextField) me.getSource();
                // 28, 108, 179
                if (jtf.getY() <= 28) {
                    System.out.println("Number input 1 linked!");
                    number1JTF = jtf;
                } else if (jtf.getY() <= 108) {
                    System.out.println("Number input 2 linked!");
                    number2JTF = jtf;
                } else if (jtf.getY() <= 179) {
                    System.out.println("Sum output linked!");
                    sumJTF = jtf;
                }
            } catch (Exception e) {
                JButton jbt = (JButton) me.getSource();
                calcJB = jbt;
            }
            
            if (calcJB != null) {
                System.out.println("All linked!");
                try {
                    runTests();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyAWTEventListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           
        }
    }
    
    void runTests() throws InterruptedException {
        ArrayList<Integer> found = new ArrayList();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                number1JTF.setText(String.valueOf(i));
                number2JTF.setText(String.valueOf(j));
                calcJB.doClick();
                Thread.sleep(1);
                int sumI = i + j;
                int checksum = Integer.parseInt(sumJTF.getText());
                if (checksum != sumI) {
                    System.out.print("Test Failed:   ");
                    int dif = checksum - sumI;
                    System.out.println(i+"\t + \t"+j+"\t = \t"+checksum+"\t // Dif = "+dif);
                } else {
                    System.out.print("Test Passed:   ");
                    int dif = checksum - sumI;
                    System.out.println(i+"\t + \t"+j+"\t = \t"+checksum+"\t // Dif = "+dif);
                }
            }
        }
    }
    
}
