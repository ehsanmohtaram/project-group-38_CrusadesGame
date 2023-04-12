package model.building;

import model.Land;
import model.Resource;

import java.util.HashMap;

public class Building {
    private HashMap<Resource, Integer> cost;
    private Integer numberOfWorkers;
    private Land position;
    protected BuildingName buildingName;

    public Building(Land position, BuildingName buildingName) {
        this.position = position;
        this.buildingName = buildingName;
    }
}
