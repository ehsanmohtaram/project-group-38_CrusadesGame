package model.building;

import model.Land;
import model.Resource;

import java.util.HashMap;

public class Farms extends Building {
    //private final Integer RATE;


    public Farms(HashMap<Resource, Integer> cost, Integer numberOfWorkers, Land position, BuildingName buildingName) {
        super(position, buildingName);
    }
}
