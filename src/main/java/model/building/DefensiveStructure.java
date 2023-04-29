package model.building;

import model.Kingdom;
import model.MapBlock;

public class DefensiveStructure extends Building{
    public DefensiveStructure(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType,owner);
    }

}
