package model.building;

public enum SiegeType {
    MANGONEL(200, 3, 2, false),
    BALLISTA(400 ,5, 2, false),
    CATAPULT(200 , 3 , 2, true),
    TREBUCHET(400 , 6, 3 ,true);
    private final Integer damage;
    private final Integer fireRange;
    private final Integer engineerNeeded;
    private final Boolean isPortable;

    SiegeType(Integer damage, Integer fireRange, Integer engineerNeeded, Boolean isPortable) {
        this.damage = damage;
        this.fireRange = fireRange;
        this.engineerNeeded = engineerNeeded;
        this.isPortable = isPortable;
    }

    public Integer getDamage() {
        return damage;
    }

    public Integer getFireRange() {
        return fireRange;
    }

    public Integer getEngineerNeeded() {
        return engineerNeeded;
    }

    public Boolean getPortable() {
        return isPortable;
    }
}
