package model.building;

import model.Land;
import model.Resource;

import java.util.HashMap;

public class Building {
    private Integer gold;
    private Resource resource;
    private Integer resourceNumber;
    protected Integer hp;
    private Land position;
    private BuildingType buildingType;
    public Building(Land position, BuildingType buildingType) {
        this.position = position;
        this.buildingType = buildingType;
    }

}
