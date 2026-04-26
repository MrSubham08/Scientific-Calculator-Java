import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    private JFrame frame;
    private JTextField displayField;
    private JTextField expressionField;
    private JTextArea historyArea;

    private String operator = "";
    private double num1 = 0;
    private boolean startNewNumber = true;

    public Main() {

        frame = new JFrame("Scientific Calculator");
        frame.setSize(500, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        // ===== Expression Field =====
        expressionField = new JTextField();
        expressionField.setEditable(false);
        expressionField.setBackground(new Color(30, 30, 30));
        expressionField.setForeground(Color.LIGHT_GRAY);
        expressionField.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        // ===== Display Field =====
        displayField = new JTextField();
        displayField.setFont(new Font("Arial", Font.BOLD, 20));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setBackground(new Color(20, 20, 20));
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(expressionField, BorderLayout.NORTH);
        topPanel.add(displayField, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);

        // ===== Buttons Panel =====
        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5));
        panel.setBackground(new Color(30, 30, 30));

        String[] buttons = {
                "7","8","9","/",
                "4","5","6","*",
                "1","2","3","-",
                "0",".","=","+",
                "C","√","x²","^"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setBackground(new Color(50, 50, 50));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setFocusPainted(false);

            btn.addActionListener(e -> handleButton(text));
            panel.add(btn);
        }

        frame.add(panel, BorderLayout.CENTER);

        // ===== History Area =====
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(20, 20, 20));
        historyArea.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(historyArea);

        // ===== Clear History Button =====
        JButton clearHistoryBtn = new JButton("Clear History");
        clearHistoryBtn.setBackground(new Color(70, 70, 70));
        clearHistoryBtn.setForeground(Color.WHITE);
        clearHistoryBtn.setFocusPainted(false);

        clearHistoryBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Clear all history?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                historyArea.setText("");
            }
        });

        // ===== History Panel =====
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setPreferredSize(new Dimension(170, 0));
        historyPanel.add(scroll, BorderLayout.CENTER);
        historyPanel.add(clearHistoryBtn, BorderLayout.SOUTH);

        frame.add(historyPanel, BorderLayout.EAST);

        // ===== Keyboard Support =====
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();

                if (Character.isDigit(key) || key == '.') {
                    handleButton(String.valueOf(key));
                }

                if (key == '+') handleButton("+");
                if (key == '-') handleButton("-");
                if (key == '*') handleButton("*");
                if (key == '/') handleButton("/");

                if (key == '\n') handleButton("="); // Enter
                if (key == 'c' || key == 'C') handleButton("C");
            }
        });

        frame.setFocusable(true);
        frame.requestFocus();

        frame.setVisible(true);
    }

    // ===== Logic =====
    private void handleButton(String text) {
        try {
            switch (text) {

                case "+":
                case "-":
                case "*":
                case "/":
                case "^":

                    if (displayField.getText().isEmpty()) return;

                    num1 = Double.parseDouble(displayField.getText());
                    operator = text;
                    expressionField.setText(num1 + " " + operator);
                    startNewNumber = true;
                    break;

                case "=":

                    if (operator.isEmpty() || displayField.getText().isEmpty()) return;

                    double num2 = Double.parseDouble(displayField.getText());
                    double result = 0;

                    switch (operator) {
                        case "+": result = num1 + num2; break;
                        case "-": result = num1 - num2; break;
                        case "*": result = num1 * num2; break;

                        case "/":
                            if (num2 == 0) {
                                displayField.setText("Error");
                                return;
                            }
                            result = num1 / num2;
                            break;

                        case "^":
                            result = Math.pow(num1, num2);
                            break;
                    }

                    displayField.setText(String.valueOf(result));

                    String fullExp = num1 + " " + operator + " " + num2 + " = " + result;
                    expressionField.setText(fullExp);
                    historyArea.append(fullExp + "\n");

                    operator = "";
                    startNewNumber = true;
                    break;

                case "√":
                    double val = Double.parseDouble(displayField.getText());
                    double sqrt = Math.sqrt(val);

                    displayField.setText(String.valueOf(sqrt));

                    String exp1 = "√" + val + " = " + sqrt;
                    expressionField.setText(exp1);
                    historyArea.append(exp1 + "\n");
                    startNewNumber = true;
                    break;

                case "x²":
                    double sq = Double.parseDouble(displayField.getText());
                    double sqr = sq * sq;

                    displayField.setText(String.valueOf(sqr));

                    String exp2 = sq + "² = " + sqr;
                    expressionField.setText(exp2);
                    historyArea.append(exp2 + "\n");
                    startNewNumber = true;
                    break;

                case "C":
                    displayField.setText("");
                    expressionField.setText("");
                    operator = "";
                    num1 = 0;
                    startNewNumber = true;
                    break;

                default:
                    if (startNewNumber) {
                        displayField.setText(text);
                        startNewNumber = false;
                    } else {
                        displayField.setText(displayField.getText() + text);
                    }
            }

        } catch (Exception e) {
            displayField.setText("Error");
            startNewNumber = true;
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}