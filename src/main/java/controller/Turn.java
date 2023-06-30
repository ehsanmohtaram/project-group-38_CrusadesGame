package controller;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;
import view.Style;
import view.animation.FireAnimation;

import java.util.ArrayList;

public class Turn {

    private final Map gameMap;
    private final Kingdom currentKingdom;
    private final Style style;


    public Turn() {
        gameMap = GameController.gameMap;
        User currentUser = Controller.currentUser;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
        this.style = new Style();
    }


    public void runNextTurn() {
        if (currentKingdom.getPopularity() > -5) {
            executeProducerBuilding();
            executeMines();
            innCheck();
            //produceHorse();
        }
        illness();
        fire();fireGraphic();
        setReligiousBuildingPopularity();
        giveFood();
        getTax();
        move();
        growPopulation();
        battleExecution();
        removeUnitsAndBuildingWith0Hp();
        patrolExecution();
        trapsReset();
    }


    private void illness() {
        if (currentKingdom.getIllness() != null) {
            currentKingdom.setPopularity(-1);
            return;
        }
        int random = (int)(10 * Math.random());
//        if (random % 4 == 0) {
//        (int)(Math.random() * 10000) % gameMap.getMapWidth(),(int)(Math.random() * 10000) % gameMap.getMapHeight()
            MapBlock illnessBlock = gameMap.getMapBlockByLocation(5, 5);
            Rectangle rectangle = new Rectangle(100, 100);
            rectangle.setFill(new ImagePattern(new Image(Turn.class.getResource("/images/background/illness.jpg").toExternalForm())));
            illnessBlock.getChildren().add(rectangle);
            currentKingdom.setIllness(illnessBlock);
            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                        Button button = new Button();
                        style.button0(button, "Pay for health", 100, 50);
                        illnessBlock.getChildren().add(button);
                        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                illnessBlock.getChildren().remove(button);
                                illnessBlock.getChildren().remove(rectangle);
                                currentKingdom.setBalance(-3.);
                                currentKingdom.setIllness(null);
                            }
                        });
                    }
                }
            });
            //todo یه دکمه یا یه عنی که وقتی یارو زد این بیماری برطرف بشه
