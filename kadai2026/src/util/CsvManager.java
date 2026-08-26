package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import model.Customer;
import model.Product;
import model.Reservation;

/**
 * 物品・顧客・予約データのCSV読み込み・書き込みを行うクラス
 * 外部ライブラリは使わず、Java標準のI/Oだけで処理する
 */
public class CsvManager {

    private static final String PRODUCT_FILE = "products.csv";
    private static final String CUSTOMER_FILE = "customers.csv";
    private static final String RESERVATION_FILE = "reservations.csv";

    /**
     * 物品データを読み込む。ファイルが無ければ空のリストを返す。
     */
    public ArrayList<Product> loadProducts() {
        ArrayList<Product> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split(",", -1);
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String type = data[2];
                list.add(new Product(id, name, type));
            }
        } catch (IOException e) {
            // ファイルが無い場合は初回起動とみなし、空リストのまま処理を続ける
        }
        return list;
    }

    public void saveProducts(ArrayList<Product> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product p : list) {
                writer.write(p.getId() + "," + p.getName() + "," + p.getType());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("物品データの保存に失敗しました。");
        }
    }

    /**
     * 顧客データを読み込む。ファイルが無ければ空のリストを返す。
     */
    public ArrayList<Customer> loadCustomers() {
        ArrayList<Customer> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split(",", -1);
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String phone = data[2];
                String email = data[3];
                list.add(new Customer(id, name, phone, email));
            }
        } catch (IOException e) {
            // 初回起動時はファイルが無いので何もしない
        }
        return list;
    }

    public void saveCustomers(ArrayList<Customer> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (Customer c : list) {
                writer.write(c.getId() + "," + c.getName() + "," + c.getPhone() + "," + c.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("顧客データの保存に失敗しました。");
        }
    }

    /**
     * 予約データを読み込む。ファイルが無ければ空のリストを返す。
     */
    public ArrayList<Reservation> loadReservations() {
        ArrayList<Reservation> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVATION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split(",", -1);
                int id = Integer.parseInt(data[0]);
                int productId = Integer.parseInt(data[1]);
                int customerId = Integer.parseInt(data[2]);
                LocalDate reservationDate = LocalDate.parse(data[3]);
                LocalDate returnPlanDate = LocalDate.parse(data[4]);

                Reservation reservation = new Reservation(id, productId, customerId, reservationDate, returnPlanDate);

                if (data.length > 5 && !data[5].isEmpty()) {
                    reservation.setReturnDateTime(LocalDateTime.parse(data[5]));
                }
                list.add(reservation);
            }
        } catch (IOException e) {
            // 初回起動時はファイルが無いので何もしない
        }
        return list;
    }

    public void saveReservations(ArrayList<Reservation> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESERVATION_FILE))) {
            for (Reservation r : list) {
                String returnDateTimeText = (r.getReturnDateTime() == null) ? "" : r.getReturnDateTime().toString();
                writer.write(r.getId() + "," + r.getProductId() + "," + r.getCustomerId() + ","
                        + r.getReservationDate() + "," + r.getReturnPlanDate() + "," + returnDateTimeText);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("予約データの保存に失敗しました。");
        }
    }
}
