/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sweng431.lab2;

import java.awt.Robot;

/**
 *
 * @author yha5009
 */
abstract class RoboAction extends Robo {
    private final String actionID;
    private final int actionType;
    private final long timeStamp;
    public static final int MOUSE_CLICK = 1;
    public static final int MOUSE_MOVE = 2;
    
    public RoboAction(String id, int type, long timeStamp) {
        actionID = id;
        actionType = type;
        this.timeStamp = timeStamp;
    }
    
    public long getTimeStamp() {
        return timeStamp;
    }
    
    abstract void doAction (Robot rob);

    @Override
    public String toString() {
        return actionID; //To change body of generated methods, choose Tools | Templates.
    }
}