//        }
    }

    private void fire() {
        for (Kingdom kingdom : gameMap.getPlayers()) {
            for (Building building : kingdom.getBuildings()) {
                if (building.getFire() == -1 && building.getHp() != 0) {
                    mapBlockFor:
                    for (MapBlock mapBlock : getSurrond(building)) {
//                        for (Unit unit : mapBlock.getUnits()) {
//                            if (!(unit.getOwner().equals(currentKingdom)) && (unit.getUnitType().equals(UnitType.SLAVES))) {
                                building.setFire(3);
//                                break mapBlockFor;
//                            }
//                        }
                    }
                }
            }
        }
    }

    private void fireGraphic() {
        for (Kingdom kingdom : gameMap.getPlayers()) {
            for (Building building : kingdom.getBuildings()) {
                if (building.getFire() != -1) {
                    if (building.getFire() != 0) {
                        new FireAnimation(building, building.getFire()).play();
                        building.setFire(building.getFire() - 1);
                        System.out.println("1");
                    } else {
                        System.out.println("end");
                        Rectangle rectangle = new Rectangle(100, 100);
                        rectangle.setFill(new ImagePattern(new Image(Turn.class.getResource("/images/background/illness.jpg").toExternalForm())));
                        building.getPosition().getChildren().add(rectangle);
                    }
                }
            }
        }
    }


    public ArrayList<MapBlock> getSurrond(Building building){
        ArrayList<MapBlock> mapBlocks = new ArrayList<>();
        int x = building.getPosition().getxPosition(), y = building.getPosition().getyPosition();
        mapBlocks.add(gameMap.getMapBlockByLocation(x, y + 1));mapBlocks.add(gameMap.getMapBlockByLocation(x + 1, y + 1));
        mapBlocks.add(gameMap.getMapBlockByLocation(x + 1, y));mapBlocks.add(gameMap.getMapBlockByLocation(x + 1, y - 1));
        mapBlocks.add(gameMap.getMapBlockByLocation(x, y - 1));mapBlocks.add(gameMap.getMapBlockByLocation(x - 1, y - 1));
        mapBlocks.add(gameMap.getMapBlockByLocation(x - 1, y));mapBlocks.add(gameMap.getMapBlockByLocation(x - 1, y + 1));
        return mapBlocks;
    }


    public void growPopulation() {
        int foodCounter = 0;
        for (Food food : currentKingdom.getFoods().keySet()) foodCounter += currentKingdom.getFoods().get(food);
        int addPeople = currentKingdom.getMaxPopulation() - currentKingdom.getPopulation();
        int growthAmount = addPeople - (int)((((double) addPeople + (double) currentKingdom.getPopulation()) / (double) foodCounter) * (double) addPeople);
        if (addPeople >= 0 && growthAmount >= 0) currentKingdom.setPopulation(growthAmount);
    }

    public void getTax() {
        double getTax;
        if (currentKingdom.getTaxRate() <= 0) {
            getTax = currentKingdom.getTaxRate() * 0.2 - 0.4;
            if (currentKingdom.getTaxRate() == 0) getTax = 0;
            currentKingdom.setPopularity(-currentKingdom.getTaxRate() * 2 + 1);
        }
        else {
            getTax = currentKingdom.getTaxRate() * 0.2 + 0.4;
            currentKingdom.setPopularity(-currentKingdom.getTaxRate() * 2);
        }
        currentKingdom.setBalance(getTax * currentKingdom.getPopulation() * (gameMap.getPlayers().size() - 1));
    }

    public void giveFood() {
        if (currentKingdom.getFoodRate().equals(-2)){currentKingdom.setPopularity((gameMap.getPlayers().size() - 1) * -8); return;}
        int foodCounter = 0, setFood, checkFoodAmount = 0, variety = 0;
        int getFood = (int) (((double)(currentKingdom.getFoodRate() + 2) / 2) * (double)currentKingdom.getPopulation());
        for (Food food : currentKingdom.getFoods().keySet()) foodCounter += currentKingdom.getFoods().get(food);
        for (Food food : currentKingdom.getFoods().keySet()) {
            if (currentKingdom.getFoods().get(food) > 0) variety++;
            setFood = (int)(((double) currentKingdom.getFoods().get(food) / (double) foodCounter) * (double) getFood);
            checkFoodAmount += setFood;
            currentKingdom.setFoodsAmount(food, -setFood);
        }
        if (variety != 0) currentKingdom.setPopularity((gameMap.getPlayers().size() - 1) * 2);
        currentKingdom.setPopularity((gameMap.getPlayers().size() - 1) * currentKingdom.getFoodRate() * 4);
        checkFoodAmount = getFood - checkFoodAmount;
        if (checkFoodAmount == 0) return;
        while (true) {
            for (Food food : currentKingdom.getFoods().keySet()) {
                if (currentKingdom.getFoods().get(food) > 0) {
                    currentKingdom.setFoodsAmount(food, -1); checkFoodAmount--;
                    if (checkFoodAmount == 0) return;
                }
            }
        }

    }

    public void innCheck() {
        for (Building building : currentKingdom.getBuildings())
            if (building.getBuildingType().equals(BuildingType.INN)) {
                currentKingdom.setPopularity(2 * (gameMap.getPlayers().size() - 1));
                if (currentKingdom.getResourceAmount(ResourceType.HOP) - (gameMap.getPlayers().size() - 1) * 5 < 0)
                    currentKingdom.setResourceAmount(ResourceType.HOP.getBaseSource(), -currentKingdom.getResourceAmount(ResourceType.HOP));
                else
                    currentKingdom.setResourceAmount(ResourceType.HOP.getBaseSource(), -(gameMap.getPlayers().size() - 1) * 5);
            }
    }

    public void setReligiousBuildingPopularity() {
        currentKingdom.setPopularity(-currentKingdom.getFearRate() * (gameMap.getPlayers().size() - 1));
        int popularity = 0;
        for (Building building : currentKingdom.getBuildings())
            if (building.getBuildingType().equals(BuildingType.CATHEDRAL) || building.getBuildingType().equals(BuildingType.CHURCH))
                popularity += 2;
        popularity += popularity * (gameMap.getPlayers().size() -1);
        currentKingdom.setPopularity(popularity);
    }

    public void produceHorse() {
        Camp camp;
        CampType campType;
        for (Building building : currentKingdom.getBuildings())
            if (building.getSpecificConstant().equals(CampType.STABLE)) {
                campType = (CampType) building.getSpecificConstant();
                camp = (Camp) building;
                if (camp.getCapacity().equals(campType.getCapacity())) continue;
                camp.setCapacity(1);
                currentKingdom.setBalance((double) -20);
            }
    }


    public Integer setProduceRate(Integer produceRate) {
        double fearRate = (double) (currentKingdom.getFearRate()) / 10;
        produceRate += (int) ((double) produceRate * fearRate);
        produceRate *= (gameMap.getPlayers().size() - 1);
        return produceRate;
    }

    public void executeMines() {
        MineType mineType;
        Mine mine;
        int produceRate;
        for (Building building : currentKingdom.getBuildings()) {
            if (building instanceof Mine) {
                mineType = (MineType) building.getSpecificConstant();
                mine = (Mine) building;
                MapBlock mapBlock;
                System.out.println(mine.getMineMode());
                if (mine.getMineMode().equals(ProduceMode.NON_ACTIVE)) continue;
                produceRate = setProduceRate(mineType.getProduceRate());
                if (mine.getSpecificConstant().equals(MineType.QUARRY))
                    produceRate = setProduceRate(MineType.OX_TETHER.getProduceRate());
                if (!mine.getSpecificConstant().equals(MineType.OX_TETHER)) {
                    if (mineType.equals(MineType.QUARRY) && currentKingdom.getBuildingFormKingdom(BuildingType.OX_TETHER) == null)
                        continue;
                    if ((mapBlock = checkNearBlocksForResource(building.getPosition(), mineType.getResourceType())) == null)
                        continue;
                    if (!checkMineProduce(mineType.getResourceType(), mapBlock, produceRate)) continue;
                    if (mineType.equals(MineType.QUARRY)) {
                        if (findNearestOX(building.getPosition()) != null) {
                            if (calculateCowsMoving((Mine) findNearestOX(building.getPosition()).getBuildings(), (Mine) building)) {
                                mapBlock.setResources(mineType.getResourceType(), mapBlock.getResourceAmount() - produceRate);
                                currentKingdom.setResourceAmount(mineType.getResourceType(), produceRate);
                            }
                        }
                    } else {
                        mapBlock.setResources(mineType.getResourceType(), mapBlock.getResourceAmount() - produceRate);
                        currentKingdom.setResourceAmount(mineType.getResourceType(), produceRate);
                    }
                }
            }
        }
    }

    public boolean checkMineProduce(ResourceType resourceType, MapBlock mapBlock, Integer produceRate) {
        if (mapBlock.getResourceAmount() - produceRate < 0) {
            produceResource(resourceType, mapBlock.getResourceAmount());
            mapBlock.setResources(resourceType, 0);
            return false;
        }
        if (currentKingdom.getResourceAmount(resourceType) + produceRate >
                currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) * StockType.STOCKPILE.getCAPACITY()) {
            int amount = currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) * StockType.STOCKPILE.getCAPACITY() -
                    currentKingdom.getResourceAmount(resourceType);
            produceResource(resourceType, amount);
            mapBlock.setResources(resourceType, mapBlock.getResourceAmount() - amount);
            return false;
        }
        return true;
    }

    public MapBlock checkNearBlocksForResource(MapBlock currentBlock, ResourceType resourceType) {
        int x  = currentBlock.getxPosition(), y = currentBlock.getyPosition();
        MapBlock mapBlock;
        for (int i = -1; i < 2; i++) {
            if ( i + x > gameMap.getMapWidth() || i + x < 0) continue;
            for (int j = -1; j < 2; j++) {
                if ( j + y  > gameMap.getMapHeight() || j + y < 0) continue;
                mapBlock = gameMap.getMapBlockByLocation(x + i, y + j);
                if (mapBlock.getResources().equals(resourceType) && mapBlock.getResourceAmount() > 0)
                    return mapBlock;
            }
        }
        currentKingdom.getUnits().removeAll(currentBlock.getUnitByUnitType(UnitType.WORKER, currentKingdom));
        currentBlock.getUnits().removeAll(currentBlock.getUnitByUnitType(UnitType.WORKER, currentKingdom));
        currentKingdom.setNoneEmployed(currentBlock.getUnitByUnitType(UnitType.WORKER, currentKingdom).size());
        ((Mine) currentBlock.getBuildings()).setMineMode(ProduceMode.NON_ACTIVE);
        return null;
    }

    public void executeProducerBuilding() {
        for (Building building : currentKingdom.getBuildings())
            if (building instanceof Producer && !((Producer) building).getMode().equals(ProduceMode.NON_ACTIVE))
                checkProduce((Producer) building);
    }

    public void produceResource(ResourceType resourceType, Integer amount) {
        currentKingdom.setResourceAmount(resourceType, amount);
        if(resourceType.getBaseSource() != null)
            currentKingdom.setResourceAmount(resourceType.getBaseSource(), -amount);
    }

    public void produceWeapon(Weapons weapons, Integer amount) {
        currentKingdom.setWeaponsAmount(weapons, amount);
        currentKingdom.setResourceAmount(weapons.getResourceType(), -amount);
    }

    public void produceFood(Food food, Integer amount) {
        currentKingdom.setFoodsAmount(food, amount);
        currentKingdom.setResourceAmount(food.getResourceType(), -amount);
    }

    public void checkProduce(Producer producer) {
        ProducerType producerType = (ProducerType) producer.getSpecificConstant();
        Enum<?> check0, check1;
        Integer amount = 1;
        check0 = producerType.getTypeOfResource0();
        check1 = producerType.getTypeOfResource1();
        if (check0 != null && check1 != null && !producer.getMode().equals(ProduceMode.FIRST) && !producer.getMode().equals(ProduceMode.SECOND))
            amount = 2;
        if (checkCapacity(check0, setProduceRate(producerType.getProduceRate()) / amount)) {
            if (!producer.getMode().equals(ProduceMode.SECOND)) {
                if (check0 instanceof Weapons) produceWeapon((Weapons) check0, setProduceRate(producerType.getProduceRate()) / amount);
                else if (check0 instanceof Food) produceFood((Food) check0, setProduceRate(producerType.getProduceRate()) / amount);
                else if (check0 != null) produceResource((ResourceType) check0, setProduceRate(producerType.getProduceRate()) / amount);
            }
        }
        if (checkCapacity(check1, setProduceRate(producerType.getProduceRate()) / amount)) {
            if (!producer.getMode().equals(ProduceMode.FIRST) && check1 != null)
                produceWeapon((Weapons) check1, setProduceRate(producerType.getProduceRate()) / amount);
        }
    }


    public boolean checkFoodCapacity(Food food, Integer produceRate) {
        if (currentKingdom.getResourceAmount(food.getResourceType()) - produceRate < 0) {
            produceFood(food, currentKingdom.getResourceAmount(food.getResourceType()));
            return false;
        }
        if (currentKingdom.getFoodAmount(food) + produceRate >
                currentKingdom.getNumberOfStock(BuildingType.FOOD_STOCKPILE) * StockType.FOOD_STOCKPILE.getCAPACITY()) {
            produceFood(food, currentKingdom.getNumberOfStock(BuildingType.FOOD_STOCKPILE) *
                    StockType.FOOD_STOCKPILE.getCAPACITY() - currentKingdom.getFoodAmount(food));
            return false;
        }
        return true;
    }

    public boolean checkResourceCapacity(ResourceType resourceType, Integer produceRate) {
        if (currentKingdom.getResourceAmount(resourceType.getBaseSource()) - produceRate < 0) {
            produceResource(resourceType, currentKingdom.getResourceAmount(resourceType.getBaseSource()));
            return false;
        }
        if (currentKingdom.getResourceAmount(resourceType) + produceRate >
                currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) * StockType.STOCKPILE.getCAPACITY()) {
            produceResource(resourceType, currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) *
                    StockType.STOCKPILE.getCAPACITY() - currentKingdom.getResourceAmount(resourceType));
            return false;
        }
        return true;
    }

    public boolean checkWeaponCapacity(Weapons weapons, Integer produceRate) {
        if (currentKingdom.getResourceAmount(weapons.getResourceType()) - produceRate < 0) {
            produceWeapon(weapons, currentKingdom.getResourceAmount(weapons.getResourceType()));
            return false;
        }
        if (currentKingdom.getWeaponAmount(weapons) + produceRate >
                currentKingdom.getNumberOfStock(BuildingType.ARMOURY) * StockType.ARMOURY.getCAPACITY()) {
            produceWeapon(weapons, currentKingdom.getNumberOfStock(BuildingType.ARMOURY) *
                    StockType.ARMOURY.getCAPACITY() - currentKingdom.getWeaponAmount(weapons));
            return false;
        }
        return true;
    }

    public boolean checkCapacity(Enum<?> resource, Integer produceRate) {
        if (resource == null) return false;
        if (resource instanceof ResourceType) return checkResourceCapacity((ResourceType) resource, produceRate);
        else if (resource instanceof Food) return checkFoodCapacity((Food) resource, produceRate);
        else return checkWeaponCapacity((Weapons) resource, produceRate);
    }

    public MapBlock findNearestOX(MapBlock mapBlock) {
        int min = gameMap.getMapWidth() * gameMap.getMapWidth() + gameMap.getMapHeight() * gameMap.getMapHeight();
        MapBlock minDistanceToOx = null;
        for (Building building : currentKingdom.getBuildings())
            if (building.getBuildingType().equals(BuildingType.OX_TETHER))
                if (min > (mapBlock.getxPosition() - building.getPosition().getxPosition()) * (mapBlock.getxPosition() - building.getPosition().getxPosition()) +
                        (mapBlock.getyPosition() - building.getPosition().getyPosition()) * (mapBlock.getyPosition() - building.getPosition().getyPosition())) {
                    min = (mapBlock.getxPosition() - building.getPosition().getxPosition()) * (mapBlock.getxPosition() - building.getPosition().getxPosition()) +
                            (mapBlock.getyPosition() - building.getPosition().getyPosition()) * (mapBlock.getyPosition() - building.getPosition().getyPosition());
                    minDistanceToOx = building.getPosition();
                }
        return minDistanceToOx;
    }

    public boolean calculateCowsMoving(Mine oxPosition, Mine query) {
        int distance;
        int x1 = oxPosition.getPosition().getxPosition();
        int x2 = query.getPosition().getxPosition();
        int y1 = oxPosition.getPosition().getyPosition();
        int y2 = query.getPosition().getyPosition();
        distance = (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        if (currentKingdom.getMoveOfCows(oxPosition) <= 0) {currentKingdom.setMoveOfCows(oxPosition, distance); return true;}
        else {currentKingdom.setMoveOfCows(oxPosition, currentKingdom.getMoveOfCows(oxPosition) - 2); return false;}
    }

    public void move() {
        for (Building building : currentKingdom.getRemainingBuildingMove())
            building.getPosition().setSiege(building);
        currentKingdom.getRemainingBuildingMove().clear();
        for (Unit unit : currentKingdom.getRemainingUnitMove()) {
            if (unit.getUnitType().getIS_ARAB().equals(-4))
                if (unit.getLocationBlock().getUnits().size() == 0) unit.getLocationBlock().addUnitHere(unit);
                else unit.getLocationBlock().getUnits().set(0, unit);

        }
        currentKingdom.getRemainingUnitMove().clear();
    }

    public void removeUnitsAndBuildingWith0Hp() {
        for (MapBlock[] mapBlock : gameMap.getMap()) {
            for (MapBlock checkBlock : mapBlock) {
                removeDestroyedBuilding(checkBlock);
                removeSiegeUnits(checkBlock);
                removeDeadUnits(checkBlock);
            }
        }
    }

    public void removeDestroyedBuilding(MapBlock checkBlock) {
        Kingdom owner;
        if (checkBlock.getBuildings() != null) {
            owner = checkBlock.getBuildings().getOwner();
            if (checkBlock.getBuildings().getHp() <= 0) {
                if (checkBlock.getBuildings().getBuildingType().equals(BuildingType.HEAD_QUARTER)) {
                    checkBlock.getBuildings().getOwner().setHeadquarter(null);
                }
                if (checkBlock.getBuildings() instanceof Stock) {
                    for (Enum<?> resource : ((Stock)checkBlock.getBuildings()).getResourceValues().keySet()) {
                        if (resource instanceof Food)
                            owner.setFoodsAmount((Food) resource, -((Stock)checkBlock.getBuildings()).getResourceValues().get(resource));
                        else if (resource instanceof Weapons)
                            owner.setWeaponsAmount((Weapons) resource, -((Stock)checkBlock.getBuildings()).getResourceValues().get(resource));
                        else owner.setResourceAmount((ResourceType) resource, -((Stock)checkBlock.getBuildings()).getResourceValues().get(resource));
                    }
                }
                for (Direction direction : Direction.values()) gameMap.changeAccess(checkBlock.getxPosition(), checkBlock.getyPosition(), direction ,true);
                checkBlock.getBuildings().getOwner().getBuildings().remove(checkBlock.getBuildings());
                checkBlock.setBuildings(null);
            }
        }
    }

    public void removeDeadUnits(MapBlock checkBlock) {
        ArrayList<Unit> units = new ArrayList<>();
        if (checkBlock.getUnits().size() != 0) {
            for (Unit unit : checkBlock.getUnits()) {
                if (unit.getHp() <= 0 ) {
                    units.add(unit);
                }
            }
            for (Unit unit : units) {
                unit.getOwner().getUnits().remove(unit);
                unit.removeUnit();
            }
        }
    }

    public void removeSiegeUnits(MapBlock checkBlock) {
        Building building;
        int counter = 0;
        if (checkBlock.getSiege() != null) {
            building = checkBlock.getSiege();
            if (checkBlock.getUnits().get(0).getHp() <= 0) {
                while (building.getBuildingType().getNumberOfWorker() != counter || building.getPosition().getUnits().size() != 0) {
                    for (Unit findUnit : checkBlock.getUnits())
                        if (findUnit.getUnitType().equals(UnitType.ENGINEER) && findUnit.getOwner().equals(building.getOwner())) {
                            building.getPosition().removeUnitFromHere(findUnit);
                            findUnit.getOwner().setNoneEmployed(1);
                            break;
                        }
                    counter++;
                }
                checkBlock.getSiege().getOwner().getBuildings().remove(checkBlock.getBuildings());
                checkBlock.setSiege(null);
            }
        }
    }

    public void battleExecution(){
        for (Unit aggressiveUnit : Unit.AggressiveUnits)
            aggressiveUnitsFight(aggressiveUnit);
        for (Kingdom player : gameMap.getPlayers()) {
            for (Unit unit : player.getUnits()) {
                if(unit.getUnitType().equals(UnitType.WORKER))
                    continue;
                if(unit.getUnitState().equals(UnitState.DEFENSIVE)) {
                    ArrayList<Unit> enemies = gameMap.getEnemiesInAttackRange(unit, true);
                    if(enemies.size() == 0)
                        continue;
                    if(unit.getUnitType().equals(UnitType.ENGINEER) && player.getResourceAmount(ResourceType.OIL) > 0) {
                        player.setResourceAmount(ResourceType.OIL, -2);
                        for (Unit enemy : enemies.get(0).getLocationBlock().getUnits())
                            unit.unilateralFight(enemy);
                    }
                    Unit nearestEnemy = gameMap.getEnemiesInAttackRange(unit, true).get(0);
                    if(nearestEnemy.getUnitState().equals(UnitState.OFFENSIVE))
                        unit.bilateralFight(nearestEnemy, true);
                    else
                        unit.unilateralFight(nearestEnemy);
                }
            }
        }

        for (Kingdom player : gameMap.getPlayers())
            for (Trap trap : player.getTraps())
                if(trap.getTrapType().equals(TrapType.DOGS_CAGE))
                    for (Unit unit : gameMap.getEnemiesInSurroundingArea(trap.getLocationBlock().getxPosition(), trap.getLocationBlock().getyPosition(),
                            player, false, 5))
                        unit.decreaseHp(trap.getTrapType().getDamage());


        for (Kingdom player : gameMap.getPlayers()) {
            for (Unit unit : player.getUnits()) {
                unit.resetAttributes();
            }
        }
    }

    private void aggressiveUnitsFight(Unit unit){
        ArrayList<Unit> enemies = gameMap.getEnemiesInAttackRange(unit, true);
        if(enemies.size() == 0)
            return;
        if(unit.getUnitType().equals(UnitType.ENGINEER) && unit.getOwner().getResourceAmount(ResourceType.OIL) > 0) {
            unit.getOwner().setResourceAmount(ResourceType.OIL, -2);
            for (Unit enemy : enemies.get(0).getLocationBlock().getUnits())
                unit.bilateralFight(enemy, false);
        }

        for (Unit defender : enemies ) {
            unit.moveTo(defender.getLocationBlock(), 0, null);
            if(!unit.bilateralFightTillEnd(defender))
                return;
        }
//        unit.setUnitState(UnitState.STANDING);
    }

    private void patrolExecution(){
        for (Kingdom player : gameMap.getPlayers()) {
            for (Unit unit : player.getUnits()) {
                if(unit.getUnitState().equals(UnitState.PATROLLING)) {
                    MapBlock origin = unit.getLocationBlock();
                    unit.moveTo(unit.getPatrolDestination(), 0, null);
                    unit.setPatrolDestination(origin);
                }
            }
        }
    }

    public void trapsReset(){
        for (Kingdom player : gameMap.getPlayers()) {
            for (Trap trap : player.getTraps()) {
                if(trap.getTrapType().equals(TrapType.BITUMEN_TRENCH))
                    trap.setActive(false);
            }
        }
    }


}
