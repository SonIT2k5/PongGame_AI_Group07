import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class HistoryUtil {
    private static final String HISTORY_FILE = "history.txt";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Ghi một dòng lịch sử: [timestamp] - winner
    public static void append(String winner) {
        String line = String.format("%s - %s", LocalDateTime.now().format(FMT), winner);
        try (BufferedWriter out = Files.newBufferedWriter(
                Paths.get(HISTORY_FILE),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            out.write(line);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Đọc tất cả dòng lịch sử
    public static List<String> readAll() {
        try {
            return Files.readAllLines(Paths.get(HISTORY_FILE));
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
