package model;

public enum Resource {
    WOOD(10);
    private int price;

    Resource(int price) {
        this.price = price;
    }
}
