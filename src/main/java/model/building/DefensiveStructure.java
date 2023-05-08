package model.building;

import model.Kingdom;
import model.MapBlock;

public class DefensiveStructure extends Building{
    private Integer capacity;
    public DefensiveStructure(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType,owner);
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity += capacity;
    }

    //TODO Siege

}
