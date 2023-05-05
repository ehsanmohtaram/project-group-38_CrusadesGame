package model.building;

import model.Kingdom;
import model.MapBlock;

public class Producer extends Building {
    public Producer(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
    }
}
