package model.building;

import model.Land;
import model.Resource;

import java.util.HashMap;

public class WeaponBuildings extends Building {
//    private final Integer CAPACITY;
//    private final Integer RATE;
//    private final ArrayList<Resource> NEEDED_RESOURCES = new ArrayList<>();

    public WeaponBuildings(HashMap<Resource, Integer> cost, Integer numberOfWorkers, Land position, BuildingName buildingName) {
        super(position, buildingName);
    }
}
