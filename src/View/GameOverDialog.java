import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GameOverDialog extends JDialog {
    private boolean replaySelected = false;

    public GameOverDialog(Window parent, String winner) {
        super(parent, "Trò chơi kết thúc!", ModalityType.APPLICATION_MODAL);
        setSize(500, 300);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Nền chủ đề pong
        setContentPane(new BackgroundPanel());

        // Nội dung chính
        JLabel messageLabel = new JLabel(winner + " thắng!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 26));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel askLabel = new JLabel("Bạn có muốn chơi lại không?");
        askLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        askLabel.setForeground(Color.LIGHT_GRAY);
        askLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Nút Yes / No
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        yesButton.setFocusPainted(false);
        noButton.setFocusPainted(false);

        yesButton.setPreferredSize(new Dimension(100, 35));
        noButton.setPreferredSize(new Dimension(100, 35));

        yesButton.setFont(new Font("Arial", Font.BOLD, 16));
        noButton.setFont(new Font("Arial", Font.BOLD, 16));

        yesButton.addActionListener(e -> {
            replaySelected = true;
            dispose();
        });

        noButton.addActionListener(e -> {
            replaySelected = false;
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Cho nền trong suốt
        buttonPanel.add(yesButton);
        buttonPanel.add(Box.createHorizontalStrut(20)); // khoảng cách giữa nút
        buttonPanel.add(noButton);

        // Panel chính để chứa tất cả
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(askLabel);
        contentPanel.add(Box.createVerticalStrut(70));
        contentPanel.add(buttonPanel);

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    public boolean isReplaySelected() {
        return replaySelected;
    }

    // Panel nền tùy chỉnh
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(new File("resources/images/victory.png"));
            } catch (Exception e) {
                System.err.println("Không tìm thấy ảnh nền: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                setBackground(Color.DARK_GRAY);
            }
        }
    }
}
