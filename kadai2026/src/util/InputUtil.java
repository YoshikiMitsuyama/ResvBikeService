package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Scannerを使った共通の入力処理をまとめたクラス
 * 入力チェックはここに集約する
 */
public class InputUtil {

    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public InputUtil(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * 空文字を許可しない文字列入力
     */
    public String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("空文字は入力できません。もう一度入力してください。");
                continue;
            }
            return input;
        }
    }

    /**
     * 整数入力（メニュー番号やIDなど）
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("数値を入力してください。");
            }
        }
    }

    /**
     * 日付入力（yyyy-MM-dd形式）
     */
    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + "（例:2026-08-26）：");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("空文字は入力できません。もう一度入力してください。");
                continue;
            }
            try {
                return LocalDate.parse(input, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("日付の形式が正しくありません。yyyy-MM-dd形式で入力してください。");
            }
        }
    }

    /**
     * はい/いいえの確認入力
     * y または yes で true、それ以外はfalse
     */
    public boolean readYesNo(String prompt) {
        System.out.print(prompt + "（y/n）：");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    /**
     * 任意の文字列（電話番号やメールなど）の簡易チェック付き入力
     * 空文字のみチェックする
     */
    public String readString(String prompt) {
        return readNonEmptyString(prompt);
    }
}
