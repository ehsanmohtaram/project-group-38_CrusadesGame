package model;

public enum Food {
    BREAD(50),MEAT(40),APPLE(30),BEER(80);

    private int price;

    Food(int price) {
        this.price = price;
    }
}
