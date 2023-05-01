package model.building;

public enum StockType {
    ARMOURY(50);
    private final Integer CAPACITY;


    StockType(Integer CAPACITY) {
        this.CAPACITY = CAPACITY;
    }

    public Integer getCAPACITY() {
        return CAPACITY;
    }
}
