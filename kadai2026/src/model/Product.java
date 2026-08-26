package model;

/**
 * レンタルする物品（バイクやヘルメットなど）を表すクラス
 */
public class Product {

    private int id;
    private String name;
    private String type;

    public Product(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ID:" + id + " 物品名:" + name + " 種類:" + type;
    }
}
