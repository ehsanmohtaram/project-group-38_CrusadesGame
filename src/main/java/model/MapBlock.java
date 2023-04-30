package model;

import model.building.Building;
import model.building.BuildingType;
import model.building.DefensiveStructure;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;

public class MapBlock {

    private Building buildings;
    private ArrayList<Unit> units;
    private ArrayList<ResourceType> resources;
    private MapBlockType mapBlockType;
    private Integer xPosition;
    private Integer yPosition;
    private HashMap<Tree , Integer> numberOfTrees;

    public MapBlock(Integer xPosition, Integer yPosition) {
        this.mapBlockType = MapBlockType.EARTH;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.buildings = null;
        resources = new ArrayList<>();
        units = new ArrayList<>();
        numberOfTrees = new HashMap<>();
        for (Tree tree: Tree.values()) numberOfTrees.put(tree , 0);
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setBuildings(Building buildings) {
        this.buildings = buildings;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public ArrayList<ResourceType> getResources() {
        return resources;
    }

    public void setResources(ArrayList<ResourceType> resources) {
        this.resources = resources;
    }

    public MapBlockType getMapBlockType() {
        return mapBlockType;
    }

    public void setMapBlockType(MapBlockType mapBlockType) {
        this.mapBlockType = mapBlockType;
    }

    public Integer getxPosition() {
        return xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public void addUnitHere(Unit toAdd){
        units.add(toAdd);
    }

    public void removeUnitFromHere(Unit toRemove){
        units.remove(toRemove);
    }

    public Building getBuildings() {
        return buildings;
    }

    public Unit getLastUnitArrived(){
        return units.get(units.size() - 1);
    }

    public void processNextTurn(){
    }

    public void addTree(Tree tree){
        numberOfTrees.put(tree , numberOfTrees.get(tree) + 1);
    }

    public static MapBlockType findEnumByLandType(String landType) {
        for (MapBlockType searchForType : MapBlockType.values())
            if (searchForType.name().toLowerCase().replaceAll("_"," ").equals(landType))
                return searchForType;
        return null;
    }

    public String getLatestDetails(){
        for (Unit moving: units) {
            if(moving.isMoving())
                return "S";
        }
        if(buildings != null) {
            if (buildings instanceof DefensiveStructure)
                return "W";
            else
                return "B";
        }
        for (Tree tree: numberOfTrees.keySet())
            if(numberOfTrees.get(tree) != 0)
                return "T";
        return " ";
    }

}
