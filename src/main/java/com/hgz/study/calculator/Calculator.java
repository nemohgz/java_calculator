package com.hgz.study.calculator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

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
    private  enum DisplayState {BEGINSTATE,ZEROPREFIX,NORMALSTATE};
    private  DisplayState displayState;
    private  boolean floatPointState = false;
    public DisplayPannel() {
        display = new JTextField(40);
        displayState = DisplayState.BEGINSTATE;
        init();
    }
    public  void appendText(String text) {
        if (displayState == DisplayState.BEGINSTATE
                && text.charAt(0) >= '0' && text.charAt(0) <= '9') {
                if(text.charAt(0) == '0' && this.getText().length() == 0) {
                    displayState = DisplayState.ZEROPREFIX;
                } else {
                    displayState = DisplayState.NORMALSTATE;
                }
        } else if (displayState == DisplayState.ZEROPREFIX
                && (text.charAt(0) < '0' || text.charAt(0) > '9')) {
                displayState = DisplayState.BEGINSTATE;
                if (text.charAt(0) == '.') {
                    floatPointState = true;
                }
        } else if (displayState == DisplayState.NORMALSTATE){
               if (text.charAt(0) == '.' && floatPointState) {
                       return;
               }
               if (text.charAt(0) < '0' || text.charAt(0) > '9') {
                   displayState = DisplayState.BEGINSTATE;
                   if (text.charAt(0) == '.') {
                       floatPointState = true;
                   } else {
                       floatPointState = false;
                   }
               }
        } else {
            return;
        }
        this.setText(this.getText() + text);
    }
    public void setText(String text){
        this.display.setText(text);
    }
    public String getText(){
        return display.getText();
    }
    public void clearText() {
        displayState = DisplayState.BEGINSTATE;
        floatPointState = false;
        display.setText("");
    }
    private void init() {
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
        ActionListener insertAction = e -> displayPanel.appendText(e.getActionCommand());
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
        Stack<Double> dataStack = new Stack<>();
        Stack<Character>  opStack = new Stack<>();
        int pre = 0,cur = 0;
        for (; cur < cmd.length(); cur++) {
            if (isOperation(cmd.charAt(cur))) {

            }
        }
        if (cur != pre) dataArray.add(cmd.substring(pre,cur));
        System.out.println(dataArray.toString());
        return "error";
    }
    private boolean isOperation(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/'  ) {
            return true;
        } else {
            return false;
        }
    }
}
