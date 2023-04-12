package model;

import model.building.Building;
import model.unit.Unit;

import java.util.ArrayList;

public class Land {

    private ArrayList<Building> buildings = new ArrayList<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<Resource> resources = new ArrayList<>();
    private LandType landType;
    public Land() {
        landType = LandType.EARTH;
    }
}
