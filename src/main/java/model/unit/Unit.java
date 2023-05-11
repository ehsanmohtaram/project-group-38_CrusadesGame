package model.unit;

import model.Kingdom;
import model.MapBlock;
import model.building.Building;
import model.building.DefensiveStructure;
import model.building.DefensiveStructureType;

public class Unit {
    private Integer hp;
    private UnitType unitType;
    private MapBlock locationBlock;
    private UnitState unitState;
    private Kingdom owner;
    private Unit forAttack;
    private Integer movesLeft;
    private DefensiveStructure higherElevation;
    public Unit(UnitType unitType, MapBlock locationBlock , Kingdom owner) {
        this.unitType = unitType;
        this.locationBlock = locationBlock;
        this.owner = owner;
        hp = unitType.getHP_IN_START();
        locationBlock.addUnitHere(this);
        owner.addUnit(this);
        unitState = UnitState.NOT_ACTIVE;
        movesLeft = unitType.getVELOCITY();
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

    public Integer getMovesLeft() {
        return movesLeft;
    }

    public DefensiveStructure getHigherElevation() {
        return higherElevation;
    }

    public void setHigherElevation(DefensiveStructure higherElevation) {
        this.higherElevation = higherElevation;
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

    public Integer getOptimizedAttackRange(){
        if(higherElevation != null && unitType.getCAN_DO_AIR_ATTACK()) {
            DefensiveStructureType type = (DefensiveStructureType) higherElevation.getSpecificConstant();
            return unitType.getATTACK_RANGE() + type.getFurtherFireRange();
        }
        else
            return unitType.getATTACK_RANGE();
    }

    public Integer getOptimizedDistanceFrom(int xPosition, int yPosition){
        int normalDistance = Math.abs(locationBlock.getxPosition() - xPosition) + Math.abs(locationBlock.getyPosition() - yPosition);
        if(higherElevation != null){
            DefensiveStructureType type = (DefensiveStructureType) higherElevation.getSpecificConstant();
            return normalDistance + type.getFurtherFireRange();
        }
        return normalDistance;
    }

    public boolean increaseMoves(int amount){
        if(amount > movesLeft)
            return false;
        movesLeft -= amount;
        return true;
    }

    public void moveTo(MapBlock destination, int length){
        increaseMoves(length);
        locationBlock.removeUnitFromHere(this);
        this.setLocationBlock(destination);
    }

    public void fight(Unit enemy){

    }
    public void destroyBuilding(Building target){

    }
//
//    public boolean canFightWith(Unit enemy){
//        if(getOptimizedAttackRange() < enemy.ge)
//    }

}
