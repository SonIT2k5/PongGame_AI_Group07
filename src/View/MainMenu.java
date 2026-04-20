package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {

    private int currentAiLevel = 1; // 1: Dễ, 2: Khó
    private JPanel crtBackground;   // Đưa crtBackground ra ngoài để các hàm khác có thể gọi

    public MainMenu() {
        setTitle("Pong Game - Retro Arcade");
        setSize(1000, 555);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ==========================================
        // 1. TẠO BACKGROUND HIỆU ỨNG MÀN HÌNH CRT + ẢNH ARCADE
        // ==========================================
        crtBackground = new JPanel() {
            ImageIcon bg = new ImageIcon("resources/images/ArcadeBG.png");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                if (bg.getImage() != null) {
                    g2.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2.setColor(new Color(15, 15, 15));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }

                // Sọc ngang CRT
                g2.setColor(new Color(255, 255, 255, 12));
                for (int i = 0; i < getHeight(); i += 4) {
                    g2.drawLine(0, i, getWidth(), i);
                }
            }
        };

        crtBackground.setLayout(new BoxLayout(crtBackground, BoxLayout.Y_AXIS));
        setContentPane(crtBackground);

        // Khởi tạo hiển thị Menu chính ban đầu
        showMainView();

        setVisible(true);
    }

    // ==========================================
    // 2. HÀM HIỂN THỊ MENU CHÍNH
    // ==========================================
    private void showMainView() {
        crtBackground.removeAll(); // Xóa sạch các nút hiện tại trên màn hình
        crtBackground.add(Box.createVerticalStrut(110)); // Khoảng cách từ trên xuống

        RetroButton singleBtn = new RetroButton("SINGLE PLAYER");
        RetroButton multiBtn = new RetroButton("MULTIPLAYER");
        RetroButton optionsBtn = new RetroButton("OPTIONS");
        RetroButton historyBtn = new RetroButton("HISTORY");
        RetroButton helpBtn = new RetroButton("HELP");
        RetroButton exitBtn = new RetroButton("EXIT");

        int spacing = 4;
        crtBackground.add(singleBtn);
        crtBackground.add(Box.createVerticalStrut(spacing));
        crtBackground.add(multiBtn);
        crtBackground.add(Box.createVerticalStrut(spacing));
        crtBackground.add(optionsBtn);
        crtBackground.add(Box.createVerticalStrut(spacing));
        crtBackground.add(historyBtn);
        crtBackground.add(Box.createVerticalStrut(spacing));
        crtBackground.add(helpBtn);
        crtBackground.add(Box.createVerticalStrut(spacing));
        crtBackground.add(exitBtn);

        // Gắn sự kiện
        singleBtn.addActionListener(e -> { dispose(); new GameFrame(true, currentAiLevel); });
        multiBtn.addActionListener(e -> { dispose(); new GameFrame(false, 1); });

        // NÚT OPTIONS: Chuyển sang giao diện tùy chọn độ khó
        optionsBtn.addActionListener(e -> showOptionsView());

        historyBtn.addActionListener(e -> showHistoryView());

        helpBtn.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Luật chơi", true);
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(this);
            ImageIcon icon = new ImageIcon("resources/images/Rule.png");
            dialog.add(new JLabel(icon));
            dialog.setVisible(true);
        });

        exitBtn.addActionListener(e -> System.exit(0));

        // Cập nhật lại giao diện
        crtBackground.revalidate();
        crtBackground.repaint();
    }

    // ==========================================
    // 3. HÀM HIỂN THỊ MENU OPTIONS (ĐỘ KHÓ)
    // ==========================================
    private void showOptionsView() {
        crtBackground.removeAll(); // Xóa menu chính

        // Vì menu này chỉ có 3 nút, ta tăng Strut lên một chút (150) để nó nằm ngay giữa màn hình
        crtBackground.add(Box.createVerticalStrut(150));

        // Hiển thị ký hiệu [*] ở chế độ đang được chọn
        String easyText = (currentAiLevel == 1) ? "[*] DỄ (CHẬM)" : "[ ] DỄ (CHẬM)";
        String hardText = (currentAiLevel == 2) ? "[*] KHÓ (NHANH)" : "[ ] KHÓ (NHANH)";

        RetroButton easyBtn = new RetroButton(easyText);
        RetroButton hardBtn = new RetroButton(hardText);
        RetroButton backBtn = new RetroButton("TRỞ LẠI");

        // Sự kiện đổi độ khó
        easyBtn.addActionListener(e -> {
            currentAiLevel = 1;
            showOptionsView(); // Gọi lại hàm để vẽ lại màn hình (cập nhật dấu [*])
        });

        hardBtn.addActionListener(e -> {
            currentAiLevel = 2;
            showOptionsView(); // Gọi lại hàm để vẽ lại màn hình (cập nhật dấu [*])
        });

        // Nút Trở lại: Xóa Options và vẽ lại Menu chính
        backBtn.addActionListener(e -> showMainView());

        int spacing = 15; // Giãn cách rộng ra một chút cho đẹp
        crtBackground.add(easyBtn);
        crtBackground.add(Box.createVerticalStrut(spacing));
        crtBackground.add(hardBtn);
        crtBackground.add(Box.createVerticalStrut(spacing));
        crtBackground.add(backBtn);

        // Cập nhật lại giao diện
        crtBackground.revalidate();
        crtBackground.repaint();
    }

    // ==========================================
    // ==========================================
    // 4. HÀM HIỂN THỊ MENU LỊCH SỬ (GIAO DIỆN BẢNG NEON)
    // ==========================================
    private void showHistoryView() {
        crtBackground.removeAll();
        crtBackground.add(Box.createVerticalStrut(80)); // Đẩy giao diện xuống giữa màn hình

        Color neonCyan = new Color(100, 255, 255); // Màu xanh ngọc (Cyan) giống viền trong ảnh
        Color neonGreen = new Color(57, 255, 20);  // Màu xanh lá cây của chữ

        // 1. Tiêu đề chính
        JLabel titleLabel = new JLabel("PONG: LỊCH SỬ CHƠI");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        titleLabel.setForeground(neonCyan);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        crtBackground.add(titleLabel);
        crtBackground.add(Box.createVerticalStrut(10));

        // 2. Tiêu đề phụ
        JLabel subTitleLabel = new JLabel("HIỂN THỊ 10 TRẬN ĐẤU GẦN NHẤT");
        subTitleLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        subTitleLabel.setForeground(Color.WHITE);
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        crtBackground.add(subTitleLabel);
        crtBackground.add(Box.createVerticalStrut(20));

        // 3. Tạo bảng bằng Text (ASCII Art Table)
        java.util.List<String> lines = HistoryUtil.readAll();
        StringBuilder tableText = new StringBuilder();

        // Vẽ Header của bảng
        tableText.append(" ┌─────┬─────────────────────┬──────────────┐\n");
        tableText.append(" │ STT │      THỜI GIAN      │ NGƯỜI THẮNG  │\n");
        tableText.append(" ├─────┼─────────────────────┼──────────────┤\n");

        if (lines.isEmpty()) {
            tableText.append(" │            CHƯA CÓ DỮ LIỆU               │\n");
        } else {
            // Lấy tối đa 10 trận gần nhất, duyệt từ dưới lên (trận mới nhất lên đầu)
            int count = 1;
            for (int i = lines.size() - 1; i >= Math.max(0, lines.size() - 10); i--) {
                String line = lines.get(i);
                // Tách chuỗi theo định dạng "2026-04-17 07:44:59 - Player 2"
                String[] parts = line.split(" - ");
                String time = parts.length > 0 ? parts[0] : "N/A";
                String winner = parts.length > 1 ? parts[1].trim() : "N/A";

                // Format khoảng trắng cho chữ lọt chuẩn vào giữa các cột
                tableText.append(String.format(" │ %2d. │ %-19s │ %-12s │\n", count, time, winner));
                count++;
            }
        }
        tableText.append(" └─────┴─────────────────────┴──────────────┘\n");

        // 4. Khung chứa Bảng (Có hiệu ứng viền sáng Glow)
        JTextArea textArea = new JTextArea(tableText.toString()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Lớp nền đen mờ bên trong bảng
                g2.setColor(new Color(15, 20, 25, 210));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Viền phát sáng ngọc (Glow)
                g2.setColor(new Color(100, 255, 255, 80)); // Viền mờ bọc ngoài
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 10, 10);

                g2.setColor(neonCyan); // Viền sắc nét bên trong
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);

                super.paintComponent(g);
            }
        };
        textArea.setFont(new Font("Monospaced", Font.BOLD, 15));
        textArea.setForeground(neonGreen); // Chữ màu xanh lá
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setMargin(new Insets(15, 15, 15, 15));

        // Bọc vào JPanel để không bị kéo giãn full màn hình
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        wrapper.add(textArea);

        crtBackground.add(wrapper);
        crtBackground.add(Box.createVerticalStrut(20));

        // 5. Nút quay lại kiểu bọc viền (Giống y hệt hình ảnh)
        JButton backBtn = new JButton("QUAY LẠI MENU CHÍNH");
        backBtn.setFont(new Font("Monospaced", Font.BOLD, 18));
        backBtn.setForeground(neonCyan);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tạo viền xanh ngọc bọc quanh chữ, có khoảng trống (padding)
        backBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonCyan, 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        // Hiệu ứng Hover đổi thành màu trắng
        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backBtn.setForeground(Color.WHITE);
                backBtn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backBtn.setForeground(neonCyan);
                backBtn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(neonCyan, 2),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)
                ));
            }
        });

        backBtn.addActionListener(e -> showMainView());
        crtBackground.add(backBtn);

        crtBackground.revalidate();
        crtBackground.repaint();
    }
    // =========================================================
    // LỚP GIAO DIỆN: NÚT BẤM RETRO THUẦN CODE
    // =========================================================
    class RetroButton extends JButton {
        private String rawText;

        public RetroButton(String text) {
            super(text);
            this.rawText = text;

            setAlignmentX(Component.CENTER_ALIGNMENT);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setFont(new Font("Monospaced", Font.BOLD, 16));

            // Màu Hổ phách (Amber) cổ điển
            Color retroAmber = new Color(255, 176, 0);
            setForeground(retroAmber);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setForeground(new Color(255, 220, 100)); // Sáng lên
                    setText("► " + rawText + " ◄");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setForeground(retroAmber); // Trở lại bình thường
                    setText(rawText);
                }
            });
        }
    }
}