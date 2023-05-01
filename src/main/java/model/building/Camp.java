package model.building;

import model.Kingdom;
import model.MapBlock;
import model.unit.Unit;

import java.util.ArrayList;

public class Camp extends Building{
    private ArrayList<Unit> units = new ArrayList<>();
    private Integer capacity;

    public Camp(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
