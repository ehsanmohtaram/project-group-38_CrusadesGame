package model.unit;

import model.Kingdom;
import model.MapBlock;

import java.util.ArrayList;

public class Unit {
    private Integer hp;
    private UnitType unitType;

    private MapBlock locationBlock;
    private UnitState unitState;
    private Kingdom owner;

    public Unit(UnitType unitType, MapBlock locationBlock , Kingdom owner) {
        this.unitType = unitType;
        this.locationBlock = locationBlock;
        this.owner = owner;
        hp = unitType.getHP_IN_START();
        locationBlock.addUnitHere(this);
        unitState = UnitState.NOT_ACTIVE;
    }

    public Integer getHp() {
        return hp;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public Integer getxPosition() {
        return locationBlock.getxPosition();
    }

    public Integer getyPosition() {
        return locationBlock.getyPosition();
    }

    public MapBlock getLocationBlock() {
        return locationBlock;
    }

    public UnitState getUnitState() {
        return unitState;
    }

    public Kingdom getOwner() {
        return owner;
    }

    public void setUnitState(UnitState unitState) {
        this.unitState = unitState;
    }

    public void moveTo(MapBlock destination){
        locationBlock.removeUnitFromHere(this);
        destination.addUnitHere(this);
        //toDo the function is not complete
    }

    public void fight(Unit enemy){
    }

}
