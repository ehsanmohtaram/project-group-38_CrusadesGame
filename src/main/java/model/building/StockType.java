package model.building;

public enum StockType {
    ARMOURY(50),
    FOOD_STOCKPILE(100),
    STOCKPILE(120);
    private final Integer CAPACITY;


    StockType(Integer CAPACITY) {
        this.CAPACITY = CAPACITY;
    }

    public Integer getCAPACITY() {
        return CAPACITY;
    }

}
