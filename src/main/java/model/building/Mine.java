package model.building;

import model.Kingdom;
import model.MapBlock;

public class Mine extends Building{
    public Mine(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
    }
}
