/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sweng431.lab2;

import gui.MyGUI;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;

/**
 *
 * @author yha5009
 */
public class SWENG431LAB2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws AWTException {
//        Robot r = new Robot();
//        for (int i = 0; i < 500; i++) {
//            r.mouseMove(i, 100);
//        }
        TesterMenu tm = new TesterMenu();
        tm.setVisible(true);
    }
    
}
