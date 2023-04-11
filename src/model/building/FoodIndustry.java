package model.building;

import model.Land;
import model.Resource;

import java.util.HashMap;

public class FoodIndustry extends Building {
//    private final Integer CAPACITY;
//    private final Integer RATE;

    public FoodIndustry(HashMap<Resource, Integer> cost, Integer numberOfWorkers, Land position, BuildingName buildingName) {
        super(position, buildingName);
    }
}
