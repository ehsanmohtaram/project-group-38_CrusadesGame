package controller;
import model.*;
import model.building.*;

public class Turn {

    private final Map gameMap;
    private final Kingdom currentKingdom;
    private final User currentUser;
    private final Kingdom lastKingdom;

    public Turn() {
        gameMap = GameController.gameMap;
        currentUser = Controller.currentUser;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
        int result = ((gameMap.getPlayers().indexOf(currentKingdom) - 1) + gameMap.getPlayers().size()) % gameMap.getPlayers().size();
        lastKingdom = gameMap.getPlayers().get(result);
    }

    public void runNextTurn() {
        executeProducerBuilding();
        executeMines();
    }

    private void executeMines() {
        MineType mineType;
        for (Building building : currentKingdom.getBuildings())
            if (building instanceof Mine) {
                mineType = (MineType) building.getSpecificConstant();
                currentKingdom.setResourceAmount(mineType.getResourceType(), mineType.getProduceRate());
            }
    }

    private void executeProducerBuilding() {
        for (Building building : currentKingdom.getBuildings())
            if (building instanceof Producer && !((Producer) building).getMode().equals(ProduceMode.NON_ACTIVE))
                checkProduce((Producer) building);
    }

    private void produceResource(ResourceType resourceType, Integer amount) {
        currentKingdom.setResourceAmount(resourceType, amount);
        currentKingdom.setResourceAmount(resourceType.getBaseSource(), -amount);
    }

    private void produceWeapon(Weapons weapons, Integer amount) {
        currentKingdom.setWeaponsAmount(weapons, amount);
        currentKingdom.setResourceAmount(weapons.getResourceType(), -amount);
    }

    private void produceFood(Food food, Integer amount) {
        //TODO CAPACITY CHECK FOR STOCK AND KARGAR
        currentKingdom.setFoodAmount(food, amount);
        currentKingdom.setResourceAmount(food.getResourceType(), -amount);
    }

    private void checkProduce(Producer producer) {
        ProducerType producerType = (ProducerType) producer.getSpecificConstant();
        Enum<?> check0, check1;
        Integer amount = 1;
        check0 = producerType.getTypeOfResource0();
        check1 = producerType.getTypeOfResource1();
        if (check0 != null && check1 != null && !producer.getMode().equals(ProduceMode.FIRST) && !producer.getMode().equals(ProduceMode.SECOND))
            amount = 2;
        if (!producer.getMode().equals(ProduceMode.SECOND)) {
            if (check0 instanceof Weapons) produceWeapon((Weapons) check0, producerType.getProduceRate() / amount);
            else if (check0 instanceof Food) produceFood((Food) check0, producerType.getProduceRate() / amount);
            else produceResource((ResourceType) check0, producerType.getProduceRate() / amount);
        }
        if (!producer.getMode().equals(ProduceMode.FIRST) && check1 != null)
            produceWeapon((Weapons) check0, producerType.getProduceRate() / amount);
    }
    public static void calculateResources(){

    }

    public static void removeDeadUnits(){

    }

    public static void removeDestroyedBuildings(){

    }

    public static void checkWinOrLose(){

    }

}
