package model;

import model.building.*;
import model.unit.Unit;
import model.unit.UnitType;

import java.util.ArrayList;
import java.util.HashMap;

public class MapBlock {

    private Building buildings;
    private Building siege;
    private ArrayList<Unit> units;
    private ResourceType resource;
    private int resourceAmount;
    private Trap trap;
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
        trap = null;
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
            resourceAmount = 60;
        }
        if(mapBlockType.equals(MapBlockType.SLATE)){
            resource = ResourceType.ROCK;
            resourceAmount = 100;
        }
        if(mapBlockType.equals(MapBlockType.PLAIN)){
            resource = ResourceType.RIG;
            resourceAmount = 60;
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

    public Trap getTrap() {
        return trap;
    }

    public void setTrap(Trap trap) {
        this.trap = trap;
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

    public boolean addTree(Tree tree){
        if(!mapBlockType.isCultivable())
            return false;
        numberOfTrees.put(tree , numberOfTrees.get(tree) + 10);
        resource = ResourceType.WOOD;
        resourceAmount += (numberOfTrees.get(tree) * 20);
        return true;
    }

    public static MapBlockType findEnumByLandType(String landType) {
        for (MapBlockType searchForType : MapBlockType.values())
            if (searchForType.name().toLowerCase().replaceAll("_"," ").equals(landType))
                return searchForType;
        return null;
    }

    public String getLatestDetails(){
        if(units.size() != 0) return "S";
        if(buildings != null) {
            if (buildings.getBuildingType().equals(BuildingType.HEAD_QUARTER)) return "H";
            else if (buildings instanceof DefensiveStructure) return "W";
            else return "B";
        }
        for (Tree tree: numberOfTrees.keySet())
            if(numberOfTrees.get(tree) != 0)
                return "T";
        return " ";
    }

    public ArrayList<Unit> getUnitByUnitType(UnitType unitType) {
        ArrayList<Unit> selectedUnit = new ArrayList<>();
        for (Unit unit : units)
            if(unit.getUnitType().equals(unitType)) selectedUnit.add(unit);
        return selectedUnit;
    }

    public Boolean isUnitsShouldBeAttackedFirst(Unit attacker){
        if(units.size() == 0)
            return false;
        return units.get(0).getOptimizedDistanceFrom(attacker.getXPosition(), attacker.getYPosition(), true) <= attacker.getOptimizedAttackRange();
    }

    public Integer getOptimizedDistanceFrom(int xPosition, int yPosition, boolean considerHigherElevations){
        int normalDistance = Math.abs(this.xPosition - xPosition) + Math.abs(this.yPosition - yPosition);
        if(units.get(0).getHigherElevation() != null && considerHigherElevations){
            DefensiveStructureType type = (DefensiveStructureType) units.get(0).getHigherElevation().getSpecificConstant();
            return normalDistance + type.getFurtherFireRange();
        }
        return normalDistance;
    }

}
