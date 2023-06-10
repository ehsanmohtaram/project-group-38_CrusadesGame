package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class MapDesignController {
    private final Map gameMap;
//    private final DesignMapMenu designMapMenu;
    private final User currentUser;
    private int XofMap;
    private int YofMap;
    private ArrayList<MapBlock> selectedBlocks;
    private Pane mapDesignPane;

    public MapDesignController(Map gameMap) {
        this.gameMap = gameMap;
//        designMapMenu = new DesignMapMenu(this);
        currentUser = Controller.currentUser;
        selectedBlocks = new ArrayList<>();
    }

    public void run(){
//        while (true){
//            switch (designMapMenu.run()) {
//                case "start":
//                    GameController gameController = new GameController(gameMap);
//                    gameController.run();
//                    return;
//                case "map":
//                    MapController mapController = new MapController(gameMap, currentUser, XofMap, YofMap);
//                    mapController.run();
//
//            }
//        }
    }

    public void addMapToPane(Pane mapDesignPane){
        this.mapDesignPane = mapDesignPane;
        mapDesignPane.getChildren().add(gameMap.getMapPane());
//        mapDesignPane.setAlignment(mapDesignPane.getChildren().get(0) , Pos.CENTER_LEFT);
//        gameMap.getMapPane().getChildren().add(new Label("hello word"));


    }

    public Pane getGameMapPane() {
        return gameMap.getMapPane();
    }

    public void handelMapSelection(){
        Pane mapPane = gameMap.getMapPane();
        mapPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mapPane.requestFocus();

                for (MapBlock[] mapBlocks : gameMap.getMap())
                    for (MapBlock mapBlock : mapBlocks)
                        if (mapBlock.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
                            if (!mouseEvent.isControlDown()) {
                                for (MapBlock selectedBlock : selectedBlocks) {
                                    selectedBlock.setSelected(false);
                                    selectedBlock.changeBorder(false);
                                }
                                selectedBlocks.clear();
                            }
                            selectedBlocks.add(mapBlock);
                            mapBlock.changeBorder(true);
                            mapBlock.setSelected(true);
                        }
                System.out.println(selectedBlocks);
            }
        });
    }

    public String setTexture(HashMap<String , String> options) {
        if(options.get("t") == null)
            return "please choose a type to change texture";
        MapBlockType mapBlockType;
        if((mapBlockType = MapBlock.findEnumByLandType(options.get("t"))) == null)
            return "no such type available for lands";
        String checkingResult;
        if(options.get("x") == null && options.get("y") == null) {
            if(mapBlockType.equals(MapBlockType.BIG_POND) || mapBlockType.equals(MapBlockType.SMALL_POND))
                return "ponds have static size";
            if((checkingResult = checkLocationValidation(options.get("x1") , options.get("y1"))) != null )
                return checkingResult;
            if((checkingResult = checkLocationValidation(options.get("x2") , options.get("y2"))) != null )
                return checkingResult;
            int x1 = Integer.parseInt(options.get("x1"));
            int x2 = Integer.parseInt(options.get("x2"));
            int y1 = Integer.parseInt(options.get("y1"));
            int y2 = Integer.parseInt(options.get("y2"));
            gameMap.changeType(x1, y1, x2, y2, mapBlockType);
        }else if(options.get("x") != null && options.get("y") != null){
            if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
                return checkingResult;
            int x = Integer.parseInt(options.get("x"));
            int y = Integer.parseInt(options.get("y"));
            options.remove("x");
            options.remove("y");
            for (String key : options.keySet())
                if (!key.equals("t") && options.get(key) != null)
                    return "choose two or four digits to specify area!";
            switch (mapBlockType){
                case BIG_POND:
                    if(gameMap.getMapBlockByLocation(x + 4 , y + 4)!= null)
                        gameMap.changeType(x, y , x + 4 , y + 4 , mapBlockType);
                    else
                        return "choose correct position for big pond";
                break;
                case SMALL_POND:
                    if(gameMap.getMapBlockByLocation(x + 2 , y + 2)!= null)
                        gameMap.changeType(x, y , x + 2 , y + 2 , mapBlockType);
                    else
                        return "choose correct position for small pond";
                default:gameMap.changeType(x, y , x , y , mapBlockType);
            }
        }else
            return "you must choose at least two digits for bounds";
        return "type changed successfully";
    }

