package model.building;

import model.Land;
import model.Resource;

import java.util.HashMap;

public class Building {
    protected Integer hp;
    private Land position;
    private BuildingType buildingType;
    public Building(Land position, BuildingType buildingType) {
        this.position = position;
        this.buildingType = buildingType;
        this.hp = buildingType.HP_IN_FIRST;
    }

    public static BuildingType findEnumByBuildingName(String buildingName) {
        for (BuildingType searchForBuilding : BuildingType.values())
            if (searchForBuilding.name().toLowerCase().replaceAll("_"," ").equals(buildingName))
                return searchForBuilding;
        return null;
    }
    //TODO category var may be needed

    public Integer getGOLD() {
        return buildingType.GOLD;
    }

    public Resource getRESOURCES() {
        return buildingType.RESOURCES;
    }

    public Integer getRESOURCE_NUMBER() {
        return buildingType.RESOURCE_NUMBER;
    }

    public Integer getHP_IN_FIRST() {
        return buildingType.HP_IN_FIRST;
    }

    public Enum<?> getSpecificConstant() {
        return buildingType.specificConstant;
    }

    public Integer getHp() {
        return hp;
    }

    public void damage(Integer amount) {
        this.hp = hp - amount;
    }

    public Land getPosition() {
        return position;
    }

    public void setPosition(Land position) {
        this.position = position;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
}
