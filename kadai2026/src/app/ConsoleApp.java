package app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import model.Customer;
import model.Product;
import model.Reservation;
import service.CustomerService;
import service.ProductService;
import service.ReservationService;
import util.InputUtil;
import util.LogManager;

/**
 * CLI画面の表示とScannerによる入力受付を担当するクラス
 * 実際の業務処理はservice側のクラスに任せる
 */
public class ConsoleApp {

    private Scanner scanner;
    private InputUtil input;

    private ProductService productService;
    private CustomerService customerService;
    private ReservationService reservationService;

    public ConsoleApp(ProductService productService, CustomerService customerService,
            ReservationService reservationService) {
        this.scanner = new Scanner(System.in);
        this.input = new InputUtil(scanner);
        this.productService = productService;
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    /**
     * メインループ
     */
    public void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = input.readInt("番号を選んでください：");

            switch (choice) {
                case 1:
                    showProductList();
                    break;
                case 2:
                    registerReservation();
                    break;
                case 3:
                    showReservationList();
                    break;
                case 4:
                    updateReservation();
                    break;
                case 5:
                    cancelReservation();
                    break;
                case 6:
                    returnRegistration();
                    break;
                case 7:
                    registeredInfoMenu();
                    break;
                case 8:
                    updateProductInfo();
                    break;
                case 9:
                    running = false;
                    System.out.println("予約システムを終了します。");
                    break;
                default:
                    System.out.println("正しい番号を入力してください。");
                    break;
            }
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("---------------------------------");
        System.out.println("予約システム メインメニュー");
        System.out.println("1. 物品一覧");
        System.out.println("2. 予約登録");
        System.out.println("3. 予約一覧");
        System.out.println("4. 予約更新");
        System.out.println("5. 予約キャンセル");
        System.out.println("6. 返却登録");
        System.out.println("7. 登録情報参照");
        System.out.println("8. 物品情報更新");
        System.out.println("9. 終了");
        System.out.println("---------------------------------");
    }

    // ==================== 1.物品一覧 ====================

    private void showProductList() {
        System.out.println("== 物品一覧 ==");
        ArrayList<Product> list = productService.getAllSortedById();
        if (list.isEmpty()) {
            System.out.println("登録されている物品はありません。");
            return;
        }
        for (Product p : list) {
            System.out.println(p);
        }
    }

    // ==================== 2.予約登録 ====================

    private void registerReservation() {
        System.out.println("== 予約登録 ==");

        int productId = input.readInt("物品IDを入力してください：");
        Product product = productService.findById(productId);
        if (product == null) {
            System.out.println("指定した物品IDは存在しません。メインメニューへ戻ります。");
            return;
        }

        Customer customer = selectCustomer();
        if (customer == null) {
            System.out.println("メインメニューへ戻ります。");
            return;
        }

        LocalDate reservationDate = input.readDate("予約日を入力してください");
        LocalDate returnPlanDate = input.readDate("返却予定日を入力してください");

        if (returnPlanDate.isBefore(reservationDate)) {
            System.out.println("返却予定日は予約日より後の日付にしてください。登録を中止します。");
            return;
        }

        System.out.println("== 入力内容確認 ==");
        System.out.println("物品：" + product);
        System.out.println("顧客：" + customer);
        System.out.println("予約日：" + reservationDate);
        System.out.println("返却予定日：" + returnPlanDate);

        boolean ok = input.readYesNo("この内容で予約しますか？");
        if (!ok) {
            System.out.println("予約をキャンセルしました。メインメニューへ戻ります。");
            return;
        }

        Reservation reservation = reservationService.addReservation(
                product.getId(), customer.getId(), reservationDate, returnPlanDate);
        LogManager.log("予約登録", String.valueOf(reservation.getId()));

        System.out.println("予約が完了しました。予約ID：" + reservation.getId());
    }

    /**
     * 予約登録時の顧客選択処理
     * 1.登録済み顧客を使用 2.新規顧客登録 3.戻る
     */
    private Customer selectCustomer() {
        System.out.println("顧客を選択してください");
        System.out.println("1. 登録済み顧客を使用");
        System.out.println("2. 新規顧客登録");
        System.out.println("3. 戻る");
        int choice = input.readInt("番号を選んでください：");

        if (choice == 1) {
            return searchAndSelectCustomer();
        } else if (choice == 2) {
            return registerNewCustomer();
        } else {
            return null;
        }
    }

