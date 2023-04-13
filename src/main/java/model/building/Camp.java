package model.building;

import model.Land;
import model.unit.Unit;

import java.util.ArrayList;

public class Camp extends Building{
    private ArrayList<Unit> units = new ArrayList<>();
    private Integer numberOFLadderMan;
    private Integer numberOfEngineer;

    public Camp(Land position, BuildingType buildingType) {
        super(position, buildingType);
        numberOfEngineer = 0;
        numberOFLadderMan = 0;
    }
}
