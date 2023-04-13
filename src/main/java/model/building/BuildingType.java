package model.building;

import model.Resource;

public enum BuildingType {
    SMALL_STONE_GATEHOUSE(0,null,0,1000);
    private final Integer GOLD;
    private final Resource RESOURCES;
    private final Integer RESOURCE_NUMBER;
    private final Integer HP_IN_FIRST;

    BuildingType(Integer GOLD, Resource RESOURCES, Integer RESOURCE_NUMBER, Integer HP_IN_FIRST) {
        this.GOLD = GOLD;
        this.RESOURCES = RESOURCES;
        this.RESOURCE_NUMBER = RESOURCE_NUMBER;
        this.HP_IN_FIRST = HP_IN_FIRST;
    }

    public Integer getGOLD() {
        return GOLD;
    }

    public Resource getRESOURCES() {
        return RESOURCES;
    }

    public Integer getRESOURCE_NUMBER() {
        return RESOURCE_NUMBER;
    }

    public Integer getHP_IN_FIRST() {
        return HP_IN_FIRST;
    }
}
