import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ModeSelectionDialog extends JDialog {

    public ModeSelectionDialog(JFrame parent) {
        super(parent, "Chọn chế độ chơi", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(null);

        // Nền là ảnh Pong Menu đã tạo
        JLabel background = new JLabel(new ImageIcon("resources/images/OptionPlay.png"));
        background.setBounds(0, 0, 500, 400);

        // Tạo nút 1 PLAYER
        GameButton onePlayerButton = new GameButton("1 PLAYER");
        onePlayerButton.setBounds(180, 270, 140, 40);
        onePlayerButton.addActionListener(e -> {
            // Đóng luôn MainMenu (this) và cửa sổ parent nếu có
            dispose();
            parent.dispose();  // hoặc SwingUtilities.getWindowAncestor(this).dispose();

            // Mở luôn game với playWithAI = true và aiLevel = 1
            new GameFrame(true, 1);
        });




        // Tạo nút 2 PLAYER
        GameButton twoPlayerButton = new GameButton("2 PLAYER");
        twoPlayerButton.setBounds(180, 320, 140, 40);
        twoPlayerButton.addActionListener(e -> {
            dispose();
            parent.dispose();
            new GameFrame(false,1); // chơi 2 người
        });

        add(onePlayerButton);
        add(twoPlayerButton);
        add(background); // thêm sau cùng để background nằm dưới

        setResizable(false);
        setVisible(true);
    }

    // Nút phong cách retro Pong
    class GameButton extends JButton {
        public GameButton(String text) {
            super(text);
            setFont(new Font("Monospaced", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setBackground(new Color(30, 30, 30));
            setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            setFocusPainted(false);
            setContentAreaFilled(true);
            setOpaque(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(new Color(60, 60, 60));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(new Color(30, 30, 30));
                }
            });
        }
    }
}