    /**
     * 顧客名で検索し、一覧から顧客IDを指定して選択する
     */
    private Customer searchAndSelectCustomer() {
        String keyword = input.readNonEmptyString("検索する顧客名（一部でも可）を入力してください：");
        ArrayList<Customer> result = customerService.searchByName(keyword);

        if (result.isEmpty()) {
            System.out.println("該当する顧客が見つかりませんでした。");
            return null;
        }

        System.out.println("== 検索結果 ==");
        for (Customer c : result) {
            System.out.println(c);
        }

        int id = input.readInt("使用する顧客の顧客IDを入力してください：");
        Customer selected = customerService.findById(id);
        if (selected == null) {
            System.out.println("指定した顧客IDは検索結果にありません。");
            return null;
        }
        return selected;
    }

    /**
     * 予約登録中に新規顧客を登録する
     */
    private Customer registerNewCustomer() {
        System.out.println("== 新規顧客登録 ==");
        String name = input.readNonEmptyString("名前を入力してください：");
        String phone = input.readNonEmptyString("電話番号を入力してください：");
        String email = input.readNonEmptyString("メールアドレスを入力してください：");

        Customer customer = customerService.addCustomer(name, phone, email);
        LogManager.log("顧客登録", String.valueOf(customer.getId()));

        System.out.println("顧客を登録しました。顧客ID：" + customer.getId());
        return customer;
    }

    // ==================== 3.予約一覧 ====================

    private void showReservationList() {
        System.out.println("== 予約一覧 ==");
        ArrayList<Reservation> list = reservationService.getAllSortedByReservationDate();
        if (list.isEmpty()) {
            System.out.println("登録されている予約はありません。");
            return;
        }
        for (Reservation r : list) {
            System.out.println(r);
        }
    }

    // ==================== 4.予約更新 ====================

    private void updateReservation() {
        System.out.println("== 予約更新 ==");
        int id = input.readInt("予約IDを入力してください：");
        Reservation reservation = reservationService.findById(id);

        if (reservation == null) {
            System.out.println("指定した予約IDは存在しません。メインメニューへ戻ります。");
            return;
        }

        System.out.println("現在の予約情報：" + reservation);

        LocalDate newReservationDate = input.readDate("新しい予約日を入力してください");
        LocalDate newReturnPlanDate = input.readDate("新しい返却予定日を入力してください");

        if (newReturnPlanDate.isBefore(newReservationDate)) {
            System.out.println("返却予定日は予約日より後の日付にしてください。更新を中止します。");
            return;
        }

        System.out.println("== 更新内容確認 ==");
        System.out.println("予約日：" + newReservationDate);
        System.out.println("返却予定日：" + newReturnPlanDate);

        boolean ok = input.readYesNo("この内容で更新しますか？");
        if (!ok) {
            System.out.println("更新をキャンセルしました。メインメニューへ戻ります。");
            return;
        }

        reservationService.updateReservation(id, newReservationDate, newReturnPlanDate);
        LogManager.log("予約更新", String.valueOf(id));

        System.out.println("予約を更新しました。");
    }

    // ==================== 5.予約キャンセル ====================

    private void cancelReservation() {
        System.out.println("== 予約キャンセル ==");
        int id = input.readInt("予約IDを入力してください：");
        Reservation reservation = reservationService.findById(id);

        if (reservation == null) {
            System.out.println("指定した予約IDは存在しません。メインメニューへ戻ります。");
            return;
        }

        System.out.println("キャンセル対象：" + reservation);
        boolean ok = input.readYesNo("この予約をキャンセルしますか？");

        if (!ok) {
            System.out.println("キャンセルしませんでした。メインメニューへ戻ります。");
            return;
        }

        reservationService.cancelReservation(id);
        LogManager.log("予約キャンセル", String.valueOf(id));

        System.out.println("予約をキャンセルしました。");
    }

    // ==================== 6.返却登録 ====================

