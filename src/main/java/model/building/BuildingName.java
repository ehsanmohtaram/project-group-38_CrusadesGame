package model.building;

import model.Resource;

import java.util.*;

public enum BuildingName {
    BARRAK(PartOfCastle.class , "Barrak" , new HashMap<>(Map.ofEntries(Map.entry(Resource.WOOD , 5))), 10 );
    private Class type;
    private String name;
    private HashMap<Resource, Integer> cost;
    private Integer numberOfWorkers;
    private Integer RATE;
    private Integer CAPACITY;
    private Integer FIRE_RANGE;
    private Integer DEFEND_RANGE;
    BuildingName(Class type , String name ,HashMap<Resource , Integer> cost , Integer numberOfWorkers) {
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.numberOfWorkers = numberOfWorkers;
    }

    //toDo overload constructor
}
