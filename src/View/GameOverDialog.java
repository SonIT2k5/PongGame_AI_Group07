package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverDialog extends JDialog {
    private boolean replaySelected = false;

    // Đã thêm 2 tham số p1Score và p2Score để hiển thị tỷ số
    public GameOverDialog(Window parent, String winner, int p1Score, int p2Score) {
        super(parent, "Game Over", ModalityType.APPLICATION_MODAL);
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Làm nền trong suốt để bo góc

        // ==========================================
        // 1. TẠO KHUNG NỀN VIỀN TRẮNG
        // ==========================================
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 15;
                // Nền đen mờ
                g2.setColor(new Color(15, 20, 25, 230));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, arc, arc);

                // Viền trắng bọc ngoài
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, arc, arc);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // ==========================================
        // 2. KHỞI TẠO CÁC DÒNG CHỮ (TEXT)
        // ==========================================
        JLabel titleLabel = new JLabel("WINNER!");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 50));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel congratsLabel = new JLabel("CONGRATULATIONS");
        congratsLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        congratsLabel.setForeground(Color.LIGHT_GRAY);
        congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tên người thắng
        JLabel winnerLabel = new JLabel("🏆 " + winner.toUpperCase() + " 🏆");
        winnerLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        winnerLabel.setForeground(new Color(100, 255, 255)); // Màu xanh ngọc (Cyan)
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tỷ số (Định dạng "00 - 00")
        String scoreText = String.format("%02d - %02d", p1Score, p2Score);
        JLabel scoreLabel = new JLabel(scoreText);
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 45));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreSubLabel = new JLabel("SCORE");
        scoreSubLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        scoreSubLabel.setForeground(Color.LIGHT_GRAY);
        scoreSubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ==========================================
        // 3. KHỞI TẠO 2 NÚT BẤM CÓ VIỀN NEON
        // ==========================================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Nút xanh (Play Again)
        RetroActionButton playAgainBtn = new RetroActionButton(
                "<html><center>PLAY AGAIN<br><span style='font-size:11px; color:#A0A0A0;'>CHƠI LẠI</span></center></html>",
                new Color(57, 255, 20)); // Xanh lá

        // Nút đỏ (Main Menu)
        RetroActionButton mainMenuBtn = new RetroActionButton(
                "<html><center>MAIN MENU<br><span style='font-size:11px; color:#A0A0A0;'>MENU CHÍNH</span></center></html>",
                new Color(255, 50, 50)); // Đỏ

        playAgainBtn.addActionListener(e -> { replaySelected = true; dispose(); });
        mainMenuBtn.addActionListener(e -> { replaySelected = false; dispose(); });

        buttonPanel.add(playAgainBtn);
        buttonPanel.add(mainMenuBtn);

        // ==========================================
        // 4. SẮP XẾP BỐ CỤC
        // ==========================================
        panel.add(Box.createVerticalStrut(20));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(congratsLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(winnerLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(scoreLabel);
        panel.add(scoreSubLabel);
        panel.add(Box.createVerticalStrut(25));
        panel.add(buttonPanel);

        add(panel);
    }

    public boolean isReplaySelected() {
        return replaySelected;
    }

    // =========================================================
    // LỚP GIAO DIỆN: NÚT BẤM CÓ VIỀN MÀU NEON TRỐNG GIỮA
    // =========================================================
    class RetroActionButton extends JButton {
        private Color neonColor;
        private boolean isHovered = false;

        public RetroActionButton(String text, Color neonColor) {
            super(text);
            this.neonColor = neonColor;
            setFont(new Font("Monospaced", Font.BOLD, 18));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(160, 55)); // Kích thước nút bấm

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 15;
            // Nền đen
            g2.setColor(Color.BLACK);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

            if (isHovered) {
                // Khi di chuột vào: Nút đổi thành viền sáng rực (Dày hơn)
                g2.setColor(neonColor);
                g2.setStroke(new BasicStroke(4f));
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, arc, arc);
            } else {
                // Bình thường: Viền mỏng
                g2.setColor(neonColor);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}