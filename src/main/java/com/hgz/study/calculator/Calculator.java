package com.hgz.study.calculator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * @Author: Administrator
 * @Date: 2017/12/18
 * @Time: 20:35
 * @Description:
 * @Motifiedy by:
 */
public class Calculator extends JFrame{
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 300;
    private static final String DEFAULT_TITLE = "JAVA Calcultor";
    public Calculator() {
        DisplayPannel display = new DisplayPannel();
        this.add(display,BorderLayout.NORTH);
        this.add(new CalPannel(display), BorderLayout.CENTER);
        init();
    }
    private void init() {
        //size
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        this.setSize(screenSize.width/4,screenSize.height/4);
        this.setLocationByPlatform(true);
        this.setTitle(DEFAULT_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }
    public static void main(String[] args) {
        Calculator cal = new Calculator();
    }
}
class DisplayPannel extends  JPanel {
    private  JTextField display;
    public DisplayPannel() {
        display = new JTextField(40);
        init();
    }
    public void setText(String text){
        this.display.setText(text);
    }
    public String getText(){
        return display.getText();
    }
    public void clearText() {
        display.setText("");
    }
    private void init() {
        //this.setLayout(new FlowLayout());
        //display.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT );
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        this.add(display);
        JButton clearButton = new JButton("clear");
        clearButton.addActionListener(new ClearAction());
        this.add(clearButton);
    }
    private class ClearAction implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            clearText();
        }
    }
}
class CalPannel extends  JPanel {
    private  DisplayPannel displayPanel;
    public CalPannel(DisplayPannel displayPanel) {
        this.displayPanel = displayPanel;
        init();
    }
    private void init() {
        this.setLayout(new GridLayout(4,4,5,10));
        ActionListener insertAction = new ActionListener() {
            private boolean numBefore = true;
            private boolean zeroBefore = false;
            public void actionPerformed(ActionEvent e) {
                char c = e.getActionCommand().charAt(0);
                String curText = displayPanel.getText();
                if (curText.isEmpty() && (c > '9' || c < '0')) return;
                if (c  == '0') {
                    if(zeroBefore == true) return;
                    zeroBefore  = true;
                }
                if (c > '9' || c < '0') {
                    if (numBefore == false ) return;
                    numBefore = false;
                    zeroBefore = false;
                } else {
                    numBefore = true;
                }
                displayPanel.setText(curText + e.getActionCommand());
            }
        };
        ActionListener calAction = e -> displayPanel.setText(calCMDString(displayPanel.getText()));
        addButton(new JButton("9"),insertAction);
        addButton(new JButton("8"),insertAction);
        addButton(new JButton("7"),insertAction);
        addButton(new JButton("+"),insertAction);
        addButton(new JButton("6"),insertAction);
        addButton(new JButton("5"),insertAction);
        addButton(new JButton("4"),insertAction);
        addButton(new JButton("-"),insertAction);
        addButton(new JButton("3"),insertAction);
        addButton(new JButton("2"),insertAction);
        addButton(new JButton("1"),insertAction);
        addButton(new JButton("*"),insertAction);
        addButton(new JButton("0"),insertAction);
        addButton(new JButton("."),insertAction);
        addButton(new JButton("="),calAction);
        addButton(new JButton("/"),insertAction);
    }
    private void addButton(JButton b, ActionListener action) {
        b.addActionListener(action);
        this.add(b);
    }
    private  String calCMDString(String cmd) {
        return "error";
    }
}
