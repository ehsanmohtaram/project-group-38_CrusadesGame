package model.building;

public enum DefensiveStructureType {
    SMALL_STONE_GATEHOUSE(0,0),
    BIG_STONE_GATEHOUSE(0,0),
    DRAWBRIDGE(0,0),
    LOOKOUT_TOWER(20,30),
    PERIMETER_TOWER(8,10),
    DEFENSE_TURRET(8,10),
    SQUARE_TOWER(10,15),
    ROUND_TOWER(10,15),
    HEAD_QUARTER(20,20);

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
