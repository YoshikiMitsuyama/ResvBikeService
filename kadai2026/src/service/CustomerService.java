package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Customer;

/**
 * 顧客の登録・参照を行うクラス
 */
public class CustomerService {

    private ArrayList<Customer> customerList = new ArrayList<>();
    private int nextId = 1;

    /**
     * CSVから読み込んだデータをセットする（起動時に使用）
     */
    public void setAll(ArrayList<Customer> list) {
        this.customerList = list;
        int maxId = 0;
        for (Customer c : customerList) {
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        this.nextId = maxId + 1;
    }

    public ArrayList<Customer> getAll() {
        return customerList;
    }

    /**
     * 顧客を新規登録する。IDは自動採番。
     */
    public Customer addCustomer(String name, String phone, String email) {
        Customer customer = new Customer(nextId, name, phone, email);
        customerList.add(customer);
        nextId++;
        return customer;
    }

    /**
     * IDで顧客を検索する。見つからなければnullを返す。
     */
    public Customer findById(int id) {
        for (Customer c : customerList) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    /**
     * 名前の一部が一致する顧客を検索する（部分一致）
     */
    public ArrayList<Customer> searchByName(String keyword) {
        ArrayList<Customer> result = new ArrayList<>();
        for (Customer c : customerList) {
            if (c.getName().contains(keyword)) {
                result.add(c);
            }
        }
        return result;
    }

    /**
     * 顧客ID昇順で並び替えた一覧を返す
     */
    public ArrayList<Customer> getAllSortedById() {
        ArrayList<Customer> sorted = new ArrayList<>(customerList);
        Collections.sort(sorted, new Comparator<Customer>() {
            @Override
            public int compare(Customer c1, Customer c2) {
                return c1.getId() - c2.getId();
            }
        });
        return sorted;
    }
}
