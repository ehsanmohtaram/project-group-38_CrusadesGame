package model;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitType;
import view.controller.GameUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MapBlock extends StackPane {

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
    private Rectangle backgroundImage;
    private boolean isSelected;
    private ArrayList<Rectangle> treeImage;

    public MapBlock(Integer xPosition, Integer yPosition) {
        super();
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
        treeImage = new ArrayList<>();


//        setScaleX(100);
//        setScaleY(100);
        isSelected = false;
        changeBackground();
        hoverProcess();

    }


    public void setVisualPosition(){
        setLayoutX(xPosition * 100);
        setLayoutY(yPosition * 100);
        setPrefSize(100, 100);
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
        changeBackground();
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
        if(!mapBlockType.isCultivable()) {
            for (Tree tree : numberOfTrees.keySet()) {
                numberOfTrees.put(tree, 0);
                getChildren().remove(treeImage);
            }
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Rectangle getBackgroundImage() {
        return backgroundImage;
    }

    public boolean addTree(Tree tree){
        if(!mapBlockType.isCultivable())
            return false;
        numberOfTrees.put(tree , numberOfTrees.get(tree) + 1);
        resource = ResourceType.WOOD;
        if(treeImage.size() < 4){
            Rectangle image = new Rectangle(60, 60);
            this.treeImage.add(image);
            image.setFill(Color.rgb(0, 0, 0, 0));
            image.setFill(new ImagePattern(tree.getTexture()));
            image.setManaged(false);
            image.setLayoutX(new Random().nextInt(50) );
            image.setLayoutY(new Random().nextInt(50) );
            getChildren().add(image);
        }
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

    public ArrayList<Unit> getUnitByUnitType(UnitType unitType, Kingdom owner) {
        ArrayList<Unit> selectedUnit = new ArrayList<>();
        for (Unit unit : units)
            if(unit.getUnitType().equals(unitType) && unit.getOwner().equals(owner)) selectedUnit.add(unit);
        return selectedUnit;
    }

    public Boolean isUnitsShouldBeAttackedFirst(Unit attacker){
        if(units.size() == 0)
            return false;
        return units.get(0).getOptimizedDistanceFrom(attacker.getXPosition(), attacker.getYPosition(), true) <= attacker.getOptimizedAttackRange();
    }

    public Integer getOptimizedDistanceFrom(int xPosition, int yPosition, boolean considerHigherElevations){
        int normalDistance = Math.abs(this.xPosition - xPosition) + Math.abs(this.yPosition - yPosition);
        if (units.size() > 0) {
            if (units.get(0).getHigherElevation() != null && considerHigherElevations) {
                DefensiveStructureType type = (DefensiveStructureType) units.get(0).getHigherElevation().getSpecificConstant();
                return normalDistance + type.getFurtherFireRange();
            }
        }
        return normalDistance;
    }

    public void changeBackground(){
//        Image image = new Image(LoginMenu.class.getResource(mapBlockType.getTextureAddress()).toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100 , 100 , false, false, false, false);
//        Rectangle backGround = new Rectangle(100 , 100);
//        backGround.setFill(Color.rgb(0,0, 0 , 0));
//        backGround.setFill(new ImagePattern(mapBlockType.getTexture()));
        setBackground(new Background(new BackgroundImage(mapBlockType.getTexture(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize)));
//        getChildren().add(backGround);
//        this.backgroundImage = backGround;
    }

    public void changeBorder(boolean shouldAdd){

        if(shouldAdd)
            this.setBorder(new Border(new BorderStroke(Color.rgb(0,0,0), BorderStrokeStyle.SOLID, null, BorderStroke.THIN, new Insets(1, 1 , 1 , 1))));
        else
            this.setBorder(null);
    }


    private void hoverProcess() {
        this.setOnMouseEntered(e -> {
            changeBorder(true);
            GameUI.mouseOnBlock = this;
        });
        this.setOnMouseExited(e -> {
            if(!isSelected)
                changeBorder(false);
        });
    }

}
