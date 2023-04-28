package model.building;

import model.MapBlock;
import model.unit.Unit;

import java.util.ArrayList;

public class Camp extends Building{
    private ArrayList<Unit> units = new ArrayList<>();
    private Integer numberOFLadderMan;
    private Integer numberOfEngineer;

    public Camp(MapBlock position, BuildingType buildingType) {
        super(position, buildingType);
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public Integer getNumberOFLadderMan() {
        return numberOFLadderMan;
    }

    public void setNumberOFLadderMan(Integer numberOFLadderMan) {
        this.numberOFLadderMan = numberOFLadderMan;
    }

    public Integer getNumberOfEngineer() {
        return numberOfEngineer;
    }

    public void setNumberOfEngineer(Integer numberOfEngineer) {
        this.numberOfEngineer = numberOfEngineer;
    }

}
