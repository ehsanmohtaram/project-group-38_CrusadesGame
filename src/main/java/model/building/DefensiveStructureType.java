package model.building;

public enum DefensiveStructureType {
    HEAD_QUARTER(20,20),
    SQUARE_TOWER(10,15);
    private final Integer FIRE_RANG;
    private final Integer DEFEND_RANGE;

    DefensiveStructureType(Integer FIRE_RANG, Integer DEFEND_RANGE) {
        this.FIRE_RANG = FIRE_RANG;
        this.DEFEND_RANGE = DEFEND_RANGE;
    }

    public Integer getFIRE_RANG() {
        return FIRE_RANG;
    }

    public Integer getDEFEND_RANGE() {
        return DEFEND_RANGE;
    }
}
