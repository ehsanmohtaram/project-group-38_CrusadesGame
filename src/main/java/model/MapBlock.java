package model;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitType;
import view.LoginMenu;

import java.util.*;

public class MapBlock  {

    private Building buildings;
    private Building siege;
    private ArrayList<Unit> units;
    private ResourceType resource;
    private int resourceAmount;
    private transient Trap trap; //toDo trap pokide
    private MapBlockType mapBlockType;
    private final Integer xPosition;
    private final Integer yPosition;
    private HashMap<Tree , Integer> numberOfTrees;
    private transient Rectangle backgroundImage;
    private boolean isSelected;
    private transient ArrayList<Rectangle> treeImage;
    private transient StackPane graphics;

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
        graphics = new StackPane();

//        setScaleX(100);
//        setScaleY(100);
        isSelected = false;
        changeBackground();
//        hoverProcess();

    }

    public StackPane getGraphics() {
        return graphics;
    }

    public void setGraphics(StackPane graphics) {
        this.graphics = graphics;
    }

    public void setVisualPosition(){
        graphics.setLayoutX(xPosition * 100);
        graphics.setLayoutY(yPosition * 100);
        graphics.setPrefSize(100, 100);
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setBuildings(Building buildings) {
        this.buildings = buildings;
        for (Tree tree : numberOfTrees.keySet()) {
            numberOfTrees.put(tree, 0);
            graphics.getChildren().removeAll(treeImage);
        }
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
                graphics.getChildren().removeAll(treeImage);
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

    public ArrayList<Unit> getSelectedUnits(){
        ArrayList<Unit> selectedUnits = new ArrayList<>();
        for (Unit unit : units)
            if(unit.isSelected())
                selectedUnits.add(unit);

        return selectedUnits;
    }

    public HashMap<Tree, Integer> getNumberOfTrees() {
        return numberOfTrees;
    }

    public void addUnitHere(Unit toAdd){
        units.add(toAdd);
        Pane unitImage = toAdd.getUnitPane();
        graphics.getChildren().add(unitImage);
        unitImage.setManaged(false);
        unitImage.setLayoutX(new Random().nextInt(60) - 10);
        unitImage.setLayoutY(new Random().nextInt(60) - 10);

    }

    public void removeUnitFromHere(Unit toRemove){
        units.remove(toRemove);
        graphics.getChildren().remove(toRemove.getUnitPane());
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
            graphics.getChildren().add(image);
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

    public String showDetails(){
        StringBuilder result = new StringBuilder("type: "
                + getMapBlockType().name().toLowerCase().replaceAll("_", " ") + '\n');
        result.append("Units:\n");
        HashMap<UnitType, Integer[]> numberOFEachUnit = unitDetailsByType();
        for (UnitType unitType : numberOFEachUnit.keySet()) {
            Integer[] unitDetails = numberOFEachUnit.get(unitType);
            result.append(unitType.name().toLowerCase().replaceAll("_", " ")).append(" ->owner: ")
                    .append(Flags.values()[unitDetails[2]].name()).append("\nhp: ").append(unitDetails[1])
                    .append(" damage: ").append(unitType.getDAMAGE()).append(" *").append(unitDetails[0]).append('\n') ;
        }
        result.append("building:\n");
        if(getBuildings() != null){
            result.append(getBuildings().getBuildingType().name().toLowerCase().replaceAll("_", " "))
                    .append(" ->owner: ")
                    .append(getBuildings().getOwner().getFlag().name()).append('\n') ;
        }
        result.append("siege:\n");
        if(getSiege() != null){
            result.append(getSiege().getBuildingType().name().toLowerCase().replaceAll("_", " "))
                    .append(" ->owner: ")
                    .append(getSiege().getOwner().getFlag().name()).append('\n') ;
        }
        if(getResources() != null){
            result.append("resource:\n").append(getResourceAmount()).append(" units of ").append(getResources().name().toLowerCase());
        }
        return result.toString();
    }

    public HashMap<UnitType, Integer[]> unitDetailsByType(){
        HashMap<UnitType, Integer[]> numberOFEachUnit = new HashMap<>();
        for (Unit unit: getUnits()) {
            if(!numberOFEachUnit.containsKey(unit.getUnitType())){
                numberOFEachUnit.put(unit.getUnitType(), new Integer[]{1 , unit.getHp(), List.of(Flags.values()).indexOf(unit.getOwner().getFlag())});
            }else {
                numberOFEachUnit.get(unit.getUnitType())[0] ++;
                numberOFEachUnit.get(unit.getUnitType())[1] += unit.getHp();
            }
        }
        return numberOFEachUnit;
    }

    public ArrayList<Unit> getUnitByUnitType(UnitType unitType, Kingdom owner) {
        ArrayList<Unit> selectedUnit = new ArrayList<>();
        if(unitType != null)
            for (Unit unit : units)
                if (unit.getUnitType().equals(unitType) && unit.getOwner().equals(owner)) selectedUnit.add(unit);

        return selectedUnit;
    }

    public ArrayList<Unit> getUnitByOwner(Kingdom owner) {
        ArrayList<Unit> selectedUnit = new ArrayList<>();
        for (Unit unit : units)
            if(unit.getOwner().equals(owner)) selectedUnit.add(unit);
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
        graphics.setBackground(new Background(new BackgroundImage(mapBlockType.getTexture(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize)));
//        graphics.getChildren().add(backGround);
//        this.backgroundImage = backGround;
    }

    public void changeBorder(boolean shouldAdd){

        if(shouldAdd)
            graphics.setBorder(new Border(new BorderStroke(Color.rgb(0,0,0), BorderStrokeStyle.SOLID, null, BorderStroke.THIN, new Insets(1, 1 , 1 , 1))));
        else
            graphics.setBorder(null);
    }


//    private void hoverProcess() {
//        this.setOnMouseEntered(e -> {
//            changeBorder(true);
//            MapDesignMenuController.mouseOnBlock = this;
//        });
//        this.setOnMouseExited(e -> {
//            if(!isSelected)
//                changeBorder(false);
//        });
//    }

    public void dropRock(Direction direction) {
        setMapBlockType(MapBlockType.ROCK);
        Image rockImage = new Image(LoginMenu.class.getResource("/images/landTextures/rock.png").toExternalForm());
        Rectangle rock = new Rectangle(40 , 40);
        rock.setFill(new ImagePattern(rockImage));
        rock.setManaged(false);
        rock.setLayoutX(direction.getX() * 32);
        rock.setLayoutY(direction.getY() * 31);
        graphics.getChildren().add(rock);
    }
}
