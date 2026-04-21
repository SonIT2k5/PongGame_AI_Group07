package View;
import Model.*; // Lệnh này giúp GamePanel nhìn thấy Ball, Paddle, Score
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
  private static final int WINNING_SCORE = 10;
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
//   Trong class GamePanel
//   Hàm di chuyển vợt AI (Đã được nâng cấp khả năng dự đoán quỹ đạo)
  public void aimove() {
    int predictedY = ball.y;

    // DỰ ĐOÁN QUỸ ĐẠO BÓNG (Analytic)
    if (ball.xVelocity > 0) { // Chỉ tính toán khi bóng đang bay về phía sân AI

      // Khoảng cách theo trục X từ quả bóng đến vợt AI
      int distanceX = (GAME_WIDTH - PADDLE_WIDTH) - ball.x;

      // Tính thời gian (số frame) dự kiến để bóng bay tới nơi
      int timeToReach = distanceX / ball.xVelocity;

      // Tọa độ Y "ảo" (giả sử bóng có thể bay xuyên tường ra ngoài vũ trụ)
      int virtualY = ball.y + (timeToReach * ball.yVelocity);

      // Chiều cao không gian di chuyển thực tế của quả bóng
      int fieldHeight = GAME_HEIGHT - BALL_DIAMETER;

      // Sử dụng công thức "Sóng tam giác" để gấp khúc quỹ đạo ảo thành các lần nảy tường
      predictedY = fieldHeight - Math.abs((Math.abs(virtualY) % (2 * fieldHeight)) - fieldHeight);

    } else {
      // Khi bóng đang bay về phía người chơi, AI rảnh rỗi nên tự động lùi về giữa sân chờ
      predictedY = (GAME_HEIGHT / 2) - (BALL_DIAMETER / 2);
    }

    // ==========================================
    // BƯỚC 2: HILL CLIMBING (Quyết định di chuyển)
    // ==========================================
    int paddleCenter = paddle2.y + (PADDLE_HEIGHT / 2); // Tâm của vợt AI
    int targetY = predictedY + (BALL_DIAMETER / 2);     // Điểm rơi kỳ vọng của tâm quả bóng

    // Điều chỉnh tốc độ (độ khó) dựa vào biến aiLevel được truyền từ Menu
    int aiSpeed = 5;

    // Di chuyển vợt: Tìm cách giảm khoảng cách giữa paddleCenter và targetY về 0
    if (paddleCenter < targetY - aiSpeed) {
      paddle2.y += aiSpeed; // Đi xuống
    } else if (paddleCenter > targetY + aiSpeed) {
      paddle2.y -= aiSpeed; // Đi lên
    }

    // Không để vợt AI bay xuyên thủng trần hoặc sàn nhà
    if (paddle2.y < 0) {
      paddle2.y = 0;
    }
    if (paddle2.y > GAME_HEIGHT - PADDLE_HEIGHT) {
      paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
    }
  }

//  public void aimove() {
//    int predictedY = ball.y;
//    int aiSpeed = 3; // CHỈ SỐ LÀM YẾU
//
//    if (ball.xVelocity > 0) {
//      int distanceX = (GAME_WIDTH - PADDLE_WIDTH) - ball.x;
//      int timeToReach = distanceX / ball.xVelocity;
//      int virtualY = ball.y + (timeToReach * ball.yVelocity);
//      int fieldHeight = GAME_HEIGHT - BALL_DIAMETER;
//      predictedY = fieldHeight - Math.abs((Math.abs(virtualY) % (2 * fieldHeight)) - fieldHeight);
//    } else {
//      predictedY = (GAME_HEIGHT / 2) - (BALL_DIAMETER / 2);
//    }
//
//    int paddleCenter = paddle2.y + (PADDLE_HEIGHT / 2);
//    int targetY = predictedY + (BALL_DIAMETER / 2); // Mục tiêu chính xác tuyệt đối 100%
//
//    if (paddleCenter < targetY - aiSpeed) {
//      paddle2.y += aiSpeed;
//    } else if (paddleCenter > targetY + aiSpeed) {
//      paddle2.y -= aiSpeed;
//    }
//
//    if (paddle2.y < 0) paddle2.y = 0;
//    if (paddle2.y > GAME_HEIGHT - PADDLE_HEIGHT) paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
//  }


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
      GameOverDialog dialog = new GameOverDialog(SwingUtilities.getWindowAncestor(this), winner, score.player1, score.player2);
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
        delta = 0;
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

      if (key == KeyEvent.VK_ESCAPE) {
        // Chỉ gọi Menu khi game chưa bị tạm dừng
        if (!isPaused) {
          isPaused = true;

          // Bật Menu Tạm Dừng (Luồng giao diện sẽ bị chặn chờ tại đây)
          PauseDialog pauseMenu = new PauseDialog(SwingUtilities.getWindowAncestor(GamePanel.this));
          pauseMenu.setVisible(true);

          // Xử lý quyết định của người chơi sau khi Menu Tạm Dừng đóng lại
          int action = pauseMenu.getAction();

          if (action == PauseDialog.RESUME) {
            isPaused = false; // Tiếp tục chạy game
          }
          else if (action == PauseDialog.RESTART) {
            score.reset();    // Reset điểm về 0
            newPaddles();     // Khởi tạo lại vị trí vợt
            newBall();        // Khởi tạo lại bóng
            isPaused = false; // Bắt đầu trận mới
          }
          else if (action == PauseDialog.MAIN_MENU) {
            SwingUtilities.getWindowAncestor(GamePanel.this).dispose(); // Đóng cửa sổ chơi game
            new MainMenu();   // Mở lại menu chính
          }
        }
      } else if (key == KeyEvent.VK_Q) {  // Bấm Q để thoát nhanh về menu chính
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
