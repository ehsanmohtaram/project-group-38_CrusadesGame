package model.building;

public enum DefensiveStructureType {
    WALL(2, 4, false, false),
    BIG_WALL(2, 6, false, false),
    SMALL_STONE_GATEHOUSE(0, 6, false, true),
    BIG_STONE_GATEHOUSE(0, 10, false, true),
    DRAWBRIDGE(0,0, false , false),
    LOOKOUT_TOWER(5,8, false,false),
    PERIMETER_TOWER(3,12, true, true),
    DEFENSE_TURRET(4,8, false, false),
    SQUARE_TOWER(3,12, true ,true),
    ROUND_TOWER(3,15 , true, true),
    HEAD_QUARTER(2,20, false ,true);

    private final Integer furtherFireRange;
    private final Integer unitsCapacity;
    private final Boolean isHoldSiege;
    private final Boolean tunnelImmune;

    DefensiveStructureType(Integer furtherFireRange, Integer unitsCapacity, Boolean isHoldSiege, Boolean tunnelImmune) {
        this.furtherFireRange = furtherFireRange;
        this.unitsCapacity = unitsCapacity;
        this.isHoldSiege = isHoldSiege;
        this.tunnelImmune = tunnelImmune;
    }

    public Integer getFurtherFireRange() {
        return furtherFireRange;
    }

    public Integer getUnitsCapacity() {
        return unitsCapacity;
    }

    public Boolean getHoldSiege() {
        return isHoldSiege;
    }

    public Boolean getTunnelImmune() {
        return tunnelImmune;
    }
}
