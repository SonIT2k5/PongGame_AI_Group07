import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;


public class GamePanel extends JPanel implements Runnable {
  private volatile boolean isPaused = false; // Biến kiểm tra xem game có tạm dừng hay không
  private boolean playWithAI; // Biến kiểm tra chế độ chơi với AI
  static final int GAME_WIDTH = 1000;
  static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555)); // Chiều cao của game (tỉ lệ 16:9)
  static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT); // Kích thước màn hình
  static final int BALL_DIAMETER = 20; // Đường kính của quả bóng
  static final int PADDLE_WIDTH = 25; // Chiều rộng của vợt
  static final int PADDLE_HEIGHT = 100; // Chiều cao của vợt
  private int aiBallCount = 0;
  private static final int WINNING_SCORE = 3;
  private static final int MIN_LEAD = 2;

  Thread gameThread;
  Image image;
  Graphics graphics;
  Random random;
  Paddle paddle1;
  Paddle paddle2;
  Ball ball;
  Score score;
  Image backgroundImage;

  // Hàm khởi tạo GamePanel với tham số kiểm tra chế độ chơi với AI
  public GamePanel(boolean playWithAI, int aiLevel) {
    this.playWithAI = playWithAI; // Gán giá trị từ GameFrame truyền vào
    newPaddles(); // Khởi tạo các vợt mới
    newBall(); // Khởi tạo quả bóng mới
    score = new Score(GAME_WIDTH, GAME_HEIGHT); // Khởi tạo điểm số
    this.setFocusable(true); // Đặt game có thể nhận sự kiện bàn phím
    this.addKeyListener(new AL()); // Thêm key listener
    this.setPreferredSize(SCREEN_SIZE); // Đặt kích thước màn hình
    this.playWithAI = playWithAI;


    try {
      backgroundImage = ImageIO.read(new File("resources/images/TableTennisTable.png"));
    } catch (IOException e) {
      e.printStackTrace(); // Xử lý lỗi nếu không đọc được hình nền
    }

    gameThread = new Thread(this); // Tạo thread chạy game
    gameThread.start(); // Bắt đầu thread
  }

  // Hàm di chuyển các đối tượng trong game
  public void move() {
    paddle1.move(); // Di chuyển vợt của người chơi 1
    if (playWithAI) {
      aimove(); // Nếu chơi với AI, di chuyển vợt AI
    } else {
      paddle2.move(); // Nếu không chơi với AI, di chuyển vợt người chơi 2
    }
    ball.move(); // Di chuyển quả bóng
  }

  // Hàm di chuyển vợt AI
  // Trong class GamePanel
  public void aimove() {
    // Tâm của paddle AI
    int paddleCenter = paddle2.y + PADDLE_HEIGHT / 2;
    // Tâm của quả bóng
    int ballCenter = ball.y + BALL_DIAMETER / 2;
    // Tốc độ di chuyển của AI (tăng nếu muốn AI nhanh hơn)
    int speed = 5;

    // Nếu bóng ở trên paddle, AI di chuyển lên
    if (ballCenter < paddleCenter) {
      paddle2.y -= speed;
    }
    // Nếu bóng ở dưới paddle, AI di chuyển xuống
    else if (ballCenter > paddleCenter) {
      paddle2.y += speed;
    }

    // Giới hạn paddle AI trong khung game
    if (paddle2.y < 0) {
      paddle2.y = 0;
    }
    if (paddle2.y > GAME_HEIGHT - PADDLE_HEIGHT) {
      paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
    }
  }



  // Hàm khởi tạo lại quả bóng mới
  public void newBall() {
    random = new Random();
    int x = (GAME_WIDTH / 2) - (BALL_DIAMETER / 2);
    int y = random.nextInt(GAME_HEIGHT - BALL_DIAMETER - 100) + 50;
    ball = new Ball(x, y, BALL_DIAMETER, BALL_DIAMETER); // Tạo quả bóng mới với vị trí ngẫu nhiên
  }

  // Hàm khởi tạo lại các vợt mới
  public void newPaddles() {
    paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1); // Vợt của người chơi 1
    paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH,
            PADDLE_HEIGHT, 2); // Vợt của người chơi 2
  }

  // Hàm vẽ lại các đối tượng trong game
  public void paint(Graphics g) {
    image = createImage(getWidth(), getHeight()); // Tạo một ảnh mới để vẽ
    graphics = image.getGraphics(); // Lấy đối tượng Graphics từ ảnh
    draw(graphics); // Vẽ các đối tượng
    g.drawImage(image, 0, 0, this); // Vẽ ảnh ra màn hình
  }

  // Hàm vẽ các đối tượng game
  public void draw(Graphics g) {
    g.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, this); // Vẽ hình nền
    paddle1.draw(g); // Vẽ vợt của người chơi 1
    paddle2.draw(g); // Vẽ vợt của người chơi 2
    ball.draw(g); // Vẽ quả bóng
    score.draw(g); // Vẽ điểm số

    if (isPaused) {
      // Nếu game tạm dừng, tạo lớp phủ mờ
      Graphics2D g2d = (Graphics2D) g.create();
      g2d.setColor(new Color(0, 0, 0, 90));  // Màu đen với độ trong suốt 90
      g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT); // Vẽ lớp phủ mờ
      g2d.dispose();

      // Chỉnh màu chữ và hiệu ứng đổ bóng
      g.setColor(Color.YELLOW);  // Màu chữ vàng
      g.setFont(new Font("Arial Black", Font.BOLD, 50)); // Font chữ

      // Đổ bóng cho chữ
      Graphics2D g2dText = (Graphics2D) g;
      g2dText.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2dText.setColor(Color.BLACK);  // Màu bóng chữ
      g2dText.drawString("TẠM DỪNG", GAME_WIDTH / 2 - 155, GAME_HEIGHT / 2 + 5);
      g2dText.setColor(Color.YELLOW);  // Màu chữ lại vàng
      g2dText.drawString("TẠM DỪNG", GAME_WIDTH / 2 - 150, GAME_HEIGHT / 2);
    }
  }

  // Hàm kiểm tra va chạm của các đối tượng trong game
  public void checkCollision() {
    // Kiểm tra quả bóng va chạm với mép trên và dưới của cửa sổ
    if (ball.y <= 0) {
      ball.setYDirection(-ball.yVelocity); // Đổi hướng quả bóng khi chạm mép trên
    }
    if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
      ball.setYDirection(-ball.yVelocity); // Đổi hướng quả bóng khi chạm mép dưới
    }

    // Kiểm tra va chạm với vợt của người chơi
    if (ball.intersects(paddle1)) {
      ball.xVelocity = Math.abs(ball.xVelocity); // Chuyển xVelocity thành số dương
      ball.xVelocity++; // Tăng tốc độ mỗi khi quả bóng va vào vợt
      if (ball.yVelocity > 0)
        ball.yVelocity++; // Tăng tốc độ theo chiều y
      else
        ball.yVelocity--; // Giảm tốc độ theo chiều y
      ball.setXDirection(ball.xVelocity);
      ball.setYDirection(ball.yVelocity);
      SoundPlayer.playSound("resources/sounds/hit.wav", false);
    }

    if (ball.intersects(paddle2)) {
      ball.xVelocity = Math.abs(ball.xVelocity); // Chuyển xVelocity thành số dương
      ball.xVelocity++; // Tăng tốc độ mỗi khi quả bóng va vào vợt
      if (ball.yVelocity > 0)
        ball.yVelocity++; // Tăng tốc độ theo chiều y
      else
        ball.yVelocity--; // Giảm tốc độ theo chiều y
      ball.setXDirection(-ball.xVelocity);
      ball.setYDirection(ball.yVelocity);
      SoundPlayer.playSound("resources/sounds/hit.wav", false);
    }

    // Dừng vợt không cho vượt quá cửa sổ
    if (paddle1.y <= 0)
      paddle1.y = 0;
    if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
      paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
    if (paddle2.y <= 0)
      paddle2.y = 0;
    if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
      paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;

    // Cho người chơi 1 điểm và tạo lại vợt và bóng
    if (ball.x <= 0) {
      score.player2++; // Người chơi 2 được điểm
      newPaddles(); // Tạo lại vợt
      newBall(); // Tạo lại bóng
      System.out.println("Player 2: " + score.player2);
    }
    if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
      score.player1++; // Người chơi 1 được điểm
      newPaddles(); // Tạo lại vợt
      newBall(); // Tạo lại bóng
      System.out.println("Player 1: " + score.player1);
    }

    // Kiểm tra kết thúc game
    if ((score.player1 >= WINNING_SCORE || score.player2 >= WINNING_SCORE) && Math.abs(score.player1 - score.player2) >= MIN_LEAD) {
      gameOver();
      SoundPlayer.playSound("resources/sounds/victory.wav", false);
    }
  }

  // Hàm xử lý kết thúc game
  public void gameOver() {
    String winner;
    if (score.player1 >= WINNING_SCORE && score.player1 - score.player2 >= MIN_LEAD) {
      winner = "Player 1"; // Người chơi 1 thắng
    } else if (score.player2 >= WINNING_SCORE && score.player2 - score.player1 >= MIN_LEAD) {
      winner = "Player 2"; // Người chơi 2 thắng
    } else {
      return; // Không đủ điều kiện thắng
    }

    HistoryUtil.append(winner);
    isPaused = true; // Tạm dừng game

    // Hiển thị cửa sổ GameOverDialog
    SwingUtilities.invokeLater(() -> {
      GameOverDialog dialog = new GameOverDialog(SwingUtilities.getWindowAncestor(this), winner);
      dialog.setVisible(true);

      if (dialog.isReplaySelected()) {
        score.reset(); // Đặt lại điểm số
        newPaddles(); // Tạo lại vợt
        newBall(); // Tạo lại bóng
        isPaused = false; // Tiếp tục game
      } else {
        SwingUtilities.getWindowAncestor(this).dispose();  // Đóng GameFrame
        new MainMenu();  // Mở lại MainMenu
      }
    });
  }

  // Hàm chạy game
  public void run() {
    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;

    while (true) {
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;

      if (!isPaused && delta >= 1) {  // Nếu không tạm dừng game
        move();
        checkCollision();
        repaint();
        delta--;
      } else if (isPaused) {
        repaint();  // Vẽ lại màn hình nhưng không di chuyển
        try {
          Thread.sleep(100); // Tiết kiệm CPU
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // Hàm xử lý sự kiện phím
  public class AL extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();

      if (key == KeyEvent.VK_ESCAPE) {  // Bấm ESC để tạm dừng hoặc tiếp tục game
        isPaused = !isPaused;
      } else if (key == KeyEvent.VK_Q) {  // Bấm Q để thoát về menu chính
        isPaused = true;
        SwingUtilities.getWindowAncestor(GamePanel.this).dispose(); // Đóng cửa sổ hiện tại
        new MainMenu();  // Mở lại menu chính
      } else {
        if (!isPaused) {
          paddle1.keyPressed(e); // Người chơi 1 bấm phím
          paddle2.keyPressed(e); // Người chơi 2 bấm phím
        }
      }
    }

    public void keyReleased(KeyEvent e) {
      if (!isPaused) {
        paddle1.keyReleased(e); // Người chơi 1 thả phím
        paddle2.keyReleased(e); // Người chơi 2 thả phím
      }
    }
  }
}
