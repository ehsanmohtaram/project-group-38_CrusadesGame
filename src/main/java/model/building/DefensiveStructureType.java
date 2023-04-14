package model.building;

public enum DefensiveStructureType {
    SQUARE_TOWER(10,15);
    public final Integer FIRE_RANG;
    public final Integer DEFEND_RANGE;

    DefensiveStructureType(Integer FIRE_RANG, Integer DEFEND_RANGE) {
        this.FIRE_RANG = FIRE_RANG;
        this.DEFEND_RANGE = DEFEND_RANGE;
    }

}
