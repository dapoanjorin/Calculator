// This project is not complete
// @author - Dapo


package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class Controller {


    @FXML
    private TextField bigField;

    @FXML
    private TextField smallField;

    @FXML
    private Button numButton;

    private char plus;
    private char minus;
    private char multiSign;
    private char divide;

    private boolean twoSigns;
    private boolean decimal;

    private double result;

    private Character infinitySign;

    public void initialize() {

        infinitySign = '\u221e';

        numButton.getStyleClass().add("num-button");

        bigField.setText("");
        smallField.setText("");

        plus = '+';
        minus = '-';
        multiSign = '*';
        divide = '/';


        twoSigns = false;

        // decimal flag to maintain order when printing .
        decimal = false;
        result = 0;
    }

    // Method for handling buttons
    @FXML
    public void handleButton(ActionEvent e) {

        Button button = (Button) e.getSource();

        if (isNumber(button.getText())) {
            twoSigns = false;
        }

        if (button.getText().equals("1")) {
            bigField.appendText("1");
        } else if (button.getText().equals("2")) {
            bigField.appendText("2");
        } else if (button.getText().equals("3")) {
            bigField.appendText("3");
        } else if (button.getText().equals("4")) {
            bigField.appendText("4");
        } else if (button.getText().equals("5")) {
            bigField.appendText("5");
        } else if (button.getText().equals("6")) {
            bigField.appendText("6");
        } else if (button.getText().equals("7")) {
            bigField.appendText("7");
        } else if (button.getText().equals("8")) {
            bigField.appendText("8");
        } else if (button.getText().equals("9")) {
            bigField.appendText("9");
        } else if (button.getText().equals("0")) {
            bigField.appendText("0");
        } else if (button.getText().equals(".")) {
            // Can also print . when the field is clear. It represents 0.
            if (bigField.getText().equals("")) {
                bigField.appendText(".");
            }
            printOperation('.');
            // Making sure you can't display an operator first. I will work on negative operator later
            // for now you can't display minus first
        } else if (button.getText().equals("+")) {
            if (!bigField.getText().equals(""))
                printOperation(plus);
        } else if (button.getText().equals("-")) {
            if (!bigField.getText().equals(""))
                printOperation(minus);
        } else if (button.getText().equals("/")) {
            if (!bigField.getText().equals(""))
                printOperation(divide);
        } else if (button.getText().equals("*")) {
            if (!bigField.getText().equals(""))
                printOperation(multiSign);
        }
        // To have the functionality of answer showing while still calculating
        // I will work on a formatted answer later
        // for now the answer displays in an int format
        // I will handle decimals later
        processResult();
    }

    // Delete last Character and process Result
    @FXML
    public void handleDelButton() {

        if (bigField.getText().length() >= 1) {
            smallField.clear();
            bigField.deleteText(bigField.getText().length() - 1, bigField.getText().length());
        }
        processResult();

    }

    @FXML
    public void handleAnswerButton() {
        if (Double.isInfinite(processResult())) {
            bigField.setText(Character.toString('\u221e'));
        } else {
            double a = processResult();
            bigField.setText(String.valueOf(a));
        }
        smallField.clear();
    }

    private void printOperation(char sign) {

        // Series of tests to avoid arbitrary input of signs
        // The previous character must be a digit before displaying any operator except minus of course
        String text = bigField.getText();
        char lastChar = text.charAt(text.length() - 1);
        switch (sign) {
            case '+': {
                if (lastChar != plus && lastChar != minus && lastChar != multiSign && lastChar != divide && lastChar!='.') {
                    bigField.appendText("+");
                } else {
                    if (twoSigns) {
                        bigField.setText(text.substring(0, text.length() - 2));
                        bigField.appendText("+");
                        twoSigns = false;
                    } else {
                        bigField.setText(bigField.getText(0, text.length() - 1));
                        bigField.appendText("+");
                    }
                }
            }
            decimal = false;
            break;
            case '-': {
                if (lastChar != plus && lastChar != minus && lastChar != divide && lastChar != divide && lastChar != multiSign && lastChar!='.') {
                    if (lastChar == multiSign || lastChar == divide) {
                        twoSigns = true;
                        bigField.appendText("-");
                    } else {
                        bigField.appendText("-");
                    }

                } else {
                    bigField.setText(bigField.getText(0, text.length() - 1));
                    bigField.appendText("-");
                }

            }
            decimal = false;
            break;
            case '/': {
                if ((lastChar != plus) && (lastChar != minus) && (lastChar != multiSign) && (lastChar != divide) && lastChar!='.') {
                    bigField.appendText("/");
                } else {
                    if (twoSigns) {
                        bigField.setText(text.substring(0, text.length() - 2));
                        bigField.appendText("/");
                        twoSigns = false;
                    } else {
                        bigField.setText(bigField.getText(0, text.length() - 1));
                        bigField.appendText("/");
                    }
                }
            }
            decimal = false;
            break;
            case '*': {
                if ((lastChar != plus) && (lastChar != minus) && (lastChar != multiSign) && (lastChar != divide) && lastChar!='.') {
                    bigField.appendText("*");
                } else {
                    if (twoSigns) {
                        bigField.setText(text.substring(0, text.length() - 2));
                        bigField.appendText("*");
                        twoSigns = false;
                    } else {
                        bigField.setText(bigField.getText(0, text.length() - 1));
                        bigField.appendText("*");
                    }
                }
            }
            decimal = false;
            break;
            case '.': {
                if (!decimal) {
                    if (lastChar != '.') {
                        bigField.appendText(".");
                    }
                    decimal = true;
                    break;
                }
            }
        }
    }

    private double processResult() {
        // Split bigField text into an array of Numbers and an array of signs
        // Operate on those numbers based on the signs using bodmas
        // I will handle operations on negatives later
        // I will also handle operations on decimals later

        if (!bigField.getText().equals("")) {
            String resultField = bigField.getText();

            String[] values = bigField.getText().split("\\+|-|/|\\*");
            ArrayList<Character> operations = new ArrayList<>();
            Character sign;
            ArrayList<Double> digits = new ArrayList<>();

            for (int i = 0; i < values.length; i++) {
                digits.add(Double.parseDouble(values[i]));
            }

            for (int i = 0; i < resultField.length(); i++) {
                sign = resultField.charAt(i);
                if (!Character.isDigit(sign) && sign != '.') {
                    operations.add(sign);
                }
            }

            return processAnswer(digits, operations);

        }
        return 0;
    }

    private double processAnswer(ArrayList<Double> values, ArrayList<Character> signs) {

        char lastChar = bigField.getText().charAt(bigField.getText().length() - 1);
        if (Character.isDigit(lastChar)) {
            A:
            for (int i = 0; i < signs.size(); i++) {
                if (signs.get(i) == divide) {
                    signs.remove(i);
                    System.out.println(values.get(i + 1));
                    double x = values.get(i + 1);
                    values.set(i, (values.get(i) / x));
                    values.remove(i + 1);
                    i--;
                    continue A;


                }
            }

            B:
            for (int i = 0; i < signs.size(); i++) {
                if (signs.get(i) == multiSign) {
                    signs.remove(i);
                    values.set(i, (values.get(i) * values.get(i + 1)));
                    values.remove(i + 1);
                    i--;
                    continue B;
                }
            }

            for (int i = 0; i < signs.size(); i++) {
                if (signs.get(i) == plus) {
                    signs.remove(i);
                    System.out.println(i + 1);
                    System.out.print(values.get(i) + ", " + values.get(i + 1));
                    values.set(i, (values.get(i) + values.get(i + 1)));
                    values.remove(i + 1);
                    i--;
                    continue;
                }
            }

            for (int i = 0; i < signs.size(); i++) {
                if (signs.get(i) == minus) {
                    signs.remove(i);
                    values.set(i, (values.get(i) - values.get(i + 1)));
                    values.remove(i + 1);
                    i--;
                    continue;
                }
            }
            double a = values.get(0);
            if (Double.isInfinite(values.get(0))) {
                smallField.setText(Character.toString('\u221e'));
            } else {
                smallField.setText(String.valueOf(a));
//                smallField.setText(Double.toString(values.get(0)));
            }
            result = values.get(0);
            return result;
        }

       return 0;
    }

    private boolean isNumber(String s) {

        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
