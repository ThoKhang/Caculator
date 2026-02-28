package view;

import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel {
    private JTextArea inputDisplay;
    private JLabel resultDisplay;
    private JLabel modeLabel;

    // Màu LCD thực tế hơn (Xanh rêu nhạt pha chút xám)
    private final Color LCD_BG = new Color(230, 240, 230);
    private final Color TEXT_COLOR = new Color(20, 20, 20);

    public DisplayPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30)); // Viền ngoài tối màu
        setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));

        // Panel màn hình chính với hiệu ứng lún xuống (Bevel)
        JPanel lcdScreen = new JPanel(new BorderLayout());
        lcdScreen.setBackground(LCD_BG);
        lcdScreen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED), // Hiệu ứng lún
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Khu vực nhập liệu
        inputDisplay = new JTextArea(3, 20);
        inputDisplay.setEditable(false);
        inputDisplay.setBackground(LCD_BG);
        inputDisplay.setForeground(TEXT_COLOR);
        // Dùng font Consolas hoặc Monospaced cho giống máy tính
        inputDisplay.setFont(new Font("Consolas", Font.PLAIN, 16));
        inputDisplay.setText("");
        inputDisplay.setLineWrap(true);
        inputDisplay.setWrapStyleWord(false);

        // Khu vực kết quả
        resultDisplay = new JLabel("0", SwingConstants.RIGHT);
        resultDisplay.setFont(new Font("Consolas", Font.BOLD, 28)); // Số to rõ
        resultDisplay.setForeground(TEXT_COLOR);
        resultDisplay.setPreferredSize(new Dimension(0, 40));

        lcdScreen.add(inputDisplay, BorderLayout.CENTER);
        lcdScreen.add(resultDisplay, BorderLayout.SOUTH);

        // Thanh trạng thái (Mode)
        modeLabel = new JLabel("OFF", SwingConstants.RIGHT);
        modeLabel.setFont(new Font("Arial", Font.BOLD, 10));
        modeLabel.setForeground(new Color(100, 100, 100));
        modeLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        add(lcdScreen, BorderLayout.CENTER);
        add(modeLabel, BorderLayout.SOUTH);
    }

    public void updateDisplay(String text) {
        inputDisplay.setText(text);
    }

    public void updateResult(String result) {
        resultDisplay.setText(result);
    }

    public void setMode(String mode) {
        modeLabel.setText(mode);
        // Hiệu ứng đèn báo
        if ("OFF".equals(mode)) {
            // Ẩn màn hình khi tắt
            inputDisplay.setForeground(LCD_BG);
            resultDisplay.setForeground(LCD_BG);
        } else {
            inputDisplay.setForeground(TEXT_COLOR);
            resultDisplay.setForeground(TEXT_COLOR);
        }
    }
}