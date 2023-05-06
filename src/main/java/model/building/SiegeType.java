package model.building;

public enum SiegeType {
    MANGONEL(200, 3,false),
    BALLISTA(400 ,5, false),
    CATAPULT(200 , 3,true),
    TREBUCHET(400 , 6,true);
    private final Integer damage;
    private final Integer fireRange;
    private final Boolean isPortable;

    SiegeType(Integer damage, Integer fireRange, Boolean isPortable) {
        this.damage = damage;
        this.fireRange = fireRange;
        this.isPortable = isPortable;
    }

    public Integer getDamage() {
        return damage;
    }

    public Integer getFireRange() {
        return fireRange;
    }

    public Boolean getPortable() {
        return isPortable;
    }
}
