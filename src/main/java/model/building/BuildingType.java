package model.building;

import model.ResourceType;

public enum BuildingType {
    SMALL_STONE_GATEHOUSE(0,null,0,1000, null),
    CATHEDRAL(0,null,0,1000, null),
    BIG_STONE_GATEHOUSE(0,null,0,1000, null),
    DRAWBRIDGE(0,null,0,1000, null),
    CHURCH(0,null,0,1000, null),
    HEAD_QUARTER(0,null,0,10000, DefensiveStructureType.HEAD_QUARTER),
    BARRACK(0, ResourceType.ROCK,15,500, CampType.BARRACK),
    SQUARE_TOWER(0, ResourceType.ROCK,35,1600, DefensiveStructureType.SQUARE_TOWER),
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
