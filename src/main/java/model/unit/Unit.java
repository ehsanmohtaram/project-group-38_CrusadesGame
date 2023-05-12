package model.unit;

import controller.UnitController;
import model.Kingdom;
import model.MapBlock;
import model.building.Building;
import model.building.BuildingType;
import model.building.DefensiveStructure;
import model.building.DefensiveStructureType;

import java.util.ArrayList;

public class Unit {
    private Integer hp;
    private UnitType unitType;
    private MapBlock locationBlock;
    private MapBlock PatrolDestination;
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
        locationBlock.setUnits(this);
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

    public MapBlock getPatrolDestination() {
        return PatrolDestination;
    }

    public void setPatrolDestination(MapBlock patrolDestination) {
        PatrolDestination = patrolDestination;
    }

    public void decreaseHp(int amount){
        hp -= amount;
        if(hp <= 0) {
            locationBlock.getUnits().remove(this);
            owner.getUnits().remove(this);
            if(UnitController.currentUnit != null)
                if(UnitController.currentUnit.contains(this))
                    UnitController.currentUnit.remove(this);
        }
    }

    public Integer getOptimizedAttackRange(){
        if(higherElevation != null && unitType.getCAN_DO_AIR_ATTACK()) {
            DefensiveStructureType type = (DefensiveStructureType) higherElevation.getSpecificConstant();
            return unitType.getATTACK_RANGE() + type.getFurtherFireRange();
        }
        else
            return unitType.getATTACK_RANGE();
    }

    public Integer getOptimizedDistanceFrom(int xPosition, int yPosition, boolean considerHigherElevations){
        int normalDistance = Math.abs(locationBlock.getxPosition() - xPosition) + Math.abs(locationBlock.getyPosition() - yPosition);
        if(higherElevation != null){
            DefensiveStructureType type = (DefensiveStructureType) higherElevation.getSpecificConstant();
            return normalDistance + type.getFurtherFireRange();
        }
        return normalDistance;
    }

    public Integer getOptimizedDamage(){
        return unitType.getDAMAGE() * owner.getAttackRate();
    }

    public boolean decreaseMoves(int amount){
        if(amount > movesLeft)
            return false;
        movesLeft -= amount;
        return true;
    }

    public void moveTo(MapBlock destination, int length){
        decreaseMoves(length);
        locationBlock.removeUnitFromHere(this);
        destination.addUnitHere(this);
        locationBlock.removeUnitFromHere(this);
    }

    private boolean checkEnemyCanAttack(Unit enemy){
        return false;

    }

    public void unilateralFight(Unit enemy){
        enemy.decreaseHp(getOptimizedDamage());
    }

    public boolean bilateralFight(Unit enemy){
        int strikes = enemy.getHp() / getOptimizedDamage() + 1;
        int enemyStrikes = hp / enemy.getOptimizedDamage() + 1;
        if(strikes > enemyStrikes){
            decreaseHp(enemyStrikes * enemy.getOptimizedDamage());
            enemy.decreaseHp(enemyStrikes * getOptimizedDamage());
            return false; //false as dead
        }else{
            decreaseHp(strikes * enemy.getOptimizedDamage());
            enemy.decreaseHp(strikes * getOptimizedDamage());
            return true; //true as alive
        }

    }
    public void destroyBuilding(Building target, ArrayList<Unit> archers){
        if(!target.getBuildingType().equals(BuildingType.HEAD_QUARTER)){
            target.decreaseHP(getOptimizedDamage());
        }else{
            //toDo update the amount of head quarter damage
            target.decreaseHP(getOptimizedDamage());
            decreaseHp(5);
        }
        for (Unit archer : archers) {
            archer.unilateralFight(this);
        }

    }


//
//    public boolean canFightWith(Unit enemy){
//        if(getOptimizedAttackRange() < enemy.ge)
//    }

}
