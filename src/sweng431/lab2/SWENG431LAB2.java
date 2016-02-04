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
//    public static void main (String[] args) throws java.lang.Exception
//	{
//		int [] t1 = {1,2,3,4,5,6,7};
//		int [] t2 = {6,7,1,2,3,4,5};
//		int [] t3 = {3,4,5,6,7,1,2};
//                int [] tt4 = {1};
//		System.out.println(t1[getP(t1)]);
//		System.out.println(t2[getP(t2)]);
//		System.out.println(t3[getP(t3)]);
//                System.out.println(tt4[getP(tt4)]);
//		
//	}
//	
//	static int getP(int [] A) {
//		int left = 0, right = A.length-1;
//		while (true) {
//			int mid = left + (right - left)/2;
//			if (right == left) return mid;
//			if (A[mid] > A[right]) {
//				left = mid;
//			} else {
//				right = mid;
//			}
//		}
//	}
    
}
