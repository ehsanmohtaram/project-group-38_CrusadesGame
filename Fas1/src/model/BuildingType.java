package model;

public enum BuildingType {
    ;
    private final Integer HP_IN_START;
    private final Integer FIRE_RANGE;
    private final Integer DAMAGE;
    private final Integer RATE;
    private final Integer CAPACITY;

    BuildingType(Integer HP_IN_START, Integer FIRE_RANGE, Integer DAMAGE, Integer RATE, Integer CAPACITY) {
        this.HP_IN_START = HP_IN_START;
        this.FIRE_RANGE = FIRE_RANGE;
        this.DAMAGE = DAMAGE;
        this.RATE = RATE;
        this.CAPACITY = CAPACITY;
    }
}
