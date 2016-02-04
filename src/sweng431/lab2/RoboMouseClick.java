/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sweng431.lab2;

import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import javax.swing.SwingUtilities;

/**
 *
 * @author yha5009
 */
class RoboMouseClick extends RoboAction {

    AWTEvent event;
    
    public RoboMouseClick(String mouse, int MOUSE_MOVE, AWTEvent event,
            long timeStamp) {
        super(mouse, MOUSE_MOVE, timeStamp);
        this.event = event;
    }
    
    private void leftClick(Robot robot) {
        System.out.println("MOUSE CLICKO");
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(200);
    }
    
    void doAction(Robot robert) {
        int x = ((MouseEvent) event).getXOnScreen();
        int y = ((MouseEvent) event).getYOnScreen();
        System.out.println(SwingUtilities.isEventDispatchThread());
        MouseEvent m = (MouseEvent) event;
        robert.mouseMove(x, y);
        leftClick(robert);
    }
}
