package view;

import model.CalculatorEngine;
import model.EquationMode;
import model.OptionMenu;

import javax.swing.*;
import java.awt.*;

public class CalculatorGUI extends JFrame {
    private DisplayPanel displayPanel;
    private ButtonPanel buttonPanel;
    private CalculatorEngine engine;
    private JLabel shiftIndicator;
    private JLabel alphaIndicator;

    public CalculatorGUI() {
        engine = new CalculatorEngine();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("CASIO fx-580VN X");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel displayContainer = new JPanel(new BorderLayout());
        displayContainer.setBackground(Color.BLACK);

        JPanel indicatorsPanel = createIndicatorsPanel();
        displayContainer.add(indicatorsPanel, BorderLayout.NORTH);

        displayPanel = new DisplayPanel();
        displayContainer.add(displayPanel, BorderLayout.CENTER);

        add(displayContainer, BorderLayout.CENTER);

        buttonPanel = new ButtonPanel(this);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        updateDisplay();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setBackground(Color.BLACK);
        header.setPreferredSize(new Dimension(350, 40));
        header.setLayout(new BorderLayout());

        JLabel casioLabel = new JLabel("CASIO");
        casioLabel.setFont(new Font("Arial", Font.BOLD, 16));
        casioLabel.setForeground(Color.WHITE);
        casioLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel modelLabel = new JLabel("fx-580VN X");
        modelLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        modelLabel.setForeground(Color.WHITE);
        modelLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        header.add(casioLabel, BorderLayout.WEST);
        header.add(modelLabel, BorderLayout.EAST);
        return header;
    }

    private JPanel createIndicatorsPanel() {
        JPanel indicators = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        indicators.setBackground(new Color(180, 200, 180));
        indicators.setPreferredSize(new Dimension(350, 20));

        shiftIndicator = new JLabel("S"); shiftIndicator.setFont(new Font("Arial", Font.BOLD, 10)); shiftIndicator.setForeground(new Color(50, 70, 50)); shiftIndicator.setVisible(false);
        alphaIndicator = new JLabel("A"); alphaIndicator.setFont(new Font("Arial", Font.BOLD, 10)); alphaIndicator.setForeground(new Color(50, 70, 50)); alphaIndicator.setVisible(false);

        indicators.add(shiftIndicator);
        indicators.add(alphaIndicator);
        return indicators;
    }

    public void handleButtonPress(String buttonText) {
        engine.processInput(buttonText);
        updateDisplay();
    }

    // --- HÀM UPDATE DISPLAY ĐÃ SỬA LỖI HIỂN THỊ ---
    private void updateDisplay() {
        if (!engine.isPoweredOn()) {
            displayPanel.updateDisplay(""); displayPanel.updateResult(""); displayPanel.setMode("OFF");
            shiftIndicator.setVisible(false); alphaIndicator.setVisible(false);
            return;
        }

        OptionMenu optMenu = engine.getOptionMenu();
        if (optMenu.getCurrentMode() != OptionMenu.Mode.NORMAL) {
            displayPanel.updateDisplay(optMenu.getMenuDisplay());
            displayPanel.updateResult("");
            return;
        }

        EquationMode equationMode = engine.getEquationMode();
        EquationMode.Mode mode = equationMode.getCurrentMode();

        if (mode == EquationMode.Mode.NORMAL) {
            displayPanel.updateDisplay(engine.getDisplayText());
            if (engine.hasResult()) displayPanel.updateResult(engine.getResultText());
            else displayPanel.updateResult("");
            displayPanel.setMode(engine.getAngleMode());

        } else if (mode == EquationMode.Mode.MENU) {
            displayPanel.updateDisplay(equationMode.getCurrentPrompt());
            displayPanel.updateResult("");
            displayPanel.setMode("MENU");

        } else if (mode == EquationMode.Mode.EQUATION_MENU) {
            String display = engine.getDisplayText();
            // Hiển thị tiếng Việt trong menu
            if (display.isEmpty() || display.equals("|")) displayPanel.updateDisplay("1:Hệ phương trình\n2:Phương trình đa thức");
            else displayPanel.updateDisplay(display);
            displayPanel.updateResult(""); displayPanel.setMode("EQN");

        } else if (mode == EquationMode.Mode.POLYNOMIAL || mode == EquationMode.Mode.LINEAR_SYSTEM) {
            String prompt = equationMode.getCurrentPrompt();
            String currentInput = equationMode.getCurrentInput();
            displayPanel.updateDisplay(prompt + "\n" + currentInput);
            if (engine.hasResult()) {
                String eqStr = equationMode.getEquationDisplay();
                if (!eqStr.isEmpty()) displayPanel.updateDisplay(eqStr);
                displayPanel.updateResult(engine.getResultText());
            } else displayPanel.updateResult("");
            displayPanel.setMode("EQN");

        } else if (mode.toString().startsWith("TABLE")) {
            // FIX LỖI Ở ĐÂY: Đổi chỗ hiển thị
            if (engine.hasResult()) {
                // Nội dung bảng (nhiều dòng) đưa lên màn hình chính
                displayPanel.updateDisplay(engine.getResultText());
                // Tiêu đề/Trạng thái đưa xuống dòng kết quả
                displayPanel.updateResult("Bảng giá trị");
            } else {
                String prompt = equationMode.getCurrentPrompt();
                String input = equationMode.getCurrentInput();
                displayPanel.updateDisplay(prompt + input);
                displayPanel.updateResult("");
            }
            displayPanel.setMode("TBL");

        } else if (mode == EquationMode.Mode.BASEN_MAIN) {
            // FIX LỖI Ở ĐÂY: Đổi chỗ hiển thị Base-N
            if (engine.hasResult()) {
                displayPanel.updateDisplay(engine.getResultText()); // Kết quả nhiều dòng
                displayPanel.updateResult("Kết quả đổi cơ số");
            } else {
                String prompt = equationMode.getCurrentPrompt();
                String input = equationMode.getCurrentInput();
                displayPanel.updateDisplay(prompt + "\n" + input);
                displayPanel.updateResult("");
            }
            displayPanel.setMode("DEC");
        }

        shiftIndicator.setVisible(engine.isShiftMode());
        alphaIndicator.setVisible(engine.isAlphaMode());
    }

    public CalculatorEngine getEngine() { return engine; }
    public DisplayPanel getDisplayPanel() { return displayPanel; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorGUI calculator = new CalculatorGUI();
            calculator.setVisible(true);
        });
    }
}