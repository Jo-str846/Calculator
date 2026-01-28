package com.example.calculator;

import com.example.calculator.CalculatorOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.List;
import java.util.ArrayList;

public class CalculatorController {
    @FXML private Label mainDisplay, historyDisplay;
    @FXML private Label hexDisplay, decDisplay, octDisplay, binDisplay;
    @FXML Button btn;
    @FXML Button btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    @FXML Button btnA, btnB, btnC, btnD, btnE, btnF;
    @FXML Button btnHexMode, btnDecMode, btnOctMode, btnBinMode;
    List<Button> hexButtons;

    private String currentInput = "";
    private long firstNumber = 0;
    private String operator = "";
    private boolean startNewInput = true;

    public enum NumberBase {
        BINARY(2), OCTAL(8), DECIMAL(10), HEXADECIMAL(16);
        public final int value;
        NumberBase(int value){this.value = value;}
    }
    private NumberBase currentMode = NumberBase.DECIMAL;

    private void updateKeyPad() {
        boolean isHex = (currentMode == NumberBase.HEXADECIMAL);
        for(Button btn : hexButtons) {
            btn.setDisable(!isHex);
            btn.setOpacity(isHex ? 1.0:0.4);
        }
    }

    private void applyMode() {
        boolean isHex = (currentMode == NumberBase.HEXADECIMAL);
        boolean isOct = (currentMode == NumberBase.OCTAL);
        boolean isBin = (currentMode == NumberBase.BINARY);

        btnA.setDisable(!isHex);
        btnA.setOpacity(isHex ? 1.0 : 0.3);
        btnB.setDisable(!isHex);
        btnB.setOpacity(isHex ? 1.0 : 0.3);
        btnC.setDisable(!isHex);
        btnC.setOpacity(isHex ? 1.0 : 0.3);
        btnD.setDisable(!isHex);
        btnD.setOpacity(isHex ? 1.0 : 0.3);
        btnE.setDisable(!isHex);
        btnE.setOpacity(isHex ? 1.0 : 0.3);
        btnF.setDisable(!isHex);
        btnF.setOpacity(isHex ? 1.0 : 0.3);

        boolean disable89 = (isOct || isBin);
        btn8.setDisable(disable89);
        btn8.setOpacity(isOct ? 0.3 : 1.0);
        btn9.setDisable(disable89);
        btn9.setOpacity(isOct ? 0.3 : 1.0);

        btn2.setDisable(isBin);
        btn2.setOpacity(isBin ? 0.3 : 1.0);
        btn3.setDisable(isBin);
        btn3.setOpacity(isBin ? 0.3 : 1.0);
        btn4.setDisable(isBin);
        btn4.setOpacity(isBin ? 0.3 : 1.0);
        btn5.setDisable(isBin);
        btn5.setOpacity(isBin ? 0.3 : 1.0);
        btn6.setDisable(isBin);
        btn6.setOpacity(isBin ? 0.3 : 1.0);
        btn7.setDisable(isBin);
        btn7.setOpacity(isBin ? 0.3 : 1.0);
    }

    @FXML
    private void handleHexMode() {
        currentMode = NumberBase.HEXADECIMAL;
        applyMode();
    }

    @FXML
    private void handleDecMode() {
        currentMode = NumberBase.DECIMAL;
        applyMode();
    }

    @FXML
    private void handleOctMode() {
        currentMode = NumberBase.OCTAL;
        applyMode();
    }

    @FXML
    private void handleBinMode() {
        currentMode = NumberBase.BINARY;
        applyMode();
    }

    @FXML
    public void onNumberClicked(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String digit = btn.getText();

        if (startNewInput) {
            currentInput = "";
            startNewInput = false;
        }

        if (currentInput.equals("0")) {
            currentInput = digit;
        } else {
            currentInput += digit;
        }
        mainDisplay.setText(currentInput);
        updateBases();
    }

    @FXML
    public void onOperatorClicked(ActionEvent event) {
        if(currentInput.isEmpty()) return;

        firstNumber = Long.parseLong(currentInput, 16);
        operator = ((Button) event.getSource()).getText();
        historyDisplay.setText(currentInput + " " + operator);
        startNewInput = true;
    }

    @FXML
    private void onDeleteClicked(ActionEvent event) {
        if (currentInput != null && currentInput.length() > 0) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);

            if (currentInput.isEmpty()) {
                currentInput = "0";
                startNewInput = true;
            }

            mainDisplay.setText(currentInput);
            updateBases();
        }
    }

    @FXML
    public void onEqualClicked(ActionEvent event) {
        if(operator.isEmpty() || currentInput.isEmpty()) return;

        try{
            long secondNumber = Long.parseLong(currentInput, 16);
            CalculatorOperator op = null;

            switch(operator){
                case "+": op = new CalculatorOperator.Addition(firstNumber, secondNumber); break;
                case "-": op = new CalculatorOperator.Subtraction(firstNumber,secondNumber); break;
                case "x": op = new CalculatorOperator.Multiplication(firstNumber, secondNumber); break;
                case "÷": op = new CalculatorOperator.Division(firstNumber, secondNumber); break;
            }

            if(op != null){
                long result = op.execute();
                historyDisplay.setText(firstNumber + " " + operator + " " + currentInput + " =");
                currentInput = Long.toHexString(result).toUpperCase();
                mainDisplay.setText(currentInput);
                updateBases();
            }
            startNewInput = true;
        } catch (Exception e) {
            mainDisplay.setText("Error");
        }
    }

    private void updateBases() {
        if (currentInput.isEmpty()) return;
        long val = Long.parseLong(currentInput, 16);
        hexDisplay.setText(currentInput.toUpperCase());
        decDisplay.setText(String.valueOf(val));
        octDisplay.setText(Long.toOctalString(val));
        binDisplay.setText(Long.toBinaryString(val));
        }

    @FXML
    private void onClearClicked(ActionEvent event) {
        currentInput = "";
        firstNumber = 0;
        operator = "";
        startNewInput = true;

        mainDisplay.setText("0");
        historyDisplay.setText("");
        hexDisplay.setText("0");
        decDisplay.setText("0");
        octDisplay.setText("0");
        binDisplay.setText("0");
    }

    @FXML
    private void handleModeChange(ActionEvent event) {
        Button clickedbutton = (Button) event.getSource();
        String modeText = clickedbutton.getText();

        switch (modeText) {
            case "HEX": currentMode = NumberBase.HEXADECIMAL; break;
            case "DEC": currentMode = NumberBase.DECIMAL; break;
            case "OCT": currentMode = NumberBase.OCTAL; break;
            case "BIN": currentMode = NumberBase.BINARY; break;
        }
        updateKeyPad();
    }
    }
