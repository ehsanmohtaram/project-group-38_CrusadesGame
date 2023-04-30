package model.building;

import model.Kingdom;
import model.MapBlock;

public class GeneralBuilding extends Building{
    public GeneralBuilding(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
    }

}
