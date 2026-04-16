import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame(boolean playWithAI, int aiLevel) {
        GamePanel panel = new GamePanel(playWithAI, aiLevel); // Truyền aiLevel vào GamePanel
        this.add(panel);
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
