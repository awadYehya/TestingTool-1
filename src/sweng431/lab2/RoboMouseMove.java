/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sweng431.lab2;

import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author yha5009
 */
public class RoboMouseMove extends RoboAction {
    
    private AWTEvent event;
    
    public RoboMouseMove(String id, int type, AWTEvent event, long time) {
        super(id, type, time);
        this.event = event;
    }
    
    public void doAction (Robot robert) {
        //System.out.println(SwingUtilities.isEventDispatchThread());
        MouseEvent m = (MouseEvent) event;
        Point clickLoc = m.getLocationOnScreen();
        robert.mouseMove(clickLoc.x,clickLoc.y);
    }
    
}
