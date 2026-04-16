import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Pong Game");
        setSize(1000, 555);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setContentPane(new JLabel(new ImageIcon("resources/images/MainMenu.png"))); // nền bóng bàn
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createVerticalStrut(80)); // khoảng cách trên cùng

        MenuButton startButton = new MenuButton("Bắt đầu");
        MenuButton rulesButton = new MenuButton("Luật chơi");
        MenuButton historyButton = new MenuButton("Lịch sử");
        MenuButton exitButton = new MenuButton("Thoát");
        SoundPlayer.playSound("resources/sounds/background.wav", true);

        add(startButton);
        add(Box.createVerticalStrut(40));
        add(rulesButton);
        add(Box.createVerticalStrut(40));
        add(historyButton);
        add(Box.createVerticalStrut(40));
        add(exitButton);

        // Bắt sự kiện
        startButton.addActionListener(e -> {
            new ModeSelectionDialog(this);
        });


        rulesButton.addActionListener(e -> {
            // Tạo cửa sổ mới để hiển thị ảnh
            JDialog dialog = new JDialog(this, "Luật chơi", true);
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(this); // Hiển thị ở giữa menu

            // Tạo nhãn chứa ảnh
            ImageIcon icon = new ImageIcon("resources/images/Rule.png"); // đường dẫn ảnh
            JLabel label = new JLabel();
            label.setIcon(icon);

            dialog.add(label);
            dialog.setResizable(false);
            dialog.setVisible(true);
        });


        historyButton.addActionListener(e -> {
            // Đọc dòng lịch sử
            java.util.List<String> lines = HistoryUtil.readAll();

            // Nếu chưa có trận nào
            if (lines.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Chưa có lịch sử trận nào!",
                        "Lịch sử",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Tạo JList để hiển thị
            JList<String> list = new JList<>(lines.toArray(new String[0]));
            list.setFont(new Font("Monospaced", Font.PLAIN, 14));

            JScrollPane scroll = new JScrollPane(list);
            scroll.setPreferredSize(new Dimension(500, 300));

            // Hiển thị dialog
            JOptionPane.showMessageDialog(
                    this,
                    scroll,
                    "Lịch sử trận đấu",
                    JOptionPane.PLAIN_MESSAGE
            );
        });


        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        setVisible(true);
    }

    // Custom Button class
    class MenuButton extends JButton {
        public MenuButton(String text) {
            super(text);
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setPreferredSize(new Dimension(300, 70));
            setMaximumSize(new Dimension(300, 70));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 24));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setForeground(new Color(200, 255, 200)); // Nhẹ sáng lên khi hover
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setForeground(Color.WHITE);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            // Shadow
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, 60, 60);

            // Nút chính
            GradientPaint gp = new GradientPaint(0, 0, new Color(5, 66, 53), 0, getHeight(), new Color(1, 41, 33));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth() - 16, getHeight() - 16, 60, 60);

            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        public void paintBorder(Graphics g) {
            // Không vẽ viền
        }

        @Override
        public boolean isOpaque() {
            return false;
        }
    }
}
