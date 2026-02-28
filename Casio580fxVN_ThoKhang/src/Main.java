// CalculatorMain.java
import view.CalculatorGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorGUI calculator = new CalculatorGUI();
            calculator.setVisible(true);
        });
    }
}