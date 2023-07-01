package controller;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitType;
import view.*;
import view.controller.MapDesignMenuController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GameController {
    public static Map gameMap;
    public static Building selectedBuilding;
    public static ArrayList<Unit> selectedUnit;
    private final GameMenu gameMenu;
    private User currentUser;
    private Kingdom currentKingdom;
    private int XofMap;
    private int YofMap;
    private Pane mapDesignPane;
    private boolean isDragActive;
    private Style style;
    private UnitController unitController;
    public static Label fightBoard = new Label("");

    public GameController(Map gameMap, Pane mapDesignPane) {
        GameController.gameMap = gameMap;
        this.gameMenu = new GameMenu(this);
        this.mapDesignPane = mapDesignPane;
        currentUser = Controller.currentUser;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
        selectedUnit = new ArrayList<>();
        isDragActive = false;
        style = new Style();
        fightBoard.setFont(style.Font0(30));
        fightBoard.setTextFill(Color.rgb(200, 210 , 0));
        fightBoard.setLayoutX(650);
        fightBoard.setLayoutY(20);
        mapDesignPane.getChildren().add(fightBoard);
    }

    public void run() {
        System.out.println("Welcome to game menu:))");
        while (true) {
            switch (gameMenu.run()) {
                case "map":
                    MapController mapController = new MapController(gameMap, currentUser, XofMap, YofMap);
                    mapController.run();
                    break;
                case "design map":
                    MapDesignController mapDesignController = new MapDesignController(gameMap);
                    mapDesignController.run();
                    break;
                case "trade":

                    break;
                case "building":
                    //BuildingController buildingController = new BuildingController();
                    break;
                case "unit":
                    UnitController unitController = new UnitController();
                    unitController.run();
                    break;
                case "next turn":

                    break;
                case "back": return;
            }
        }
    }

    public void nextTurn(){
        int nextPerson = gameMap.getPlayers().indexOf(currentKingdom);
        Controller.currentUser = gameMap.getPlayers().get((nextPerson + 1) % gameMap.getPlayers().size()).getOwner();
        currentUser = Controller.currentUser;
        selectedUnit = new ArrayList<>(); selectedBuilding = null;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
        Turn turn = new Turn();
        turn.runNextTurn();
        checkForHeadQuarters();
        if (gameMap.getPlayers().size() == 1) {
            gameMap.setEndGame(true); System.out.println(getListOfPlayers());
            Controller.currentUser = Controller.loggedInUser;
            System.out.println("END");
        }
    }

    public void dragAndDropSelection() {
        Pane mapPane = gameMap.getMapPane();
        Rectangle movingRectangle = new Rectangle(100, 100);
        double[] firstPos = new double[2];
        mapPane.setOnDragDetected(e -> {
            isDragActive = true;
            movingRectangle.setFill(Color.rgb(100 , 100 , 100 , 0.5));
            firstPos[0] = e.getX();
            firstPos[1] = e.getY();
            movingRectangle.setX(firstPos[0]);
            movingRectangle.setY(firstPos[1]);
            movingRectangle.setWidth(0);
            movingRectangle.setHeight(0);
            mapPane.getChildren().add(movingRectangle);
        });
        mapPane.setOnMouseDragged(e -> {
            movingRectangle.setX(Math.min(e.getX(), firstPos[0]));
            movingRectangle.setWidth(Math.abs(e.getX() - firstPos[0]));
            movingRectangle.setY(Math.min(e.getY(), firstPos[1]));
            movingRectangle.setHeight(Math.abs(e.getY() - firstPos[1]));
        });
        mapPane.setOnMouseReleased(mouseEvent -> {
            if (isDragActive) {
                mapPane.getChildren().remove(mapPane.getChildren().size() - 1);
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(10));
                pauseTransition.setOnFinished(e -> {
                    isDragActive = false;
                    selectUnits(firstPos[0], firstPos[1], mouseEvent.getX() , mouseEvent.getY());
                });
                pauseTransition.play();
            }
        });
    }

    private void selectUnits(double x, double y, double x2, double y2) {
        gameMap.getMapPane().setDisable(true);
        selectedUnit = new ArrayList<>();
        HashMap<UnitType, ArrayList<Unit>> numberOfEachUnit = new HashMap<>();
        int finalY = (int)(Math.max(y, y2)/100);int finalX = (int)(Math.max(x, x2)/100);
        int startY = (int)(Math.min(y, y2)/100);int startX = (int)(Math.min(x, x2)/100);
        for (int i = startX; i <= finalX ; i++)
            for (int j = startY; j <= finalY; j++)
                selectedUnit.addAll(gameMap.getMapBlockByLocation(i, j).getUnitByOwner(currentKingdom));

        if(selectedUnit.size() == 0 ) {
            gameMap.getMapPane().setDisable(false);
            return;
        }
        for (Unit unit : selectedUnit) {
            if(!numberOfEachUnit.containsKey(unit.getUnitType()))
                numberOfEachUnit.put(unit.getUnitType(), new ArrayList<>(List.of(unit)));
            else
                numberOfEachUnit.get(unit.getUnitType()).add(unit);
        }
        String details = "";
        UnitType[] selectedType = new UnitType[1];
        ArrayList<MenuItem> unitTypes = new ArrayList<>();
        Integer[] count = {0};
        Label number = new Label(count[0].toString());
        style.label0(number, 50 , 40);
        number.setFont(style.Font0(15));
        MenuButton menuButton = new MenuButton("unit");
        for (UnitType unitType : numberOfEachUnit.keySet()) {
            String name = unitType.name().replaceAll("_", " ").toLowerCase();
            details += numberOfEachUnit.get(unitType).size() + "x " + name +"\n";
            MenuItem item = new MenuItem(name);
            item.setOnAction(e-> {
                selectedType[0] = unitType;
                menuButton.setText(name);
                count[0] = 0;
                number.setText(count[0].toString());

            });
            unitTypes.add(item);
        }
        menuButton.getItems().addAll(unitTypes);
        Label units = new Label(details);
        units.setFont(style.Font0(15));
        style.label0(units, 200 , 200);
        number.setOnScroll(e -> {
            if(selectedType[0] == null)
                return;
            int i = 1;
            if(e.getDeltaY() < 0)
                i = -1;
            if(count[0] + i > 0 && numberOfEachUnit.get(selectedType[0]).size() >= count[0] + i)
                count[0] += i;
            number.setText(count[0].toString());
        });
        Button ok = new Button();
        VBox holder = new VBox(units, menuButton, number);
        holder.setPrefSize(220 , 250);
        holder.setAlignment(Pos.CENTER);
        holder.setSpacing(15);
        VBox popUp = new VBox(holder);
        Text error = style.popUp0(mapDesignPane, popUp, ok, 10, 20, 250, 350, 180, 50, 50, 15,900, 100, null, 15);
        popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
        popUp.setAlignment(Pos.CENTER);
        ok.setOnMouseClicked(e ->{
            if(selectedType[0] != null){
                mapDesignPane.getChildren().remove(popUp);
                gameMap.getMapPane().setDisable(false);
                selectedUnit = new ArrayList<>();
                selectedUnit.addAll(numberOfEachUnit.get(selectedType[0]).subList(0, count[0]));
                if(unitController != null)
                    unitController.freeSelection();
                    unitController = new UnitController();

            }else
              error.setText("select a unitType");
        });
    }

    public String getListOfPlayers () {
        StringBuilder stringBuilder = new StringBuilder("GAME FINISHED ((()))\n");
        int counter = 0;
        stringBuilder.append((counter + 1)).append(". ").append(gameMap.getPlayers().get(0).getOwner().getUserName())
                .append(" flag: ").append(gameMap.getPlayers().get(0).getFlag().toString().toLowerCase()).
                append(" score: ").append(gameMap.getDeadPlayers().size() * 500);
        for (Kingdom kingdom : gameMap.getDeadPlayers()) {
            counter++;
            stringBuilder.append("\n").append((counter + 1)).append(". ").append(kingdom.getOwner().getUserName())
                    .append(" flag: ").append(kingdom.getFlag().toString().toLowerCase()).
                    append(" score: ").append((gameMap.getDeadPlayers().size() - counter) * 500);
        }
        return stringBuilder.toString();
    }

    public void checkForHeadQuarters() {
        for (Kingdom kingdom : gameMap.getPlayers()) {
            if (kingdom.getHeadquarter() == null) {
                kingdom.getOwner().setScore(500 * gameMap.getDeadPlayers().size());
                gameMap.setDeadPlayers(kingdom);
                clearMapBlockFromKingdom(kingdom);
            }
        }
        gameMap.getPlayers().removeIf(kingdom -> kingdom.getHeadquarter() == null);
        if (gameMap.getPlayers().size() == 1)
            gameMap.getPlayers().get(0).getOwner().setScore(500 * (gameMap.getDeadPlayers().size()));
    }

    public void clearMapBlockFromKingdom(Kingdom kingdom) {
        for (MapBlock[] mapBlocks : gameMap.getMap()) {
            for (MapBlock mapBlock : mapBlocks) {
                mapBlock.getUnits().removeIf( unit -> unit.getOwner().equals(kingdom));
                mapBlock.setSiege(null);
                mapBlock.setBuildings(null);
            }
        }
    }

    public boolean backToMainMenu() {
        return currentUser.equals(Controller.loggedInUser);
    }

    public String positionValidate(String xPosition, String yPosition) {
        if(!xPosition.matches("-?\\d+") && !yPosition.matches("-?\\d+"))
            return "Please input digit as your input values!";
        if (Integer.parseInt(xPosition) > gameMap.getMapWidth() ||
                Integer.parseInt(yPosition) > gameMap.getMapWidth() ||
                Integer.parseInt(xPosition) < 0 ||
                Integer.parseInt(yPosition) < 0) return "Invalid bounds!";
        return null;
    }

    public boolean checkForNearStock(MapBlock currentBlock, BuildingType stock) {
        int x = currentBlock.getxPosition();
        int y = currentBlock.getyPosition();
        MapBlock mapBlock;
        for (int i = -1; i <= 1; i++) {
            if ( i + x  > gameMap.getMapWidth() || i + x < 0) continue;
            for (int j = -1; j <= 1; j++) {
                if ( j + y  > gameMap.getMapHeight() || j + y < 0) continue;
                mapBlock = gameMap.getMapBlockByLocation(x + i, y + j);
                if (mapBlock.getBuildings() != null && mapBlock.getBuildings().getBuildingType().equals(stock)) return true;
            }
        }
        return false;
    }

    public String checkSpecificBuilding(MapBlock mapBlock, BuildingType buildingType) {
        ProducerType producerType;
        if (buildingType.specificConstant instanceof ProducerType) {
            producerType = (ProducerType) buildingType.specificConstant;
            if (producerType.getFarm() && !mapBlock.getMapBlockType().isCultivable()) return "This block is not appropriate for farms!";
        }
        MineType mineType;
        if (buildingType.specificConstant instanceof MineType) {
            mineType = (MineType) buildingType.specificConstant;
            if (mineType.getMapBlockType() != null && !mineType.getMapBlockType().equals(mapBlock.getMapBlockType()))
                return "You should build this building on its resources!";
            if (mineType.equals(MineType.OX_TETHER) && currentKingdom.getResourceAmount(ResourceType.COW) == 0)
                return "You do not have enough cow!";
        }
        if (buildingType.specificConstant instanceof StockType) {
            if(currentKingdom.getNumberOfStock(buildingType) > 0)
                if (!checkForNearStock(mapBlock, buildingType)) return "You should build your stocks next to each other!";
        }
        return null;
    }

    public void createBuilding(MapBlock mapBlock, BuildingType buildingType) {
        Building building;
        if (buildingType.specificConstant == null) building = new Building(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof DefensiveStructureType) building = new DefensiveStructure(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof CampType) building = new Camp(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof StockType) building = new Stock(mapBlock, buildingType, currentKingdom);
        else if(buildingType.specificConstant instanceof ProducerType) building = new Producer(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof MineType) building = new Mine(mapBlock, buildingType, currentKingdom);
        else building = new Building(mapBlock, buildingType, currentKingdom);
        currentKingdom.setBalance((double) -buildingType.getGOLD());
        if (buildingType.getRESOURCES() != null) currentKingdom.setResourceAmount(buildingType.getRESOURCES(),-buildingType.getRESOURCE_NUMBER());
        mapBlock.setBuildings(building);
        if (buildingType.equals(BuildingType.OX_TETHER)) {
            currentKingdom.setMoveOfCows(building, -1);
            currentKingdom.setResourceAmount(ResourceType.COW, -1);
        }
        if (buildingType.equals(BuildingType.DRAWBRIDGE)) gameMap.setGateFlag(building, currentKingdom.getFlag());
        if (buildingType.equals(BuildingType.BIG_STONE_GATEHOUSE) || buildingType.equals(BuildingType.SMALL_STONE_GATEHOUSE))
            setGate(checkGate(mapBlock), mapBlock ,building);
        else if (!(building instanceof Camp) && !building.getBuildingType().equals(BuildingType.STAIRS))
            for (Direction direction : Direction.values()) gameMap.changeAccess(mapBlock.getxPosition(), mapBlock.getyPosition(), direction ,false);
        currentKingdom.addBuilding(building);
        currentKingdom.changeNonWorkingUnitPosition(buildingType.getWorkerNeeded(), mapBlock, buildingType.getNumberOfWorker());
    }

    public boolean checkStairPosition(MapBlock currentBlock) {
        int x = currentBlock.getxPosition();
        int y = currentBlock.getyPosition();
        int flag = 0;
        MapBlock mapBlock;
        for (int i = -1; i <= 1; i++) {
            if ( i + x  > gameMap.getMapWidth() || i + x < 0) continue;
            for (int j = -1; j <= 1; j++) {
                if (j == i || (j + i == 0)) continue;
                if (j + y > gameMap.getMapHeight() || j + y < 0) continue;
                mapBlock = gameMap.getMapBlockByLocation(x + i, y + j);
                if (mapBlock.getBuildings() != null) {
                    if (mapBlock.getBuildings().getSpecificConstant() instanceof DefensiveStructureType) {
                        if (i == 1)
                            gameMap.changeAccess(mapBlock.getxPosition(), mapBlock.getyPosition(), Direction.WEST, true);
                        else if (i == -1)
                            gameMap.changeAccess(mapBlock.getxPosition(), mapBlock.getyPosition(), Direction.EAST, true);
                        else if (j == 1)
                            gameMap.changeAccess(mapBlock.getxPosition(), mapBlock.getyPosition(), Direction.SOUTH, true);
                        else gameMap.changeAccess(mapBlock.getxPosition(), mapBlock.getyPosition(), Direction.NORTH, true);
                        flag = 1;
                    }
                }
            }
        }
        return flag != 0;
    }

    public Integer checkGate(MapBlock mapBlock) {
        if (mapBlock.getxPosition() + 1 > gameMap.getMapWidth() || mapBlock.getxPosition() - 1 < 0 ||
            mapBlock.getyPosition() + 1 > gameMap.getMapHeight() || mapBlock.getyPosition() - 1 < 0) return null;
        boolean checkNorth = gameMap.checkAccess(mapBlock.getxPosition(), mapBlock.getyPosition() + 1, Direction.NORTH);
        boolean checkSouth = gameMap.checkAccess(mapBlock.getxPosition(), mapBlock.getyPosition() - 1, Direction.SOUTH);
        boolean checkWest = gameMap.checkAccess(mapBlock.getxPosition() + 1, mapBlock.getyPosition(), Direction.WEST);
        boolean checkEast = gameMap.checkAccess(mapBlock.getxPosition() - 1, mapBlock.getyPosition(), Direction.EAST);
        if (checkEast && checkWest) return 1;
        else if (checkNorth && checkSouth) return 0;
        else return null;
    }

    public void setGate(Integer directions, MapBlock mapBlock, Building building) {
        if (directions.equals(0)) {
            gameMap.setGateDirection(building, Direction.NORTH);
            gameMap.changeAccess(mapBlock.getxPosition(), mapBlock.getyPosition(), Direction.WEST, false);
            gameMap.changeAccess(mapBlock.getxPosition(), mapBlock.getyPosition(), Direction.EAST, false);
        }
        else {
            gameMap.setGateDirection(building, Direction.EAST);
            gameMap.changeAccess(mapBlock.getxPosition(), mapBlock.getyPosition(), Direction.NORTH, false);
            gameMap.changeAccess(mapBlock.getxPosition(), mapBlock.getyPosition(), Direction.SOUTH, false);
        }
        gameMap.setGateFlag(building, currentKingdom.getFlag());
    }

    public MapBlock checkBridgePosition(MapBlock currentBlock) {
        int x = currentBlock.getxPosition();
        int y = currentBlock.getyPosition();
        MapBlock mapBlock;
        for (int i = -1; i <= 1; i++) {
            if ( i + x  > gameMap.getMapWidth() || i + x < 0) continue;
            for (int j = -1; j <= 1; j++) {
                if (j == i || (j + i == 0)) continue;
                if ( j + y  > gameMap.getMapHeight() || j + y < 0) continue;
                mapBlock = gameMap.getMapBlockByLocation(x + i, y + j);
                if (mapBlock.getBuildings() != null) {
                    if (mapBlock.getBuildings().getBuildingType().equals(BuildingType.SMALL_STONE_GATEHOUSE) ||
                            mapBlock.getBuildings().getBuildingType().equals(BuildingType.BIG_STONE_GATEHOUSE)) {
                        if (gameMap.getGateDirection(mapBlock.getBuildings()).equals(Direction.NORTH) && (x == (x + i)))
                            return mapBlock;
                        if (gameMap.getGateDirection(mapBlock.getBuildings()).equals(Direction.EAST) && (y == (y + j)))
                            return mapBlock;
                    }
                }
            }
        }
        return null;
    }

    public String dropBuilding(HashMap<String, String> options) {
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (!currentKingdom.checkOutOfRange(mapBlock.getxPosition(), mapBlock.getyPosition())) return "This block is out of range!";
        BuildingType buildingType = BuildingType.valueOf(options.get("t"));
        if (buildingType.equals(BuildingType.HEAD_QUARTER)) return "end";
        if (!mapBlock.getMapBlockType().isBuildable() && !(buildingType.specificConstant instanceof MineType))
            return "You can not build your building here.";
        if (buildingType.equals(BuildingType.OX_TETHER) && !mapBlock.getMapBlockType().isBuildable())
            return "You can not build your building here.";
        if (mapBlock.getBuildings() != null || mapBlock.getSiege() != null) return "There is a building in this block!";
        if (buildingType.getGOLD() > currentKingdom.getBalance()) return "You dont have enough balance!";
        if (buildingType.equals(BuildingType.BIG_STONE_GATEHOUSE) || buildingType.equals(BuildingType.SMALL_STONE_GATEHOUSE))
            if (checkGate(mapBlock) == null) return "You can not build your gate here!";
        if (buildingType.equals(BuildingType.DRAWBRIDGE)) if (checkBridgePosition(mapBlock) == null) return "You can not build your bridge here!";
        if (buildingType.getRESOURCES() != null && buildingType.getRESOURCE_NUMBER() > currentKingdom.getResourceAmount(buildingType.getRESOURCES()))
            return "You do not have enough " + buildingType.getRESOURCES().name().toLowerCase() + "!";
        if (currentKingdom.checkForAvailableNormalUnit(buildingType.getWorkerNeeded()) < buildingType.getNumberOfWorker())
            return "There are not enough available worker!";
        String result;
        result = checkSpecificBuilding(mapBlock, buildingType);
        if (result != null) return result;
        if (buildingType.equals(BuildingType.STAIRS)) if (!checkStairPosition(mapBlock)) return "You can not put your stairs here!";
        createBuilding(mapBlock, buildingType);
        return "done";
    }

    public String selectBuilding(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (mapBlock.getBuildings() == null) return "There is no building found in this position!";
        if (!gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")))
                .getBuildings().getOwner().getOwner().equals(currentUser) &&
                !mapBlock.getBuildings().getBuildingType().equals(BuildingType.SMALL_STONE_GATEHOUSE) &&
                !mapBlock.getBuildings().getBuildingType().equals(BuildingType.BIG_STONE_GATEHOUSE))
            return "You can not access to this building cause you do not own it!";
        GameController.selectedBuilding = mapBlock.getBuildings();
        return "building";
    }

    public String selectSiege(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (mapBlock.getSiege() == null) return "There is no siege found in this position!";
        if (!gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")))
                .getSiege().getOwner().getOwner().equals(currentUser))
            return "You can not access to this building cause you do not own it!";
        GameController.selectedBuilding = mapBlock.getSiege();
        return "building";
    }

    public String handelUnitCommands(String text, MapBlock mouseOnBlock) {
        if(mouseOnBlock == null) {
            if (MapDesignController.selectedBlocks.size() != 1)
                return "select just one block";
            else
                mouseOnBlock = MapDesignController.selectedBlocks.get(0);
        }
        if(unitController == null) {
            return "first select units";
        }
        String result = unitController.moveMultipleUnits(mouseOnBlock, text);
        if(!result.equals(""))
            return result;
        return null;
    }

    public String showMap(HashMap<String, String> options){
        for (String key: options.keySet())
            if(options.get(key) == null)
                return "input necessary options";
        for (String key: options.keySet())
            if(!options.get(key).matches("\\d*") )
                return "input numbers as arguments";
        int xPosition = Integer.parseInt(options.get("x")) ;
        int yPosition = Integer.parseInt(options.get("y")) ;
        XofMap = xPosition;
        YofMap = yPosition;
        return gameMap.getPartOfMap(xPosition, yPosition);
    }

    public String checkUnitValidation(HashMap<String, String> options) {
        UnitType unitType;
        try {unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is no such unit exist!";}
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (mapBlock.getUnitByUnitType(unitType, currentKingdom).size() == 0) return "There is no such unit found here!";
        return null;
    }

    public String selectUnit(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result = checkUnitValidation(options);
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        UnitType unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
        selectedUnit.clear();
        selectedUnit.addAll(mapBlock.getUnitByUnitType(unitType, currentKingdom));
        return "unit";
    }

//    public String showPopularityFactors() {
//        return "Popularity factors:\nFood => " + showFoodRate() + "\nTax rate => " + showTaxRate() +
//                "\nReligion => " + showReligion() + "\nFear rate => " + showFearRate();
//    }

    public String showPopularity() {
        return "Popularity rate : " + currentKingdom.getPopularity();
    }

    public String showFoodList() {
        StringBuilder output = new StringBuilder("Food list: ");
        for (Food food : currentKingdom.getFoods().keySet()) {
            output.append("\n").append(food.name().toLowerCase()).append(" : ").append(currentKingdom.getFoods().get(food));
        }
        return output.toString();
    }

    public String setFoodRate(int rateNumber) {
        currentKingdom.setFoodRate(rateNumber);
        int getFood = (int) (((double)(currentKingdom.getFoodRate() + 2) / 2) * (double)currentKingdom.getPopulation());
        int foodCounter = 0;
        for (Food food : currentKingdom.getFoods().keySet()) foodCounter += currentKingdom.getFoods().get(food);
        if (foodCounter - getFood < 0) {
            currentKingdom.setFoodRate(-2);
            return "Food rate set to -2 automatically";
        }
        else {
            currentKingdom.setTaxRate(getFood);
            return "done";
        }
    }

    public String showFoodRate() {
        return "Food rate : " + currentKingdom.getFoodRate();
    }

    public String showReligion() {
        int counter = 0;
        BuildingType buildingType;
        for (Building building : currentKingdom.getBuildings()) {
            buildingType = building.getBuildingType();
            if (buildingType.equals(BuildingType.CATHEDRAL) || buildingType.equals(BuildingType.CHURCH))
                counter++;
        }
        return "Religious building : " + counter;
    }

    public String setTaxRate(int taxRate) {
        double getTax;
        if (currentKingdom.getTaxRate() < 0) getTax = currentKingdom.getTaxRate() * 0.2 - 0.4;
        else getTax = currentKingdom.getTaxRate() * 0.2 + 0.4;
        if (currentKingdom.getBalance() + getTax * currentKingdom.getPopulation() < 0) {
            currentKingdom.setTaxRate(0);
            return "Tax rate set to 0 automatically";
        }
        else {
            currentKingdom.setTaxRate(taxRate);
            return "done";
        }
    }


    public String showFearRate() {
        return "Fear rate : " + currentKingdom.getFearRate();
    }

    public String dropTrap(HashMap<String, String> options){
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock target = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if(!currentKingdom.checkOutOfRange(target.getxPosition(), target.getyPosition()))
            return "it is not legal to drop trap outside of kingdom";
        TrapType trapType;
        try {trapType = TrapType.valueOf(options.get("t").toUpperCase().replaceAll(" ", "_"));}
        catch (Exception exception) {return "no such type of trap in the game";}
        if(trapType.equals(TrapType.BITUMEN_TRENCH)) {
            if (currentKingdom.getResourceAmount(ResourceType.RIG) > 10)
                currentKingdom.setResourceAmount(ResourceType.RIG, -10);
            else return "not enough resource to drop bitumen trench";
        }
        if(!trapType.equals(TrapType.DOGS_CAGE) && !target.getMapBlockType().equals(MapBlockType.HOLE))
            return "first dig hole to drop trenches";
        if (trapType.getPrice() > currentKingdom.getBalance()) return "You do not have enough gold to buy this trap.";
        currentKingdom.setBalance((double) -trapType.getPrice());
        Trap newTrap = new Trap(currentKingdom, target, trapType);
        target.setTrap(newTrap);
        return "trap added successfully";
    }

}
