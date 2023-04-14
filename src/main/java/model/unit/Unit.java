package model.unit;

import model.MapBlock;

import java.util.ArrayList;

public class Unit {
    private Integer hp;
    private UnitType unitType;
    private Integer xPosition;
    private Integer yPosition;
    private MapBlock locationBlock;

    public Unit(UnitType unitType, Integer xPosition, Integer yPosition ,  MapBlock locationBlock) {
        this.unitType = unitType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.locationBlock = locationBlock;
        hp = unitType.getHP_IN_START();
        locationBlock.addUnitHere(this);
    }

    public Integer getHp() {
        return hp;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public Integer getxPosition() {
        return xPosition;
    }

    public Integer getyPosition() {
        return yPosition;
    }

    public MapBlock getLocationBlock() {
        return locationBlock;
    }

    public void moveTo(MapBlock destination){
        locationBlock.removeUnitFromHere(this);
        destination.addUnitHere(this);
        //toDo the function is not complete
    }

    public void fight(Unit enemy){

    }

}
