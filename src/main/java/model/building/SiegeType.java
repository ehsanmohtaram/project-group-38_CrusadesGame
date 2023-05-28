package model.building;

public enum SiegeType {
    MANGONEL(200, 3,false, 0),
    BALLISTA(400,5, false, 0),
    CATAPULT(200, 3,true, 3),
    TREBUCHET(400, 6,false, 0),
    SIEGE_TOWER(0, 4, true, 2),
    BATTERING_RAM(300, 1, true, 2),
    MANTLET(0 ,0, true, 6),
    FIRE_BALLISTA(400, 6, true, 3);
    private final Integer damage;
    private final Integer fireRange;
    private final Boolean isPortable;
    private final Integer moveRange;

    SiegeType(Integer damage, Integer fireRange, Boolean isPortable, Integer moveRange) {
        this.damage = damage;
        this.fireRange = fireRange;
        this.isPortable = isPortable;
        this.moveRange = moveRange;
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

    public Integer getMoveRange() {
        return moveRange;
    }
}
