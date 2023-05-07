package model.unit;

import model.Kingdom;
import model.MapBlock;

public class Unit {
    private Integer hp;
    private UnitType unitType;
    private MapBlock locationBlock;
    private UnitState unitState;
    private Kingdom owner;
    private Unit forAttack;
    private boolean isMoving;
    public Unit(UnitType unitType, MapBlock locationBlock , Kingdom owner) {
        this.unitType = unitType;
        this.locationBlock = locationBlock;
        this.owner = owner;
        hp = unitType.getHP_IN_START();
        locationBlock.addUnitHere(this);
        unitState = UnitState.NOT_ACTIVE;
        isMoving = false;
    }

    public Integer getHp() {
        return hp;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public Integer getXPosition() {
        return locationBlock.getxPosition();
    }

    public Integer getYPosition() {
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

    public Unit getForAttack() {
        return forAttack;
    }

    public void setLocationBlock(MapBlock locationBlock) {
        this.locationBlock = locationBlock;
    }

    public void setForAttack(Unit forAttack) {
        this.forAttack = forAttack;
    }

    public void setUnitState(UnitState unitState) {
        this.unitState = unitState;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void moveTo(MapBlock destination){
        locationBlock.removeUnitFromHere(this);
        destination.addUnitHere(this);
        //toDo the function is not complete
    }

    public void fight(Unit enemy){
    }

}
