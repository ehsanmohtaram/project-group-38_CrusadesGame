package model.building;

import model.MapBlock;
import model.ResourceType;

public class GeneralBuilding extends Building{
    public GeneralBuilding(MapBlock position, BuildingType buildingType) {
        super(position, buildingType);
    }

    public Integer getNUMBER_OF_WORKER() {
        GeneralBuildingType generalBuildingType = (GeneralBuildingType) getBuildingType().specificConstant;
        return generalBuildingType.NUMBER_OF_WORKER;
    }

    public Integer getRATE() {
        GeneralBuildingType generalBuildingType = (GeneralBuildingType) getBuildingType().specificConstant;
        return generalBuildingType.RATE;
    }

    public ResourceType getRESOURCE_NEEDED() {
        GeneralBuildingType generalBuildingType = (GeneralBuildingType) getBuildingType().specificConstant;
        return generalBuildingType.RESOURCE_Type_NEEDED;
    }

    public Integer getAMOUNT_OF_RESOURCE() {
        GeneralBuildingType generalBuildingType = (GeneralBuildingType) getBuildingType().specificConstant;
        return generalBuildingType.AMOUNT_OF_RESOURCE;
    }
}
