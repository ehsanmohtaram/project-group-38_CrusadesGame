package model.building;

public enum DefensiveStructureType {
    SQUARE_TOWER(10,15);
    private final Integer FIRE_RANG;
    private final Integer DEFEND_RANGE;

    DefensiveStructureType(Integer FIRE_RANG, Integer DEFEND_RANGE) {
        this.FIRE_RANG = FIRE_RANG;
        this.DEFEND_RANGE = DEFEND_RANGE;
    }
}
