package model.building;

import model.Land;
import model.Resource;

import java.util.HashMap;

public class PartOfCastle extends Building {
//    private final Integer RATE;
//    private final Integer FIRE_RANGE;
//    private final Integer DEFEND_RANGE;
//    private final Integer COST;
    private Integer hp;

    public PartOfCastle(HashMap<Resource, Integer> cost, Integer numberOfWorkers, Land position, BuildingName buildingName, Integer hp) {
        super(position, buildingName);
        this.hp = hp;
    }
}
