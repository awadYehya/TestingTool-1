/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sweng431.lab2;

import gui.MyGUI;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import static jdk.nashorn.internal.objects.NativeRegExp.exec;
import static jdk.nashorn.internal.runtime.ScriptingFunctions.exec;
import org.w3c.dom.css.Counter;

/**
 *
 * @author yha5009
 */
public class TesterMenu extends javax.swing.JFrame {
    
    private ArrayList<JComponent> testComponents = new ArrayList<>();
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private ArrayList<Integer> vals1, vals2, expectedSums;
    private ArrayList<Integer> found = new ArrayList<>();
    private ArrayList<Long> roboDelays = new ArrayList<>();
    private ArrayList<RoboAction> roboActions = new ArrayList<>();
    /**
     * Creates new form TesterMenu
     */
    public TesterMenu() {
        this.vals1 = new ArrayList<>();
        this.vals2 = new ArrayList<>();
        this.expectedSums = new ArrayList<>();
        initComponents();
        initCheckBoxes();
        initRobotButtons();
        runTarget();
        log("---- Initialized successfully ----");
        log("To record, you must complete all linking steps first.");
    }
    
    private void initRobotButtons() {
        testRobotButton.setEnabled(false);
        recordButton.setEnabled(false);
        playButton.setEnabled(false);
        stopButton.setEnabled(false);
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
                //System.out.println(e.getSource());
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
                    //System.out.println(event);
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
    
    private void log(String str) {
        textLog.append(str+"\n");
    }
    
    /* Links the component and adds it to the test components Array List*/
    private void link(JComponent linkComp, String id) {
        linkComp.setName(id);
        testComponents.remove(linkComp);
        testComponents.add(linkComp);
        log("Linked: "+id+" to a "+linkComp.getClass());
    }
    
    /* Returns if all steps necessary for testing are completed */
    private boolean allStepsCompleted() {
        return linkedOkCB.isSelected() && linkedSumCB.isSelected() 
                && linkedVal1CB.isSelected() && linkedVal2CB.isSelected() 
                && selectedCB.isSelected();
    }
    
    private JButton calcButton = null;
    private JTextField val1 = null, val2 = null, sum = null;
    private int doTest(int num1, int num2, int expec) throws InterruptedException {
        JFrame fram = (JFrame) SwingUtilities.windowForComponent(val1);
        val1.setText(String.valueOf(num1));
//        fram.paintComponents(fram.getGraphics());
        Thread.sleep((long) timeDelaySlider.getValue());
        val2.setText(String.valueOf(num2));
//        fram.paintComponents(fram.getGraphics());
        Thread.sleep((long) timeDelaySlider.getValue());
        calcButton.doClick();
//        fram.paintComponents(fram.getGraphics());
        int expected = expec;
        int output = Integer.parseInt(sum.getText());
        Color defColor = sum.getBackground();
        if (output != expected) {
//            found.add(num1);
//            found.add(num2);
            int dif = output - expected;
            log("FAIL: "+num1+" + "+num2+" = "+output+" // Dif = "+dif);
            sum.setBackground(Color.RED);
        } else {
//            found.add(num1);
//            found.add(num2);
            int dif = output - expected;
            log("PASS: "+num1+" + "+num2+" = "+output+" // Dif = "+dif);
            sum.setBackground(Color.GREEN);
        }
        //fram.paintComponents(fram.getGraphics());
        Thread.sleep((long) timeDelaySlider.getValue());
        sum.setBackground(defColor);
        val1.setText("");
        val2.setText("");
        sum.setText("");
        // Paint to view
        //this.paintComponents(this.getGraphics());
        //Add auto scrolling to the bottom
        DefaultCaret caret = (DefaultCaret)textLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        textLog = new javax.swing.JTextArea();
        startTestingButton1 = new javax.swing.JButton();
        recordButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        testRobotButton = new javax.swing.JButton();
        roboSpeedSlider = new javax.swing.JSlider();
        roboSpeedMultiplierLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

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

        jLabel2.setText("1. Click on one of the linker buttons");

        jLabel3.setText("2. Click on the corresponding component");

        jLabel4.setText("3. Repeat for all other buttons");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Testing Tool 1.0");

        jLabel6.setText("Delay");

        textLog.setColumns(20);
        textLog.setRows(5);
        jScrollPane1.setViewportView(textLog);

        startTestingButton1.setText("END TESTING");
        startTestingButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startTestingButton1ActionPerformed(evt);
            }
        });

        recordButton.setText("Record");
        recordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recordButtonMouseClicked(evt);
            }
        });
        recordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");

        playButton.setText("Play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        testRobotButton.setText("Test with robot");
        testRobotButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testRobotButtonActionPerformed(evt);
            }
        });

        roboSpeedSlider.setMaximum(200);
        roboSpeedSlider.setMinimum(-50);
        roboSpeedSlider.setValue(0);
        roboSpeedSlider.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                roboSpeedSliderMouseDragged(evt);
            }
        });

        roboSpeedMultiplierLabel.setText("x1.00");

        jLabel8.setText("Robot Speed");

        jToggleButton1.setText("Robot help");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton1))
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(240, 240, 240)
                                        .addComponent(linkedVal1CB, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(selectFile, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(valField1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(valField2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(sumField, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(selectedCB, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(linkedSumCB, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(linkedOkCB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(linkedVal2CB, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(roboSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(roboSpeedMultiplierLabel)
                                        .addGap(0, 0, Short.MAX_VALUE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startTestingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(startTestingButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timeDelaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timeDelayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(recordButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(playButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(testRobotButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jToggleButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(23, 23, 23)
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
                            .addComponent(linkedOkCB)))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(selectFile)
                        .addComponent(selectedCB)
                        .addComponent(jLabel8))
                    .addComponent(roboSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roboSpeedMultiplierLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeDelayLabel)
                    .addComponent(timeDelaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(recordButton)
                        .addComponent(stopButton)
                        .addComponent(playButton)
                        .addComponent(testRobotButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startTestingButton1)
                    .addComponent(startTestingButton))
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
    
    private boolean testingNow = false;
    private void startTestingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startTestingButtonActionPerformed
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
        if (allStepsCompleted()) {
            testingNow = true;
            Thread test = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < expectedSums.size(); i++) {
                        if (!testingNow) break;
                        try {
                            doTest(vals1.get(i), vals2.get(i),
                                    expectedSums.get(i));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(
                                    TesterMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    testingNow = false;
                }
            });
            test.start();
            log("--- END TEST ---");
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

    private void startTestingButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startTestingButton1ActionPerformed
        testingNow = false;
    }//GEN-LAST:event_startTestingButton1ActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:
        if (allStepsCompleted()) {
            recordButton.setEnabled(true);
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void recordButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordButtonMouseClicked
        JButton self = (JButton) evt.getSource();
        if (allStepsCompleted()) {
            self.setEnabled(true);
        } else {
//            JOptionPane.showMessageDialog(null, "Complete all steps first.", "Can't record", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_recordButtonMouseClicked
    
    private int clickCounter = 0;
    private int moveCounter = 0;
    private long startTime = 0;
    private boolean recorded = false;
    private void recordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordButtonActionPerformed
        log("Started recording.....");
        recorded = true;
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        roboActions.clear();
        roboDelays.clear();
        roboDelays.add(System.currentTimeMillis());
        startTime = System.currentTimeMillis();
        
        AWTEventListener recordListener = new AWTEventListener() {
            
            private void resetRecord() {
                clickCounter = 0;
                moveCounter = 0;
                recordButton.setEnabled(true);
            }
            
            @Override
            public void eventDispatched(AWTEvent event) {
//                log(Integer.toString(roboActions.size()));
//                log(Integer.toString(roboDelays.size()));
                if (event.getID() == MouseEvent.MOUSE_PRESSED) {
                    roboActions.add(new RoboMouseClick("Click-"+clickCounter,
                            RoboAction.MOUSE_CLICK, event, System.currentTimeMillis()));
                    roboDelays.add(System.currentTimeMillis());
                    log("Click recorded, id: Click-"+clickCounter);
                    clickCounter++;
                } else if (event.getID() == MouseEvent.MOUSE_MOVED) {
                    roboActions.add(new RoboMouseMove("Move-"+moveCounter,
                            RoboAction.MOUSE_MOVE, event, System.currentTimeMillis()));
                    roboDelays.add(System.currentTimeMillis());
                    //log("Moved");
                    moveCounter++;
                }
                // max time
                if (System.currentTimeMillis() - roboDelays.get(0) > TimeUnit.SECONDS.toMillis(30)) {
                    log("Recording stopped... 30 seconds have passed.");
                    tk.removeAWTEventListener(this);
                } else if (clickCounter > 5) {
                    resetRecord();
                    log("Recording stopped... Max of 5 clicks have been used.");
                    tk.removeAWTEventListener(this);
                    stopButton.setEnabled(false);
                    playButton.setEnabled(true);
                } else if (event.getSource() == stopButton) {
                    log("Recording stoppped... "
                            + "hint: recording stops when you hover on 'Stop'");
                    resetRecord();
                    tk.removeAWTEventListener(this);
                    stopButton.setEnabled(false);
                    playButton.setEnabled(true);
                }
            }
        };
        
        tk.addAWTEventListener(recordListener, 
                AWTEvent.MOUSE_MOTION_EVENT_MASK|AWTEvent.MOUSE_EVENT_MASK);
    }//GEN-LAST:event_recordButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        recordButton.setEnabled(false);
        try {
            Robot robert = new Robot();
            Thread tx = new Thread(new Runnable() {
                @Override
                public void run() {
                    robert.setAutoWaitForIdle(true);
                    long lastTime = startTime;
                    for (RoboAction act : roboActions) {
                        //log("Running...");
                        int roboDelay = ((int) act.getTimeStamp() - (int) lastTime);
                        float roboSpeedMultiplier = (float) 200 - (float) roboSpeedSlider.getValue();
                        roboSpeedMultiplier /= 200;
                        roboDelay = (int) (roboDelay*(roboSpeedMultiplier));
                        robert.delay(roboDelay);
                        act.doAction(robert);
                        lastTime = act.getTimeStamp();
                    }
                    recordButton.setEnabled(true);
                }
            });
            tx.start();
        } catch (AWTException ex) {
            Logger.getLogger(TesterMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_playButtonActionPerformed
    
    private void checkTest(int expected, int output) {
        log(expected+" == "+output);
        if (expected == output) {
            log("PASSED");
        } else {
            Integer diff = expected-output;
            log("FAILED - DIFF = "+diff.toString());
        }
    }
    
    private int testClickCount = 0;
    private void doTestRobot(Integer num1, Integer num2, Integer exptected) throws InterruptedException {
        recordButton.setEnabled(false);
        try {
            Robot robert = new Robot();
            Thread tx = new Thread(new Runnable() {
                @Override
                public void run() {
                    robert.setAutoWaitForIdle(true);
                    long lastTime = startTime;
                    RoboAction last = null;
                    for (RoboAction act : roboActions) {
                        //log("Running...");
                        int roboDelay = ((int) act.getTimeStamp() - (int) lastTime);
                        float roboSpeedMultiplier = (float) 200 - (float) roboSpeedSlider.getValue();
                        roboSpeedMultiplier /= 200;
                        roboDelay = (int) (roboDelay*(roboSpeedMultiplier));
                        robert.delay(roboDelay);
                        act.doAction(robert);
                        if (act instanceof RoboMouseClick){
                            System.out.println("Robot clicked. "+testClickCount);
                            RoboTyper typer = new RoboTyper();
                            if (testClickCount < 2) {
                                System.out.println("Erasing..");
                                int counter = 5;
                                while (counter-- != 0) {
                                    robert.keyPress(KeyEvent.VK_BACK_SPACE);
                                    robert.keyRelease(KeyEvent.VK_BACK_SPACE);
                                }
                            }
                            switch (testClickCount) {
                                case 0: // after click in val1
                                    typer.type(num1.toString(), robert);
                                    break;
                                case 1: // after click in val2
                                    typer.type(num2.toString(), robert);
                                    break;
                                case 2: // after click on ok
                                    robert.delay(100);
                                    try {
                                        Integer output = Integer.parseInt(sum.getText());
                                        checkTest(exptected, output);
                                    } catch (NumberFormatException e) {
                                        break;
                                    }
                            }
                            testClickCount++;
                        }
                        lastTime = act.getTimeStamp();
                        last = act;
                    }
                    testClickCount = 0;
                    recordButton.setEnabled(true);
                }
            });
            tx.start();
            tx.join();
        } catch (AWTException ex) {
            Logger.getLogger(TesterMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean robotAlive = false;
    private void testRobotButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testRobotButtonActionPerformed
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
        log("--- TESTING WITH ROBOT INITIATED ---");
        testRobotButton.setEnabled(false);
        robotAlive = true;
        if (allStepsCompleted()) {
            Thread robotesting = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < expectedSums.size(); i++) {
                        try {
                            log("TESTING -- "+vals1.get(i)+" + "+vals2.get(i)+" = "+expectedSums.get(i));
                            doTestRobot(vals1.get(i), vals2.get(i), expectedSums.get(i));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(TesterMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    robotAlive = false;
                    log("--- TESTING WITH ROBOT ENDED ---");
                }
            });
            robotesting.start();
        } else {
//            JOptionPane.showMessageDialog(null, "Please complete "
//                    + "all steps first.", "Incomplete.",
//                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_testRobotButtonActionPerformed

    private void roboSpeedSliderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_roboSpeedSliderMouseDragged
        float roboSpeedMultiplier = (float) 200 + (float) roboSpeedSlider.getValue();
        roboSpeedMultiplier /= 200;
        String label = String.format("x%.2f", roboSpeedMultiplier);
        roboSpeedMultiplierLabel.setText(label);
    }//GEN-LAST:event_roboSpeedSliderMouseDragged

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if (robotAlive || !allStepsCompleted() || !recorded) {
            testRobotButton.setEnabled(false);
        } else if (allStepsCompleted() && recorded) {
            testRobotButton.setEnabled(true);
        }
    }//GEN-LAST:event_formMouseMoved

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        HelpMenu hm = new HelpMenu();
        hm.setVisible(true);
        hm.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    }//GEN-LAST:event_jToggleButton1ActionPerformed

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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JCheckBox linkedOkCB;
    private javax.swing.JCheckBox linkedSumCB;
    private javax.swing.JCheckBox linkedVal1CB;
    private javax.swing.JCheckBox linkedVal2CB;
    private javax.swing.JButton okButton;
    private javax.swing.JButton playButton;
    private javax.swing.JButton recordButton;
    private javax.swing.JLabel roboSpeedMultiplierLabel;
    private javax.swing.JSlider roboSpeedSlider;
    private javax.swing.JButton selectFile;
    private javax.swing.JCheckBox selectedCB;
    private javax.swing.JButton startTestingButton;
    private javax.swing.JButton startTestingButton1;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton sumField;
    private javax.swing.JButton testRobotButton;
    private javax.swing.JTextArea textLog;
    private javax.swing.JLabel timeDelayLabel;
    private javax.swing.JSlider timeDelaySlider;
    private javax.swing.JButton valField1;
    private javax.swing.JButton valField2;
    // End of variables declaration//GEN-END:variables
}
