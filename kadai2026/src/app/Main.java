package app;

import service.CustomerService;
import service.ProductService;
import service.ReservationService;
import util.CsvManager;

/**
 * アプリケーションのエントリーポイント
 * 起動時にCSVからデータを読み込み、終了時にCSVへ保存する
 */
public class Main {

    public static void main(String[] args) {

        CsvManager csvManager = new CsvManager();

        ProductService productService = new ProductService();
        CustomerService customerService = new CustomerService();
        ReservationService reservationService = new ReservationService();

        // 起動時にCSVを読み込む（ファイルが無ければ空のまま）
        productService.setAll(csvManager.loadProducts());
        customerService.setAll(csvManager.loadCustomers());
        reservationService.setAll(csvManager.loadReservations());

        ConsoleApp app = new ConsoleApp(productService, customerService, reservationService);
        app.run();

        // 終了時にCSVへ保存する
        csvManager.saveProducts(productService.getAll());
        csvManager.saveCustomers(customerService.getAll());
        csvManager.saveReservations(reservationService.getAll());

        System.out.println("データを保存しました。");
    }
}
