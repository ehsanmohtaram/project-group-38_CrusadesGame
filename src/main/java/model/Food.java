package model;

public enum Food {
    BREAD(2),MEAT(10),APPLE(1),CHEESE(3);
    private final int price;

    Food(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
