package model;

import model.building.Building;
import model.building.BuildingType;
import model.building.DefensiveStructure;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;

public class MapBlock {

    private Building buildings;
    private Building siege;
    private ArrayList<Unit> units;
    private ResourceType resource;
    private int resourceAmount;
    private MapBlockType mapBlockType;
    private final Integer xPosition;
    private final Integer yPosition;
    private HashMap<Tree , Integer> numberOfTrees;

    public MapBlock(Integer xPosition, Integer yPosition) {
        this.mapBlockType = MapBlockType.EARTH;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.buildings = null;
        this.siege = null;
        resourceAmount = 0;
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

    public void setUnits(Unit unit) {
        units.add(unit);
    }

    public ResourceType getResources() {
        return resource;
    }

    public int getResourceAmount(){
        return resourceAmount;
    }
    public void setResources(ResourceType resource, int resourceAmount) {
        this.resource = resource;
        this.resourceAmount = resourceAmount;
    }

    public MapBlockType getMapBlockType() {
        return mapBlockType;
    }

    public void setMapBlockType(MapBlockType mapBlockType) {
        this.mapBlockType = mapBlockType;
        if(mapBlockType.equals(MapBlockType.IRON)) {
            resource = ResourceType.IRON;
            resourceAmount = 30;
        }
        if(mapBlockType.equals(MapBlockType.SLATE)){
            resource = ResourceType.ROCK;
            resourceAmount = 100;
        }
    }

    public Integer getxPosition() {
        return xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public Building getBuildings() {
        return buildings;
    }

    public Building getSiege() {
        return siege;
    }

    public void setSiege(Building siege) {
        this.siege = siege;
    }

    public HashMap<Tree, Integer> getNumberOfTrees() {
        return numberOfTrees;
    }

    public void addUnitHere(Unit toAdd){
        units.add(toAdd);
    }

    public void removeUnitFromHere(Unit toRemove){
        units.remove(toRemove);
    }

    public Unit getLastUnitArrived(){
        return units.get(units.size() - 1);
    }

    public void processNextTurn(){
    }

    public boolean addTree(Tree tree){
        if(!mapBlockType.isCultivable())
            return false;
        numberOfTrees.put(tree , numberOfTrees.get(tree) + 1);
        resource = ResourceType.WOOD;
        resourceAmount += (numberOfTrees.get(tree) * 5);
        return true;
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
