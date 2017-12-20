package com.hgz.study.calculator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

import javax.management.RuntimeOperationsException;
import javax.swing.*;

/**
 * @Author: Administrator
 * @Date: 2017/12/18
 * @Time: 20:35
 * @Description:
 * @Motifiedy by:
 */
public class Calculator extends JFrame{
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
    private String calCMDString(String cmd) {
        String calResult;
        try{
            calResult = calCMDString0(cmd);
        } catch (ArithmeticException e) {
            calResult = "error";
            System.out.println(e.toString());
        }
        return  calResult;
    }
    private  String calCMDString0(String cmd) throws ArithmeticException{
        Stack<Double> dataStack = new Stack<>();
        Stack<Character>  opStack = new Stack<>();
        ArrayList<Double> data = new ArrayList<>();
        ArrayList<Character> op = new ArrayList<>();
        if (isOperationCharacter(cmd.charAt(cmd.length()-1))) {
            cmd = cmd.substring(0,cmd.length());
        }
        int pre = 0,cur = 0;
        for (; cur < cmd.length(); cur++) {
            if (isOperationCharacter(cmd.charAt(cur))) {
                data.add(Double.parseDouble(cmd.substring(pre,cur)));
                pre = cur+1;
                op.add(cmd.charAt(cur));
            }
        }
        if (cur != pre) {
            data.add(Double.parseDouble(cmd.substring(pre,cur)));
        } else {
            op.remove(op.size()-1);
        }
        dataStack.push(data.get(0));
        for (int i = 0,j = 1; i < op.size() ; i++) {
            if (opStack.isEmpty()) {
                opStack.push(op.get(i));
                dataStack.push(data.get(j++));
            } else {
                int priority = compareOperatPriority(op.get(i), opStack.peek());
                if  (priority > 0) {
                    double top = compute(dataStack.peek(),data.get(j++),op.get(i));
                    dataStack.pop();
                    dataStack.push(top);
                } else {
                    double a = dataStack.peek();
                    dataStack.pop();
                    double b = dataStack.peek();
                    dataStack.pop();
                    dataStack.push(compute(b, a, opStack.peek()));
                    dataStack.push(data.get(j++));
                    opStack.pop();
                    opStack.push(op.get(i));
                }
            }
        }
        if (!opStack.isEmpty()) {
            double a = dataStack.peek();
            dataStack.pop();
            double b = dataStack.peek();
            dataStack.pop();
            dataStack.push(compute(b, a, opStack.peek()));
        }
        String calResult = dataStack.peek().toString();
        if(calResult.indexOf(".") > 0){
            calResult = calResult.replaceAll("0+?$", "");
            calResult = calResult.replaceAll("[.]$", "");
        }
        return  calResult;
    }
    private boolean isOperationCharacter(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/'  ) {
            return true;
        } else {
            return false;
        }
    }
    private double compute(Double a, Double b, Character op) throws   ArithmeticException{
        if ( op == '+') {
            return  a + b;
        } else if (op == '-') {
            return  a - b;
        } else if (op == '*') {
            return  a * b;
        } else {
            if (b == 0) throw new ArithmeticException("divide by zero");
            return  a / b;
        }
    }
    private int compareOperatPriority(char a, char b) {
        if ( a == '+' || a == '-') {
            if (b == '+' || b == '-') {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (b == '+' || b == '-') {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
