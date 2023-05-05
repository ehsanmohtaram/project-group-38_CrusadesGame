package model;

public enum ResourceType {
    WOOD(2),
    ROCK(3),
    IRON(5),
    WHEAT(1),
    FLOUR(2),
    LEATHER(3),
    OIL(2);
    private final int price;

    ResourceType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
