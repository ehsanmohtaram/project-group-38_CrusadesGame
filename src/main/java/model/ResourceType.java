package model;

public enum ResourceType {
    WOOD(10),
    ROCK(20);
    private int price;

    ResourceType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