    private void returnRegistration() {
        System.out.println("== 返却登録 ==");
        int id = input.readInt("予約IDを入力してください：");
        Reservation reservation = reservationService.findById(id);

        if (reservation == null) {
            System.out.println("指定した予約IDは存在しません。メインメニューへ戻ります。");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        reservationService.returnReservation(id, now);
        LogManager.log("返却登録", String.valueOf(id));

        System.out.println("返却を登録しました。返却日時：" + now);
    }

    // ==================== 7.登録情報参照 ====================

    private void registeredInfoMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("== 登録情報参照 ==");
            System.out.println("1. 顧客情報");
            System.out.println("2. 物品情報");
            System.out.println("3. 物品登録");
            System.out.println("4. 物品削除");
            System.out.println("5. 戻る");
            int choice = input.readInt("番号を選んでください：");

            switch (choice) {
                case 1:
                    customerInfoMenu();
                    break;
                case 2:
                    productInfoMenu();
                    break;
                case 3:
                    registerProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("正しい番号を入力してください。");
                    break;
            }
        }
    }

    private void customerInfoMenu() {
        System.out.println("== 顧客情報 ==");
        System.out.println("1. 顧客一覧");
        System.out.println("2. 顧客ID検索");
        System.out.println("3. 戻る");
        int choice = input.readInt("番号を選んでください：");

        if (choice == 1) {
            ArrayList<Customer> list = customerService.getAllSortedById();
            if (list.isEmpty()) {
                System.out.println("登録されている顧客はいません。");
            } else {
                for (Customer c : list) {
                    System.out.println(c);
                }
            }
        } else if (choice == 2) {
            int id = input.readInt("顧客IDを入力してください：");
            Customer customer = customerService.findById(id);
            if (customer == null) {
                System.out.println("指定した顧客IDは存在しません。");
            } else {
                System.out.println(customer);
            }
        }
    }

    private void productInfoMenu() {
        System.out.println("== 物品情報 ==");
        System.out.println("1. 物品一覧");
        System.out.println("2. 物品ID検索");
        System.out.println("3. 戻る");
        int choice = input.readInt("番号を選んでください：");

        if (choice == 1) {
            showProductList();
        } else if (choice == 2) {
            int id = input.readInt("物品IDを入力してください：");
            Product product = productService.findById(id);
            if (product == null) {
                System.out.println("指定した物品IDは存在しません。");
            } else {
                System.out.println(product);
            }
        }
    }

    /**
     * 物品登録（Create要件を満たすための機能）
     */
    private void registerProduct() {
        System.out.println("== 物品登録 ==");
        String name = input.readNonEmptyString("物品名を入力してください：");
        String type = input.readNonEmptyString("種類を入力してください（例：バイク、ヘルメット）：");

        Product product = productService.addProduct(name, type);
        System.out.println("物品を登録しました。物品ID：" + product.getId());
    }

    /**
     * 物品削除
     * その物品に未返却の予約が存在する場合は削除できない
     */
    private void deleteProduct() {
        System.out.println("== 物品削除 ==");
        int id = input.readInt("物品IDを入力してください：");
        Product product = productService.findById(id);

        if (product == null) {
            System.out.println("指定した物品IDは存在しません。");
            return;
        }

        if (reservationService.hasActiveReservationForProduct(id)) {
            System.out.println("この物品には未返却の予約があるため削除できません。");
            return;
        }

        System.out.println("削除対象：" + product);
        boolean ok = input.readYesNo("この物品を削除しますか？");
        if (!ok) {
            System.out.println("削除しませんでした。");
            return;
        }

        productService.deleteProduct(id);
        LogManager.log("物品削除", String.valueOf(id));

        System.out.println("物品を削除しました。");
    }

    // ==================== 8.物品情報更新 ====================

    private void updateProductInfo() {
        System.out.println("== 物品情報更新 ==");
        int id = input.readInt("物品IDを入力してください：");
        Product product = productService.findById(id);

        if (product == null) {
            System.out.println("指定した物品IDは存在しません。メインメニューへ戻ります。");
            return;
        }

        System.out.println("現在の物品情報：" + product);

        String newName = input.readNonEmptyString("新しい物品名を入力してください：");
        String newType = input.readNonEmptyString("新しい種類を入力してください：");

        System.out.println("== 更新内容確認 ==");
        System.out.println("物品名：" + newName);
        System.out.println("種類：" + newType);

        boolean ok = input.readYesNo("この内容で更新しますか？");
        if (!ok) {
            System.out.println("更新をキャンセルしました。メインメニューへ戻ります。");
            return;
        }

        productService.updateProduct(id, newName, newType);
        LogManager.log("物品更新", String.valueOf(id));

        System.out.println("物品情報を更新しました。");
    }
}
