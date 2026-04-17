package Controller;

import View.MainMenu;

import javax.swing.*;

public class PongGame {
  public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> new MainMenu());
  }
}

//SwingUtilities.invokeLater(...) đảm bảo rằng đoạn mã GUI bên trong sẽ được chạy trên luồng giao diện (Event Dispatch Thread)
//đây là luồng đặc biệt mà Swing dùng để cập nhật và xử lý giao diện.

