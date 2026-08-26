package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Product;

/**
 * 物品（バイク・ヘルメットなど）の登録・参照・更新・削除を行うクラス
 */
public class ProductService {

    private ArrayList<Product> productList = new ArrayList<>();
    private int nextId = 1;

    /**
     * CSVから読み込んだデータをセットする（起動時に使用）
     */
    public void setAll(ArrayList<Product> list) {
        this.productList = list;
        int maxId = 0;
        for (Product p : productList) {
            if (p.getId() > maxId) {
                maxId = p.getId();
            }
        }
        this.nextId = maxId + 1;
    }

    public ArrayList<Product> getAll() {
        return productList;
    }

    /**
     * 物品を新規登録する。IDは自動採番。
     */
    public Product addProduct(String name, String type) {
        Product product = new Product(nextId, name, type);
        productList.add(product);
        nextId++;
        return product;
    }

    /**
     * IDで物品を検索する。見つからなければnullを返す。
     */
    public Product findById(int id) {
        for (Product p : productList) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * ID昇順で並び替えた一覧を返す
     */
    public ArrayList<Product> getAllSortedById() {
        ArrayList<Product> sorted = new ArrayList<>(productList);
        Collections.sort(sorted, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return p1.getId() - p2.getId();
            }
        });
        return sorted;
    }

    /**
     * 物品名と種類を更新する
     */
    public boolean updateProduct(int id, String newName, String newType) {
        Product product = findById(id);
        if (product == null) {
            return false;
        }
        product.setName(newName);
        product.setType(newType);
        return true;
    }

    /**
     * 物品を削除する
     */
    public boolean deleteProduct(int id) {
        Product product = findById(id);
        if (product == null) {
            return false;
        }
        productList.remove(product);
        return true;
    }
}