//    public void moveRight(){
//        gameMap.getMapPane().setLayoutX(gameMap.getMapPane().getLayoutX() + 100);
//    }

    private String checkLocationValidation (String xPosition , String yPosition){
        if(xPosition == null || yPosition == null) return "you must specify a location";
        if(!xPosition.matches("-?\\d+") || !yPosition.matches("-?\\d+")) return "please choose digits for location";
        int x = Integer.parseInt(xPosition);
        int y = Integer.parseInt(yPosition);
        if(gameMap.getMapBlockByLocation(x , y) == null) return "index out of bounds";
        return null;
    }

    public String clear(HashMap<String , String> options) {
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
            return checkingResult;

        int x = Integer.parseInt(options.get("x"));
        int y = Integer.parseInt(options.get("y"));
        gameMap.clearBlock(x,y);
        return "successfully cleared";
    }
    public String dropRock(HashMap<String , String> options) {
        if(options.get("d") == null)
            return "please enter necessary options";
        if(!options.get("d").matches("[nswer]"))
            return "no such direction";
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
            return checkingResult;

        int xPosition = Integer.parseInt(options.get("x"));
        int yPosition = Integer.parseInt(options.get("y"));

        if(options.get("d").charAt(0) == 'r'){
            Random random = new Random();
            options.put("d", String.valueOf("snwr".charAt(random.nextInt(4)) ));
        }
        Direction trueDirection = null;
        for (Direction direction: Direction.values())
            if(direction.name().toLowerCase().charAt(0) == options.get("d").charAt(0))
                trueDirection = direction;

        gameMap.changeAccess(xPosition , yPosition , trueDirection, false );
        gameMap.getMapBlockByLocation(xPosition, yPosition).setMapBlockType(MapBlockType.ROCK);
        return "successfully dropped";
    }
    public String dropTree(HashMap<String , String> options) {
        if(options.get("t") == null)
            return "please choose a tree";
        Tree tree;
        if((tree = Tree.findEnumByName(options.get("t"))) == null)
            return "no such type available for lands";
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
            return checkingResult;

        if(!gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")), Integer.parseInt(options.get("y"))).addTree(tree))
            return "here is not cultivable";
        return "successfully dropped";
    }

    public String checkAroundHeadQuarterPosition(Integer xPosition, Integer yPosition) {
        int changeXToString;
        int changeYToString;
        for (int i = -10; i < 10; i++) {
            for (int j = -10; j < 10; j++) {
                changeXToString = xPosition + i;
                changeYToString = yPosition + j;
                if (checkLocationValidation(Integer.toString(changeXToString), Integer.toString(changeYToString)) != null)
                    return "You have to put your HeadQuarter in a suitable position!";
                if (gameMap.getMapBlockByLocation(changeXToString,changeYToString).getBuildings() != null) return "You can not make your HeadQuarter next to others!";
            }
        }
        return null;
    }

    public String startPlaying(){
        if (gameMap.getKingdomByOwner(currentUser) == null) return "You have not added yourself to map yet. Please add your kingdom!";
        if (gameMap.getPlayers().size() < 2) return "Please at least add 2 players to start game!";
        return "start";
    }

    public ArrayList<Integer> checkForMainStockPosition(int x, int y) {
        ArrayList<Integer> stockPosition = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i != 0 && j != 0) continue;
                if (i == 0 && j == 0) continue;
                if (gameMap.getMapBlockByLocation(x + i, y + j).getMapBlockType().isBuildable()) {
                    stockPosition.add(x + i); stockPosition.add(y + j);
                    return stockPosition;
                }
            }
        }
        return stockPosition;
    }

    public String addUserToMap(HashMap<String , String> options){
        String checkingResult;
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (User.getUserByUsername(options.get("u")) == null) return "User dose not exist. Please choose user from registered users";
        try {Flags.valueOf(options.get("f").toUpperCase());}
        catch (Exception ignored) {return "Your flag color did not exist in default colors!";}
//        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null ) return checkingResult;
        if(selectedBlocks.size() == 0) return "no position has been selected yet to drop headquarter";
        if(selectedBlocks.size() > 1) return "you have selected multiple locations";
        int xPosition = selectedBlocks.get(0).getxPosition() , yPosition = selectedBlocks.get(0).getyPosition();
        if (!selectedBlocks.get(0).getMapBlockType().isBuildable())
            return "You can not build your head quarter in this type of land!";
        if ((checkingResult = checkAroundHeadQuarterPosition(xPosition,yPosition)) != null) return checkingResult;
        if (gameMap.getKingdomByOwner(User.getUserByUsername(options.get("u"))) != null)
            return "You already have put your kingdom in this map!";
        for (Kingdom kingdom : gameMap.getPlayers())
            if (kingdom.getFlag().name().equals(options.get("f").toUpperCase())) return "Another user choose this flag.";
        if (checkForMainStockPosition(xPosition, yPosition).size() == 0) return "There is no space around Head Quarter to put stock pile!";
        Kingdom kingdom = new Kingdom(Flags.valueOf(options.get("f").toUpperCase()), User.getUserByUsername(options.get("u")));
        DefensiveStructure headQuarter = new DefensiveStructure(selectedBlocks.get(0), BuildingType.HEAD_QUARTER, kingdom);
        int newXPosition = checkForMainStockPosition(xPosition, yPosition).get(0);
        int newYPosition = checkForMainStockPosition(xPosition, yPosition).get(1);
        Stock stock = new Stock(gameMap.getMapBlockByLocation(newXPosition, newYPosition), BuildingType.STOCKPILE, kingdom);
        gameMap.addPlayer(kingdom);
        kingdom.setHeadquarter(headQuarter); kingdom.addBuilding(headQuarter);
        selectedBlocks.get(0).setBuildings(headQuarter);
        kingdom.addBuilding(stock);
        selectedBlocks.get(0).setBuildings(stock);
        User.getUserByUsername(options.get("u")).addToMyMap(gameMap);
        return "successful";
    }

    public String dropUnit(HashMap<String , String> options){
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null ) return checkingResult;
        if(options.get("t") == null || options.get("f") == null) return "you must specify details like type and flag";
        int count;
        if(options.get("c") == null) count = 1;
        else if (!options.get("c").matches("\\d+")) return "please fill count option correctly";
        else count = Integer.parseInt(options.get("c"));
        if(options.get("t").equals("") || options.get("f").equals("")) return "Illegal value. Please fill the options";
        UnitType unitType;
        try {unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is no such a unit!";}
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (!mapBlock.getMapBlockType().isAccessible())
            return "You can not drop units on this types of lands. Please choose another location!";
        Flags kingdomFlag;
        Kingdom unitOwner = null;
        try {kingdomFlag = Flags.valueOf(options.get("f").toUpperCase());}
        catch (IllegalArgumentException illegalArgumentException)
        {return "Your flag color did not exist in default colors!";}
        for (Kingdom existing: gameMap.getPlayers()) if(existing.getFlag() == kingdomFlag) unitOwner = existing;
        if(unitOwner == null) return "no such kingdom has been added to map";
        if(!unitOwner.checkOutOfRange(mapBlock.getxPosition(), mapBlock.getyPosition())) return "drop units near their kingdom";
        for (int i = 0; i < count; i++) {
            Unit shouldBeAdd = new Unit(unitType, mapBlock, unitOwner);
            shouldBeAdd.setUnitState(UnitState.STANDING);
        }
        return "unit added successfully";
    }
    public void createBuilding(MapBlock mapBlock, BuildingType buildingType, Kingdom currentKingdom) {
        Building building;
        if (buildingType.specificConstant == null) building = new Building(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof DefensiveStructureType) building = new DefensiveStructure(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof CampType) building = new Camp(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof StockType) building = new Stock(mapBlock, buildingType, currentKingdom);
        else if(buildingType.specificConstant instanceof ProducerType) building = new Producer(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof MineType) building = new Mine(mapBlock, buildingType, currentKingdom);
        else building = new Building(mapBlock, buildingType, currentKingdom);
        mapBlock.setBuildings(building);
        currentKingdom.addBuilding(building);
        Unit unit;
        if (!buildingType.getNumberOfWorker().equals(0)) {
            for (int i = 0; i < buildingType.getNumberOfWorker(); i++) {
                unit = new Unit(buildingType.getWorkerNeeded(), mapBlock, currentKingdom);
                unit.setUnitState(UnitState.WORKING);
                currentKingdom.addUnit(unit);
                mapBlock.addUnitHere(unit);
            }
        }
    }

    public String dropBuilding(HashMap<String , String> options){
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null ) return checkingResult;
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        try {BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is no such a building!";}
        if (BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_")).specificConstant instanceof SiegeType)
            return "There is no such a building!";
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (!mapBlock.getMapBlockType().isBuildable()) return "You can not build your building here. Please choose another location!";
        if (mapBlock.getBuildings() != null || mapBlock.getSiege() != null) return "This block already has filled with another building.";
        Flags kingdomFlag;
        Kingdom buildingOwner = null;
        try {kingdomFlag = Flags.valueOf(options.get("f").toUpperCase());}
        catch (IllegalArgumentException illegalArgumentException)
        {return "Your flag color did not exist in default colors!";}
        for (Kingdom existing: gameMap.getPlayers()) if(existing.getFlag() == kingdomFlag) buildingOwner = existing;
        if(buildingOwner == null) return "no such kingdom has been added to map";
        if(buildingOwner.checkOutOfRange(mapBlock.getxPosition(), mapBlock.getyPosition())) return "drop buildings near their kingdom";
        BuildingType buildingType = BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
        if (buildingType.equals(BuildingType.HEAD_QUARTER)) return "You have already dropped headquarter!";
        createBuilding(mapBlock, buildingType, buildingOwner);
        return buildingType.name().toLowerCase().replaceAll("_"," ") + " added to " + kingdomFlag.name() + " kingdom.";
    }

    public String showMap(HashMap<String, String> options){
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
            return checkingResult;
        int xPosition = Integer.parseInt(options.get("x")) ;
        int yPosition = Integer.parseInt(options.get("y")) ;
        XofMap = xPosition;
        YofMap = yPosition;
        return gameMap.getPartOfMap(xPosition, yPosition);
    }

}

