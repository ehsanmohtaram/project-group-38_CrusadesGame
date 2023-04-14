package model;

public enum Resource {
    WOOD(10),
    ROCK(20);
    private int price;

    Resource(int price) {
        this.price = price;
    }
}
