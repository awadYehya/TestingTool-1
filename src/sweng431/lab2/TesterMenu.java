/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sweng431.lab2;

import gui.MyGUI;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author yha5009
 */
public class TesterMenu extends javax.swing.JFrame {
    
    private ArrayList<JComponent> testComponents = new ArrayList<>();
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private ArrayList<Integer> vals1, vals2, expectedSums;
    /**
     * Creates new form TesterMenu
     */
    public TesterMenu() {
        this.vals1 = new ArrayList<>();
        this.vals2 = new ArrayList<>();
        this.expectedSums = new ArrayList<>();
        initComponents();
        initCheckBoxes();
        runTarget();
    }
    
    /* Runs the GUI that will be tested */
    private void runTarget() {
        Thread GUIthread = new Thread(new Runnable() {
            @Override
            public void run() {
                MyGUI mgui = new MyGUI();
                mgui.setSize(500,400);
                mgui.setVisible(true);
            }
        });
        GUIthread.start();
    }
    
    /* Special setup for the checkboxes */
    private void initCheckBoxes() {
        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getSource());
                JCheckBox ck = (JCheckBox) e.getSource();
                boolean curr = ck.isSelected();
                ck.setSelected(!curr);
            }
        };
        
        linkedVal1CB.addActionListener(al);
        linkedVal2CB.addActionListener(al);
        linkedOkCB.addActionListener(al);
        selectedCB.addActionListener(al);
        linkedSumCB.addActionListener(al);
    }
    
    /* Starts the linking process and adds the listeners required */
    private void startLink(JButton buttonClicked, String id, JCheckBox check) {
        JFrame thisFrame = this;
        thisFrame.setEnabled(false);
        String origText = buttonClicked.getText();
        buttonClicked.setText("CLICK ON THE COMPONENT");
        AWTEventListener ml = new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                try {
                    System.out.println(event);
                    Container source = (Container) event.getSource();
                    JFrame GUIFrame = (JFrame) SwingUtilities.windowForComponent(source);
                    if (event.getID() == MouseEvent.MOUSE_CLICKED && !GUIFrame.equals(thisFrame)) {
                        JComponent jcomp = (JComponent) event.getSource();
                        link(jcomp, id);
                        buttonClicked.setText(origText);
                        check.setSelected(true);
                        thisFrame.setEnabled(true);
                        tk.removeAWTEventListener(this);
                    }
                } catch (NullPointerException exep) {
                    buttonClicked.setText(origText);
                    tk.removeAWTEventListener(this);
                    thisFrame.setEnabled(true);
                    JOptionPane.showMessageDialog(null, id+": Failed to link!\n"
                            + "There is no time to test the testing tools!\n"
                            + "Please select the correct components!", "Fail",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        tk.addAWTEventListener(ml, AWTEvent.MOUSE_EVENT_MASK);
    }
    
    /* Links the component and adds it to the test components Array List*/
    private void link(JComponent linkComp, String id) {
        linkComp.setName(id);
        testComponents.remove(linkComp);
        testComponents.add(linkComp);
    }
    
    /* Returns if all steps necessary for testing are completed */
    private boolean allStepsCompleted() {
        return linkedOkCB.isSelected() && linkedSumCB.isSelected() && linkedVal1CB.isSelected() && linkedVal2CB.isSelected();
    }
    
    private int doTest(int num1, int num2) throws InterruptedException {
        JButton calcButton = null;
        JTextField val1 = null, val2 = null, sum = null;
        for (JComponent comp : testComponents) {
            switch (comp.getName()) {
                case "val1":
                    val1 = (JTextField) comp;
                    break;
                case "val2":
                    val2 = (JTextField) comp;
                    break;
                case "sum":
                    sum = (JTextField) comp;
                    break;
                case "ok":
                    calcButton = (JButton) comp;
                    break;
            }
        }
        JFrame fram = (JFrame) SwingUtilities.windowForComponent(val1);
        val1.setText(String.valueOf(num1));
        fram.paintComponents(fram.getGraphics());
        Thread.sleep((long) timeDelaySlider.getValue());
        val2.setText(String.valueOf(num2));
        fram.paintComponents(fram.getGraphics());
        Thread.sleep((long) timeDelaySlider.getValue());
        calcButton.doClick();
        fram.paintComponents(fram.getGraphics());
        int expected = num1 + num2;
        int output = Integer.parseInt(sum.getText());
        Color defColor = sum.getBackground();
        if (output != expected) {
            System.out.print("Test Failed:   ");
            int dif = output - expected;
            System.out.println(num1+"\t + \t"+num2+"\t = \t"+output+"\t // Dif = "+dif);
            sum.setBackground(Color.RED);
        } else {
            System.out.print("Test Passed:   ");
            int dif = output - expected;
            System.out.println(num1+"\t + \t"+num2+"\t = \t"+output+"\t // Dif = "+dif);
            sum.setBackground(Color.GREEN);
        }
        fram.paintComponents(fram.getGraphics());
        Thread.sleep((long) timeDelaySlider.getValue());
        sum.setBackground(defColor);
        val1.setText("");
        val2.setText("");
        sum.setText("");
        return expected - output;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        valField1 = new javax.swing.JButton();
        valField2 = new javax.swing.JButton();
        sumField = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        startTestingButton = new javax.swing.JButton();
        selectFile = new javax.swing.JButton();
        linkedVal1CB = new javax.swing.JCheckBox();
        linkedVal2CB = new javax.swing.JCheckBox();
        linkedSumCB = new javax.swing.JCheckBox();
        selectedCB = new javax.swing.JCheckBox();
        linkedOkCB = new javax.swing.JCheckBox();
        timeDelaySlider = new javax.swing.JSlider();
        timeDelayLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        valField1.setText("Value field");
        valField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valField1ActionPerformed(evt);
            }
        });

        valField2.setText("Value field");
        valField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valField2ActionPerformed(evt);
            }
        });

        sumField.setText("Sum field");
        sumField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sumFieldActionPerformed(evt);
            }
        });

        okButton.setText(" OK button");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        startTestingButton.setText("START TESTING");
        startTestingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startTestingButtonActionPerformed(evt);
            }
        });

        selectFile.setText("Select test file");
        selectFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectFileActionPerformed(evt);
            }
        });

        linkedVal1CB.setText("Linked");

        linkedVal2CB.setText("Linked");

        linkedSumCB.setText("Linked");

        selectedCB.setText("Selected");

        linkedOkCB.setText("Linked");

        timeDelaySlider.setMaximum(1000);
        timeDelaySlider.setValue(10);
        timeDelaySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeDelaySliderStateChanged(evt);
            }
        });
        timeDelaySlider.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                timeDelaySliderMouseMoved(evt);
            }
        });
        timeDelaySlider.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                timeDelaySliderPropertyChange(evt);
            }
        });

        timeDelayLabel.setText("100ms");

        jLabel1.setText("Usage:");

        jLabel2.setText("1. Click on one of the buttons");

        jLabel3.setText("2. Click on the corresponding component");

        jLabel4.setText("3. Repeat for all other buttons");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Testing Tool 1.0");

        jLabel6.setText("Delay");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(0, 43, Short.MAX_VALUE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startTestingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(valField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(sumField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(valField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(selectFile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(linkedVal1CB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(linkedVal2CB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(linkedSumCB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(linkedOkCB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(selectedCB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)))
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeDelaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeDelayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valField1)
                    .addComponent(linkedVal1CB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valField2)
                    .addComponent(linkedVal2CB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sumField)
                    .addComponent(linkedSumCB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(linkedOkCB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectFile)
                    .addComponent(selectedCB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeDelayLabel)
                    .addComponent(timeDelaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startTestingButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void valField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valField1ActionPerformed
        startLink((JButton) evt.getSource(), "val1", linkedVal1CB);
    }//GEN-LAST:event_valField1ActionPerformed

    private void valField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valField2ActionPerformed
        startLink((JButton) evt.getSource(), "val2", linkedVal2CB);
    }//GEN-LAST:event_valField2ActionPerformed

    private void sumFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sumFieldActionPerformed
        startLink((JButton) evt.getSource(), "sum", linkedSumCB);
    }//GEN-LAST:event_sumFieldActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        startLink((JButton) evt.getSource(), "ok", linkedOkCB);
    }//GEN-LAST:event_okButtonActionPerformed

    private void startTestingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startTestingButtonActionPerformed
        if (allStepsCompleted()) {
            for (int i = 0; i < this.expectedSums.size(); i++) {
                try {
                    doTest(this.vals1.get(i), this.vals2.get(i));
                } catch (InterruptedException ex) {
                    Logger.getLogger(TesterMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please complete "
                    + "all steps first.", "Incomplete.",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_startTestingButtonActionPerformed

    private void selectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectFileActionPerformed
        JFileChooser fc = new JFileChooser();
        int status = fc.showOpenDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            FileReader fr = null;
            Scanner scnr = null;
            try {
                scnr = new Scanner(selectedFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TesterMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Clear old input
            this.vals1.clear(); this.vals2.clear(); this.expectedSums.clear();
            // Read new input
            while(scnr.hasNextInt()) {
                this.vals1.add(scnr.nextInt());
                this.vals2.add(scnr.nextInt());
                this.expectedSums.add(scnr.nextInt());
            }
            selectedCB.setSelected(true);
        } else {
            // no file selected
        }
    }//GEN-LAST:event_selectFileActionPerformed

    private void timeDelaySliderPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_timeDelaySliderPropertyChange
        
    }//GEN-LAST:event_timeDelaySliderPropertyChange

    private void timeDelaySliderMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeDelaySliderMouseMoved
        
    }//GEN-LAST:event_timeDelaySliderMouseMoved

    private void timeDelaySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timeDelaySliderStateChanged
        timeDelayLabel.setText(timeDelaySlider.getValue()+"ms");
    }//GEN-LAST:event_timeDelaySliderStateChanged

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(TesterMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(TesterMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(TesterMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(TesterMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TesterMenu().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JCheckBox linkedOkCB;
    private javax.swing.JCheckBox linkedSumCB;
    private javax.swing.JCheckBox linkedVal1CB;
    private javax.swing.JCheckBox linkedVal2CB;
    private javax.swing.JButton okButton;
    private javax.swing.JButton selectFile;
    private javax.swing.JCheckBox selectedCB;
    private javax.swing.JButton startTestingButton;
    private javax.swing.JButton sumField;
    private javax.swing.JLabel timeDelayLabel;
    private javax.swing.JSlider timeDelaySlider;
    private javax.swing.JButton valField1;
    private javax.swing.JButton valField2;
    // End of variables declaration//GEN-END:variables
}
