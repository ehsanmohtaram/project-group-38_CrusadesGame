package model.unit;

import controller.UnitController;
import model.Direction;
import model.Kingdom;
import model.MapBlock;
import model.Trade;
import model.building.*;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;

public class Unit {
    public static ArrayList<Unit> AggressiveUnits = new ArrayList<>();
    private Integer hp;
    private UnitType unitType;
    private MapBlock locationBlock;
    private MapBlock PatrolDestination;
    private UnitState unitState;
    private final Kingdom owner;
    private Unit forAttack;
    private Integer movesLeft;
    private DefensiveStructure higherElevation;
    public Unit(UnitType unitType, MapBlock locationBlock , Kingdom owner) {
        this.unitType = unitType;
        this.locationBlock = locationBlock;
        this.owner = owner;
        hp = unitType.getHP_IN_START();
        if (!unitType.getIS_ARAB().equals(-4)) {
            locationBlock.addUnitHere(this);
            owner.addUnit(this);
            unitState = UnitState.NOT_ACTIVE;
        }
        else unitState = UnitState.OFFENSIVE;

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
        if(unitState.equals(UnitState.OFFENSIVE))
            AggressiveUnits.add(this);
        if(this.unitState.equals(UnitState.OFFENSIVE) && !unitState.equals(UnitState.OFFENSIVE))
            AggressiveUnits.remove(this);
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
            removeUnit();
        }
    }

    public void removeUnit(){
        locationBlock.getUnits().remove(this);
        owner.getUnits().remove(this);
        if(UnitController.currentUnit != null)
            if(UnitController.currentUnit.contains(this))
                UnitController.currentUnit.remove(this);
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
        if(higherElevation != null && considerHigherElevations){
            DefensiveStructureType type = (DefensiveStructureType) higherElevation.getSpecificConstant();
            return normalDistance + type.getFurtherFireRange();
        }
        return normalDistance;
    }

    public Integer getOptimizedDamage(){
        return unitType.getDAMAGE() + owner.getAttackRate();
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
        this.locationBlock = destination;
        Trap trap = destination.getTrap();
        if(trap != null)
            if(trap.isActive())
                decreaseHp(trap.getTrapType().getDamage());
    }
    private boolean checkEnemyCanAttack(Unit enemy){
        return false;

    }

    public void unilateralFight(Unit enemy){
        if(hp <= 0)
            return;
        if(enemy.getOwner().equals(owner))
            return;
        enemy.decreaseHp(getOptimizedDamage());
    }

    public void bilateralFight(Unit enemy, boolean considerDistances){
        if(hp <= 0)
            return;
        if(enemy.getOwner().equals(owner))
            return;
        if(considerDistances){
            if(enemy.getOptimizedDistanceFrom(getXPosition(), getYPosition(), true) <= getOptimizedAttackRange()){
                enemy.decreaseHp(getOptimizedDamage());
            }
            if(getOptimizedDistanceFrom(enemy.getXPosition(), enemy.getYPosition(), true) <= enemy.getOptimizedAttackRange()){
                decreaseHp(enemy.getOptimizedDamage());
            }
        }else{
            enemy.decreaseHp(getOptimizedDamage());
            decreaseHp(enemy.getOptimizedDamage());
        }
    }

    public boolean bilateralFightTillEnd(Unit enemy){
        if(hp <= 0)
            return false;
        if(enemy.getOwner().equals(owner))
            return true;
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
        if(target.getOwner().equals(owner))
            return;
        for (Unit archer : archers) {
            archer.bilateralFight(this, true);
        }
        if(hp <= 0)
            return;
        if(!target.getBuildingType().equals(BuildingType.HEAD_QUARTER)){
            target.decreaseHP(getOptimizedDamage());
        }else{
            //toDo update the amount of head quarter damage
            target.decreaseHP(getOptimizedDamage());
            decreaseHp(100);
        }

    }

    public void resetAttributes(){
        if(hp > 0){
            movesLeft = unitType.getVELOCITY();
            if(locationBlock.getBuildings() instanceof DefensiveStructure)
                setHigherElevation((DefensiveStructure) locationBlock.getBuildings());
        }
        AggressiveUnits = new ArrayList<>();
    }



//
//    public boolean canFightWith(Unit enemy){
//        if(getOptimizedAttackRange() < enemy.ge)
//    }
}