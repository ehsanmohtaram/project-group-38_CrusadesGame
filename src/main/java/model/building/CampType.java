package model.building;

public enum CampType {
    BARRACK(20, false),
    SIEGE_TENT(5, null),
    ENGINEER_GUILD(10 ,null),
    MERCENARY_POST(20, true);
    private final Integer capacity;
    private final Boolean isArab;

    CampType(Integer capacity, Boolean isArab) {
        this.capacity = capacity;
        this.isArab = isArab;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Boolean getArab() {
        return isArab;
    }
}
