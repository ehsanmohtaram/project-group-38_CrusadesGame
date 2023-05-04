package model;

public enum Animal {
    COW(5),
    HORSE(7),
    DOG(3);
    private int price;

    Animal(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
