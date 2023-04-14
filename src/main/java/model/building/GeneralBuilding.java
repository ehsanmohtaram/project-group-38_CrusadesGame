package model.building;

import model.Land;
import model.Resource;

public class GeneralBuilding extends Building{
    public GeneralBuilding(Land position, BuildingType buildingType) {
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

    public Resource getRESOURCE_NEEDED() {
        GeneralBuildingType generalBuildingType = (GeneralBuildingType) getBuildingType().specificConstant;
        return generalBuildingType.RESOURCE_NEEDED;
    }

    public Integer getAMOUNT_OF_RESOURCE() {
        GeneralBuildingType generalBuildingType = (GeneralBuildingType) getBuildingType().specificConstant;
        return generalBuildingType.AMOUNT_OF_RESOURCE;
    }
}
