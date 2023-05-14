package model.building;

public enum TrapType {
    DOGS_CAGE(100, 10), DEATH_TRENCH(150, 1000), BITUMEN_TRENCH(80, 50);
    private int price;
    private int damage;

    TrapType(int price , int damage) {
        this.price = price;
        this.damage = damage;
    }

    public int getPrice() {
        return price;
    }

    public int getDamage() {
        return damage;
    }
}
