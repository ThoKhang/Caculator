package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {
    private CalculatorGUI parent;

    // Bảng màu
    private final Color DARK_GRAY = new Color(50, 50, 50);
    private final Color LIGHT_GRAY = new Color(90, 90, 90);
    private final Color WHITE = new Color(240, 240, 240);
    private final Color BLUE = new Color(50, 130, 210);
    private final Color ORANGE = new Color(255, 150, 40);
    private final Color YELLOW = new Color(255, 200, 80);
    private final Color PINK = new Color(255, 100, 150);
    private final Color NAV_GRAY = new Color(70, 70, 70);

    public ButtonPanel(CalculatorGUI parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        createButtons();
    }

    private void createButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // --- ROW 0: SHIFT, ALPHA, LÊN, MENU, ON ---
        gbc.gridy = 0;

        // 1. SHIFT
        gbc.gridx = 0;
        add(createButton("SHIFT", DARK_GRAY, YELLOW), gbc);

        // 2. ALPHA
        gbc.gridx = 1;
        add(createButton("ALPHA", DARK_GRAY, PINK), gbc);

        // 3. LÊN (▲) - Nằm giữa
        gbc.gridx = 2;
        add(createButton("▲", NAV_GRAY, Color.WHITE), gbc);

        // 4. MENU (Đưa lên đây thay cho nút Sang Phải cũ)
        gbc.gridx = 3;
        add(createButton("MENU", DARK_GRAY, Color.WHITE), gbc);

        // 5. ON
        gbc.gridx = 4;
        add(createButton("ON", new Color(60, 40, 40), ORANGE), gbc);


        // --- ROW 1: OPTN, TRÁI, XUỐNG, PHẢI, CALC ---
        gbc.gridy = 1;

        // 1. OPTN
        gbc.gridx = 0;
        add(createButton("OPTN", DARK_GRAY, Color.WHITE), gbc);

        // 2. TRÁI (◄)
        gbc.gridx = 1;
        add(createButton("◄", NAV_GRAY, Color.WHITE), gbc);

        // 3. XUỐNG (▼)
        gbc.gridx = 2;
        add(createButton("▼", NAV_GRAY, Color.WHITE), gbc);

        // 4. PHẢI (►) (Đưa xuống đây để cùng hàng với Trái và Xuống)
        gbc.gridx = 3;
        add(createButton("►", NAV_GRAY, Color.WHITE), gbc);

        // 5. CALC
        gbc.gridx = 4;
        add(createButton("CALC", DARK_GRAY, Color.WHITE), gbc);


        // --- CÁC HÀNG CÒN LẠI GIỮ NGUYÊN ---

        // ROW 2
        String[][] row2 = {{"□/□", "√□", "x²", "x□", "log□"}};
        addButtonRow(row2[0], 2, gbc, DARK_GRAY, Color.WHITE);

        // ROW 3
        String[][] row3 = {{"ln", "∫dx", "x⁻¹", "sin", "cos"}};
        addButtonRow(row3[0], 3, gbc, DARK_GRAY, Color.WHITE);

        // ROW 4
        String[][] row4 = {{"(-)", "°'''", "Abs", "tan", "d/dx"}};
        addButtonRow(row4[0], 4, gbc, DARK_GRAY, Color.WHITE);

        // ROW 5
        String[][] row5 = {{"STO", "ENG", "(", ")", "S⇔D"}};
        addButtonRow(row5[0], 5, gbc, DARK_GRAY, Color.WHITE);

        // ROW 6
        String[][] row6 = {{"x", "□", ",", "M-", "M+"}};
        addButtonRow(row6[0], 6, gbc, DARK_GRAY, Color.WHITE);

        // ROW 7
        String[] labels7 = {"7", "8", "9", "DEL", "AC"};
        Color[] bgColors7 = {WHITE, WHITE, WHITE, BLUE, ORANGE};
        Color[] fgColors7 = {Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE};
        addButtonRowCustomColors(labels7, 7, gbc, bgColors7, fgColors7);

        // ROW 8
        String[] labels8 = {"4", "5", "6", "×", "÷"};
        Color[] bgColors8 = {WHITE, WHITE, WHITE, LIGHT_GRAY, LIGHT_GRAY};
        Color[] fgColors8 = {Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE};
        addButtonRowCustomColors(labels8, 8, gbc, bgColors8, fgColors8);

        // ROW 9
        String[] labels9 = {"1", "2", "3", "+", "-"};
        Color[] bgColors9 = {WHITE, WHITE, WHITE, LIGHT_GRAY, LIGHT_GRAY};
        Color[] fgColors9 = {Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE};
        addButtonRowCustomColors(labels9, 9, gbc, bgColors9, fgColors9);

        // ROW 10
        String[] labels10 = {"0", ".", "×10ˣ", "Ans", "="};
        Color[] bgColors10 = {WHITE, WHITE, LIGHT_GRAY, LIGHT_GRAY, LIGHT_GRAY};
        Color[] fgColors10 = {Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE};
        addButtonRowCustomColors(labels10, 10, gbc, bgColors10, fgColors10);
    }

    private void addButtonRow(String[] labels, int row, GridBagConstraints gbc, Color bgColor, Color fgColor) {
        for (int col = 0; col < labels.length; col++) {
            gbc.gridx = col;
            gbc.gridy = row;
            add(createButton(labels[col], bgColor, fgColor), gbc);
        }
    }

    private void addButtonRowCustomColors(String[] labels, int row, GridBagConstraints gbc, Color[] bgColors, Color[] fgColors) {
        for (int col = 0; col < labels.length; col++) {
            gbc.gridx = col;
            gbc.gridy = row;
            add(createButton(labels[col], bgColors[col], fgColors[col]), gbc);
        }
    }

    private JButton createButton(String text, Color bgColor, Color fgColor) {
        ModernButton button = new ModernButton(text, bgColor, fgColor);
        button.setPreferredSize(new Dimension(60, 42));

        if (text.matches("[0-9]") || text.equals(".")) {
            button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        } else if (text.equals("DEL") || text.equals("AC")) {
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        } else if ("×÷+-=".contains(text)) {
            button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        } else if ("▲▼◄►".contains(text)) {
            button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        } else {
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }

        ActionListener listener = e -> parent.handleButtonPress(text);
        button.addActionListener(listener);

        return button;
    }
}