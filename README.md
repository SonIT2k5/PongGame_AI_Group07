# **PONG GAME**



Game arcade bóng bàn kinh điển hỗ trợ chế độ chơi nhiều người (2 Player) hoặc đấu với máy (1 Player vs AI). Trò chơi đòi hỏi phản xạ nhanh nhạy khi tốc độ bóng tăng dần sau mỗi lần chạm vợt.

Viết hoàn toàn bằng Java + Swing (không dùng thư viện ngoài), áp dụng mô hình kiến trúc MVC (Model - View - Controller) với mã nguồn được tổ chức chặt chẽ.







##### **1. Yêu cầu môi trường**

* JDK 8 trở lên (đã test hoạt động mượt mà trên các phiên bản JDK mới).
* Máy có màn hình (chạy ứng dụng GUI).
* Hệ điều hành bất kỳ (Windows, macOS, Linux) do Java Swing hỗ trợ đa nền tảng.







##### **2. Cách chạy**

Cách 1 — Dùng IDE (Khuyến nghị)

* &#x09;IntelliJ IDEA / VS Code:
1. &#x09;	Mở thư mục gốc của dự án (thư mục chứa cả src và resources).
2. &#x09;	Đảm bảo IDE nhận diện src là thư mục chứa mã nguồn (Sources Root) và resources là thư mục tài nguyên.
3. &#x09;	Mở file src/Controller/PongGame.java và nhấn nút Run ▶ ở hàm main.

Cách 2 — Dòng lệnh (Terminal/CMD)

Mở terminal tại thư mục gốc của dự án:

\# Tạo thư mục out để chứa file .class

mkdir out



\# Biên dịch toàn bộ source code

javac -d out src/Controller/\*.java src/Model/\*.java src/View/\*.java



\# Chạy game (Linux/macOS)

java -cp out:resources Controller.PongGame



\# Chạy game (Windows)

java -cp "out;resources" Controller.PongGame

(Lưu ý: Dấu phân tách classpath trên Windows là ;, còn Linux/macOS là :).







##### **3. Điều khiển**

|Hành động|Player 1 (Trái)|Player 2 / AI (Phải)|
|-|-|-|
|Lên|W|↑|
|Xuống|S|↓|
|Tạm dừng/Tiếp|ESC|ESC|
|Thoát về Menu|Q|Q|



(Ở chế độ 1 PLAYER, vợt bên phải được điều khiển tự động bởi AI — bạn chỉ cần dùng W/S).







##### **4. Luật chơi**

**Mục tiêu**

Ghi điểm bằng cách đánh bóng lọt qua mép sân của đối thủ.

**Quy tắc trận đấu**

* Bóng sẽ xuất phát từ giữa sân với hướng và tốc độ ngẫu nhiên.
* Mỗi lần bóng va chạm vào vợt, tốc độ bóng sẽ tăng lên, đòi hỏi phản xạ nhanh hơn.
* Ghi điểm: Khi bóng vượt qua ranh giới màn hình của đối phương, bạn được cộng 1 điểm. Bóng và vợt sẽ reset về vị trí trung tâm.
* Điều kiện thắng: Đạt tối thiểu 3 điểm VÀ tạo được khoảng cách ít nhất 2 điểm so với đối thủ (Ví dụ: 3-1, 4-2, 5-3).
* Kết thúc trận đấu, tên người chiến thắng sẽ được tự động lưu vào file history.txt để xem lại trong mục "Lịch sử" ở Menu chính.







##### **5. Cấu trúc dự án**

Dự án được chia theo mô hình MVC:

PONG\_AI/

├── src/

│   ├── Controller/

│   │   └── PongGame.java          — Điểm khởi chạy chương trình (Main)

│   ├── Model/

│   │   ├── Ball.java              — Thực thể quả bóng (vị trí, tốc độ)

│   │   ├── Paddle.java            — Thực thể vợt (điều khiển phím)

│   │   └── Score.java             — Quản lý và vẽ điểm số

│   └── View/

│       ├── GameFrame.java         — Cửa sổ chính chứa game

│       ├── GamePanel.java         — Màn chơi (Game Loop 60FPS, xử lý va chạm)

│       ├── MainMenu.java          — Menu chính (Nút bấm, chuyển cảnh)

│       ├── ModeSelectionDialog... — Popup chọn chế độ (1P/2P)

│       ├── GameOverDialog.java    — Màn hình thông báo người thắng cuộc

│       ├── HistoryUtil.java       — Tiện ích đọc/ghi file lịch sử text

│       └── SoundPlayer.java       — Tiện ích phát âm thanh .wav

│

├── resources/

│   ├── images/                    — Hình nền, UI Menu, bảng luật chơi

│   └── sounds/                    — Âm thanh va chạm, nhạc nền, win game

└── history.txt                    — File lưu trữ lịch sử trận đấu (tự tạo)\\







##### **6. Thay đổi \& mở rộng**

**Đổi điều kiện chiến thắng**

Mở src/View/GamePanel.java, tìm và sửa các hằng số:

&#x09;private static final int WINNING\_SCORE = 3; // Số điểm tối thiểu để thắng

&#x09;private static final int MIN\_LEAD = 2;      // Khoảng cách điểm cách biệt



**Tùy chỉnh độ khó AI**

Trong src/View/GamePanel.java, tìm hàm aimove(). AI hiện tại bám theo tọa độ Y của bóng. Thay đổi biến speed để làm AI phản xạ nhanh hoặc chậm hơn:

&#x09;int speed = 5; // Tăng lên 7-8 để AI vô đối, giảm xuống 2-3 để AI dễ bị đánh bại



**Thay tài nguyên (Hình ảnh / Âm thanh)**

Chỉ cần thay thế các file trong thư mục resources/images/ hoặc resources/sounds/ bằng file của bạn cùng tên và định dạng.







##### **7. Xử lý sự cố**

**Lỗi "Error: Could not find or load main class Controller.PongGame"**

* Nguyên nhân: Do bạn chạy file chưa được biên dịch, hoặc sai classpath/package.
* Khắc phục: Nếu dùng IDE, hãy đảm bảo thư mục src được đánh dấu là Sources Root. Nếu dùng dòng lệnh, hãy đảm bảo bạn đang đứng ở thư mục gốc và gõ đúng lệnh java Controller.PongGame.



**Lỗi NullPointerException / Trắng màn hình / Không có tiếng**

* Nguyên nhân: Code không tìm thấy thư mục resources.
* Khắc phục: Đảm bảo thư mục resources nằm cùng cấp với thư mục src (chứ không nằm bên trong src). Trong IDE, hãy set thư mục gốc của project làm Working Directory.



**Không xem được lịch sử / Lỗi ghi file**

* Nguyên nhân: Ứng dụng không có quyền ghi file trong thư mục hiện tại.
* Khắc phục: Chạy IDE hoặc Terminal dưới quyền Administrator/quyền user có thể đọc ghi, ứng dụng sẽ tự động tạo file history.txt ở thư mục gốc.

