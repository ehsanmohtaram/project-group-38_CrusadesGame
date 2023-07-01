package model.unit;

import controller.UnitController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.Direction;
import model.Kingdom;
import model.MapBlock;
import model.building.*;
import view.animation.MovingTroopAnimation;

import java.util.ArrayList;

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
    private Pane unitPane;
    private Rectangle unitImage;
    private MovingTroopAnimation movingTroopAnimation;
    private Rectangle selectedState;
    private boolean isSelected;
    public Unit(UnitType unitType, MapBlock locationBlock , Kingdom owner) {
        this.unitType = unitType;
        this.locationBlock = locationBlock;
        this.owner = owner;
        hp = unitType.getHP_IN_START();
        selectedState = new Rectangle(20, 20);
        selectedState.setFill(new ImagePattern(Direction.SOUTH.getImage()));
        unitImage = new Rectangle(70, 70);
        unitPane = new Pane(unitImage);
        movingTroopAnimation = new MovingTroopAnimation(unitType, unitImage);
        isSelected = false;
        if (!unitType.getIS_ARAB().equals(-4)) {
            locationBlock.addUnitHere(this);
            owner.addUnit(this);
            unitState = UnitState.NOT_ACTIVE;
        }
        else
            unitState = UnitState.OFFENSIVE;
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

    public Pane getUnitPane() {
        return unitPane;
    }

    public MovingTroopAnimation getMovingTroopAnimation() {
        return movingTroopAnimation;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setUnitState(UnitState unitState) {
        if(unitState.equals(UnitState.OFFENSIVE))
            if(!AggressiveUnits.contains(this))
                AggressiveUnits.add(this);
        if(this.unitState.equals(UnitState.OFFENSIVE) && !unitState.equals(UnitState.OFFENSIVE))
            AggressiveUnits.remove(this);
        if (this.unitState.equals(UnitState.PATROLLING) && !unitState.equals(UnitState.PATROLLING))
            PatrolDestination = null;
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

    public void moveTo(MapBlock destination, int length, ArrayList<MapBlock> way, Pane mapPane){
        decreaseMoves(length);
        Timeline XAnimation = new Timeline();
        Timeline YAnimation = new Timeline();
        if(way == null) {
            locationBlock.removeUnitFromHere(this);
            destination.addUnitHere(this);
        }else{
            locationBlock.removeUnitFromHere(this);
            mapPane.getChildren().add(unitPane);
            unitPane.setLayoutX(locationBlock.getLayoutX());
            unitPane.setLayoutY(locationBlock.getLayoutY());
            if(way.get(way.size() - 1).getLayoutX() > locationBlock.getLayoutX())
                movingTroopAnimation.setRight(true);
            else
                movingTroopAnimation.setRight(false);
            movingTroopAnimation.play();
            int counter = 0;
            for (MapBlock mapBlock : way) {
                XAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(counter), new KeyValue(unitPane.translateXProperty(), mapBlock.getLayoutX() - locationBlock.getLayoutX())));
                YAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(counter), new KeyValue(unitPane.translateYProperty(), mapBlock.getLayoutY() - locationBlock.getLayoutY())));
                counter ++;
            }
            XAnimation.play();
            YAnimation.play();
            XAnimation.setOnFinished(e-> {
                movingTroopAnimation.pause();
                mapPane.getChildren().remove(unitPane);
                unitPane = new Pane(unitImage, selectedState);
                destination.addUnitHere(this);

            });
        }
            

        this.locationBlock = destination;
        Trap trap = destination.getTrap();
        if(trap != null)
            if(trap.isActive())
                decreaseHp(trap.getTrapType().getDamage());
    }
    private boolean checkEnemyCanAttack(Unit enemy){
        return false;

    }

    public void changeSelection(boolean shouldSelect){
        selectedState.setX(25);
        selectedState.setY(0);
        if(shouldSelect) {
            unitPane.getChildren().add(selectedState);
            isSelected = true;
        }
        else {
            if (unitPane.getChildren().contains(selectedState))
                unitPane.getChildren().remove(selectedState);
            isSelected = false;
        }
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
            target.decreaseHP(getOptimizedDamage());
            decreaseHp(100);
        }

    }

    public void resetAttributes(){
        if(hp > 0){
            movesLeft = unitType.getVELOCITY();
            if(locationBlock.getBuildings() instanceof DefensiveStructure && locationBlock.getBuildings().getOwner().equals(owner))
                setHigherElevation((DefensiveStructure) locationBlock.getBuildings());
        }
    }



//
//    public boolean canFightWith(Unit enemy){
//        if(getOptimizedAttackRange() < enemy.ge)
//    }
}