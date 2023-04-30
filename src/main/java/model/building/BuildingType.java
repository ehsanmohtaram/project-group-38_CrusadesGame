package model.building;

import model.ResourceType;

public enum BuildingType {
    CATHEDRAL(0,null,0,1000, null),
    CHURCH(0,null,0,1000, null),
    //Defensive structure
    HEAD_QUARTER(0,null,0,10000, DefensiveStructureType.HEAD_QUARTER),
    WALL(0,ResourceType.ROCK, 5,200, DefensiveStructureType.WALL),
    BIG_WALL(0,ResourceType.ROCK, 10 ,400, DefensiveStructureType.BIG_WALL),
    SMALL_STONE_GATEHOUSE(0,null,0,1000, DefensiveStructureType.HEAD_QUARTER),
    BIG_STONE_GATEHOUSE(0,ResourceType.ROCK,20,2000,DefensiveStructureType.BIG_STONE_GATEHOUSE),
    DRAWBRIDGE(0,ResourceType.WOOD,10,100, DefensiveStructureType.DRAWBRIDGE),
    LOOKOUT_TOWER(0, ResourceType.ROCK, 10, 2000,DefensiveStructureType.LOOKOUT_TOWER),
    PERIMETER_TOWER(0,ResourceType.ROCK,10,1000, DefensiveStructureType.PERIMETER_TOWER),
    DEFENSE_TURRET(0,ResourceType.ROCK,15,1200, DefensiveStructureType.PERIMETER_TOWER),
    SQUARE_TOWER(0, ResourceType.ROCK,35,1600, DefensiveStructureType.SQUARE_TOWER),
    ROUND_TOWER(0,ResourceType.ROCK,40,2000, DefensiveStructureType.ROUND_TOWER),
    //Siege equipment
    MANGONEL(50, null, 0, 100, SiegeType.MANGONEL),
    BALLISTA(50, null, 0, 100, SiegeType.BALLISTA),
    CATAPULT(150, null, 0, 300, SiegeType.CATAPULT),
    TREBUCHET(150, null, 0, 500, SiegeType.TREBUCHET),
    //Camp equipment
    BARRACK(0, ResourceType.ROCK,15,500, CampType.BARRACK),
    FLETCHER(100, ResourceType.WOOD,20,300, GeneralBuildingType.FLETCHER),
    ARMOURY(0, ResourceType.WOOD,5,300, StockType.ARMOURY);
    private final Integer GOLD;
    private final ResourceType RESOURCES;
    private final Integer RESOURCE_NUMBER;
    private final Integer HP_IN_FIRST;
    public final Enum<?> specificConstant;

    BuildingType(Integer GOLD, ResourceType RESOURCES, Integer RESOURCE_NUMBER, Integer HP_IN_FIRST, Enum<?> specificConstant) {
        this.GOLD = GOLD;
        this.RESOURCES = RESOURCES;
        this.RESOURCE_NUMBER = RESOURCE_NUMBER;
        this.HP_IN_FIRST = HP_IN_FIRST;
        this.specificConstant = specificConstant;
    }

    public Integer getGOLD() {
        return GOLD;
    }

    public ResourceType getRESOURCES() {
        return RESOURCES;
    }

    public Integer getRESOURCE_NUMBER() {
        return RESOURCE_NUMBER;
    }

    public Integer getHP_IN_FIRST() {
        return HP_IN_FIRST;
    }
}
