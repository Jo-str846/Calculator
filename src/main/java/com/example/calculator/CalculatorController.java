package com.example.calculator;

import com.example.calculator.CalculatorOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CalculatorController {
    @FXML private Label mainDisplay, historyDisplay;
    @FXML private Label hexDisplay, decDisplay, octDisplay, binDisplay;
    @FXML Button btn;

    private String currentInput = "";
    private long firstNumber = 0;
    private String operator = "";
    private boolean startNewInput = true;

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
            // Remove the last character
            currentInput = currentInput.substring(0, currentInput.length() - 1);

            // If we deleted everything, show 0
            if (currentInput.isEmpty()) {
                currentInput = "0";
                startNewInput = true;
            }

            mainDisplay.setText(currentInput);
            updateBases(); // Keep all base displays in sync
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
            mainDisplay.setText("Error"); // Simple exception handling
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
        // Reset all internal state
        currentInput = "";
        firstNumber = 0;
        operator = "";
        startNewInput = true;

        // Reset all UI labels
        mainDisplay.setText("0");
        historyDisplay.setText("");
        hexDisplay.setText("0");
        decDisplay.setText("0");
        octDisplay.setText("0");
        binDisplay.setText("0");
    }
    }
