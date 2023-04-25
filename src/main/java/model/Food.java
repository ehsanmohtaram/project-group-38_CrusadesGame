package model;

public enum Food {
    BREAD(10),MEAT(2),APPLE(4),BEER(2);
    private final int price;

    Food(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
