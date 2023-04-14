package model.building;

import model.Resource;

public enum BuildingType {
    BARRACK(0,Resource.ROCK,15,500, CampType.BARRACK),
    SQUARE_TOWER(0,Resource.ROCK,35,1600, DefensiveStructureType.SQUARE_TOWER),
    SMALL_STONE_GATEHOUSE(0,null,0,1000, null),
    FLETCHER(100,Resource.WOOD,20,300, GeneralBuildingType.FLETCHER),
    ARMOURY(0,Resource.WOOD,5,300, StockType.ARMOURY);
    public final Integer GOLD;
    public final Resource RESOURCES;
    public final Integer RESOURCE_NUMBER;
    public final Integer HP_IN_FIRST;
    public final Enum<?> specificConstant;

    BuildingType(Integer GOLD, Resource RESOURCES, Integer RESOURCE_NUMBER, Integer HP_IN_FIRST, Enum<?> specificConstant) {
        this.GOLD = GOLD;
        this.RESOURCES = RESOURCES;
        this.RESOURCE_NUMBER = RESOURCE_NUMBER;
        this.HP_IN_FIRST = HP_IN_FIRST;
        this.specificConstant = specificConstant;
    }


}
