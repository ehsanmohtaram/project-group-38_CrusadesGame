package model.building;

import model.Land;

public class DefensiveStructure extends Building{
    public DefensiveStructure(Land position, BuildingType buildingType) {
        super(position, buildingType);
    }

    public Integer getFIRE_RANG() {
        DefensiveStructureType defensiveStructureType = (DefensiveStructureType) getBuildingType().specificConstant;
        return defensiveStructureType.FIRE_RANG;
    }

    public Integer getDEFEND_RANGE() {
        DefensiveStructureType defensiveStructureType = (DefensiveStructureType) getBuildingType().specificConstant;
        return defensiveStructureType.DEFEND_RANGE;
    }
}
