package model.building;

import model.Kingdom;
import model.MapBlock;

public class Siege extends Building {
    public Siege(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
    }
}
