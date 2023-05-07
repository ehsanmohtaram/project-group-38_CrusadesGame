package model.building;

import model.Kingdom;
import model.MapBlock;
import model.unit.Unit;
import model.unit.UnitType;

import java.util.ArrayList;

public class Siege extends Building {
    private ArrayList<Unit> engineers;
    public Siege(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
        engineers = new ArrayList<>();
    }
    public void addStaff(Unit engineer) {
        engineers.add(engineer);
    }

    public ArrayList<Unit> getEngineers() {
        return engineers;
    }

}
