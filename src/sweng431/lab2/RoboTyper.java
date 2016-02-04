/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sweng431.lab2;

import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 *
 * @author yha5009
 */
public class RoboTyper extends Robo {
    
    RoboTyper() {
        
    }
    
    public void type(int i, Robot robert) {
        robert.delay(40);
        robert.keyPress(i);
        robert.keyRelease(i);
    }
 
    public void type(String s, Robot robert) {
        char[] letters = s.toCharArray();
        for (char b : letters)
        {
          int code = b;
          // keycode only handles [A-Z] (which is ASCII decimal [65-90])
//          if (code > 96 && code < 123) code = code - 32;
          robert.delay(100);
          robert.keyPress(code);
          robert.keyRelease(code);
        }
    }
}
