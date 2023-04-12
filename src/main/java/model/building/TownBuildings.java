package model.building;

import model.Land;
import model.Resource;

import java.util.HashMap;

public class TownBuildings extends Building {
    public TownBuildings(HashMap<Resource, Integer> cost, Integer numberOfWorkers, Land position, BuildingName buildingName) {
        super(position, buildingName);
    }
}
