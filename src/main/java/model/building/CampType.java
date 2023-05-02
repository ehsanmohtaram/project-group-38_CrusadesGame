package model.building;

public enum CampType {
    BARRACK(20, 0),
    SIEGE_TENT(5, -1),
    ENGINEER_GUILD(10 ,-1),
    MERCENARY_POST(20, 1);
    private final Integer capacity;
    private final Integer isArab;

    CampType(Integer capacity, Integer isArab) {
        this.capacity = capacity;
        this.isArab = isArab;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getIsArab() {
        return isArab;
    }
}
