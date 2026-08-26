package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 簡単なログをファイルに出力するクラス
 * ログ対象：予約登録、顧客登録、予約更新、物品更新、予約キャンセル、物品削除、返却登録
 */
public class LogManager {

    private static final String LOG_FILE = "log.txt";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * ログを1行追記する
     * @param action 処理内容（例：予約登録）
     * @param targetId 対象のID
     */
    public static void log(String action, String targetId) {
        String time = LocalDateTime.now().format(FORMAT);
        String line = time + " " + action + " 対象ID:" + targetId;

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(line);
        } catch (IOException e) {
            System.out.println("ログの書き込みに失敗しました。");
        }
    }
}
