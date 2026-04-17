package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PauseDialog extends JDialog {
    public static final int RESUME = 0;
    public static final int RESTART = 1;
    public static final int MAIN_MENU = 2;

    private int action = RESUME; // Mặc định là tiếp tục

    public PauseDialog(Window parent) {
        super(parent, "Tạm Dừng", ModalityType.APPLICATION_MODAL);
        setSize(320, 380);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Xóa phông nền mặc định để làm viền bo góc

        // ==========================================
        // TẠO PANEL CHÍNH VỚI HIỆU ỨNG GLOW NEON
        // ==========================================
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 20; // Độ bo tròn góc

                // 1. Vẽ lớp bóng tỏa sáng (Glow) màu xanh ngọc phía sau
                g2.setColor(new Color(100, 255, 200, 150));
                g2.fillRoundRect(8, 8, getWidth() - 12, getHeight() - 12, arc, arc);

                // 2. Vẽ nền đen của Menu
                g2.setColor(new Color(15, 20, 25));
                g2.fillRoundRect(2, 2, getWidth() - 12, getHeight() - 12, arc, arc);

                // 3. Vẽ viền trắng dày bọc ngoài
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(4f));
                g2.drawRoundRect(2, 2, getWidth() - 12, getHeight() - 12, arc, arc);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // ==========================================
        // THÊM TIÊU ĐỀ "PAUSED"
        // ==========================================
        JLabel title = new JLabel("PAUSED");
        title.setFont(new Font("Monospaced", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ==========================================
        // TẠO CÁC NÚT BẤM CÓ ICON
        // ==========================================
        // Sử dụng các ký tự Unicode giống với icon trong ảnh nhất
        RetroPauseButton resumeBtn = new RetroPauseButton("▶ Tiếp tục");
        RetroPauseButton restartBtn = new RetroPauseButton("↺ Chơi lại");
        RetroPauseButton menuBtn = new RetroPauseButton("🚪 Trở về");

        // Bắt sự kiện
        resumeBtn.addActionListener(e -> { action = RESUME; dispose(); });
        restartBtn.addActionListener(e -> { action = RESTART; dispose(); });
        menuBtn.addActionListener(e -> { action = MAIN_MENU; dispose(); });

        // ==========================================
        // SẮP XẾP BỐ CỤC (Căn chỉnh khoảng cách)
        // ==========================================
        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(35));
        panel.add(resumeBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(restartBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(menuBtn);

        add(panel);
    }

    public int getAction() {
        return action;
    }

    // =========================================================
    // LỚP GIAO DIỆN: NÚT BẤM RETRO TRẮNG VIỀN ĐEN 3D
    // =========================================================
    class RetroPauseButton extends JButton {
        public RetroPauseButton(String text) {
            super(text);
            setFont(new Font("Monospaced", Font.BOLD, 22));
            setForeground(Color.BLACK); // Chữ màu đen
            setBackground(Color.WHITE); // Nền màu trắng
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.CENTER_ALIGNMENT);

            // Kích thước cố định cho tất cả các nút
            setMaximumSize(new Dimension(240, 50));
            setPreferredSize(new Dimension(240, 50));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(new Color(220, 255, 240)); // Hơi ngả xanh ngọc khi hover
                }
                public void mouseExited(MouseEvent e) {
                    setBackground(Color.WHITE);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 15; // Độ bo tròn của nút

            // 1. Vẽ bóng (đổ bóng đen nhỏ bên dưới tạo cảm giác 3D nổi)
            g2.setColor(Color.DARK_GRAY);
            g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, arc, arc);

            // 2. Vẽ nền nút màu trắng
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, arc, arc);

            // 3. Vẽ viền đen bọc quanh nút
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, getWidth() - 6, getHeight() - 6, arc, arc);

            super.paintComponent(g);
        }
    }
}