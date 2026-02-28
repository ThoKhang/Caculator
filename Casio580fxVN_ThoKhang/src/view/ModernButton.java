package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernButton extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public ModernButton(String text, Color bgColor, Color fgColor) {
        super(text);
        this.normalColor = bgColor;
        this.hoverColor = bgColor.brighter(); // Màu sáng hơn khi di chuột
        this.pressedColor = bgColor.darker(); // Màu tối hơn khi nhấn

        setForeground(fgColor);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);

        // Font chữ mặc định đẹp hơn
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Con trỏ tay khi di vào

        // Xử lý hiệu ứng chuột
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Khử răng cưa cho hình vẽ mượt mà
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Chọn màu dựa trên trạng thái
        Color paintColor = normalColor;
        if (isPressed) paintColor = pressedColor;
        else if (isHovered) paintColor = hoverColor;

        g2.setColor(paintColor);

        // Vẽ hình chữ nhật bo tròn (Bo góc 15 pixel)
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

        // Vẽ chữ
        super.paintComponent(g);
        g2.dispose();
    }
}