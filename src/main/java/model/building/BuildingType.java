package model.building;
import model.ResourceType;
import model.unit.UnitType;


public enum BuildingType {
    CATHEDRAL(0,null,0,1000, UnitType.WORKER,0,null),
    CHURCH(0,null,0,1000, UnitType.WORKER,0,null),
    //Defensive structure
    HEAD_QUARTER(0,null,0,10000, UnitType.WORKER,0,DefensiveStructureType.HEAD_QUARTER),
    WALL(0,ResourceType.ROCK, 5,200, UnitType.WORKER,0 ,DefensiveStructureType.WALL),
    BIG_WALL(0,ResourceType.ROCK, 10 ,400, UnitType.WORKER,0,DefensiveStructureType.BIG_WALL),
    SMALL_STONE_GATEHOUSE(0,null,0,1000, UnitType.WORKER,0,DefensiveStructureType.HEAD_QUARTER),
    BIG_STONE_GATEHOUSE(0,ResourceType.ROCK,20,2000, UnitType.WORKER,0,DefensiveStructureType.BIG_STONE_GATEHOUSE),
    DRAWBRIDGE(0,ResourceType.WOOD,10,100, UnitType.WORKER,0,DefensiveStructureType.DRAWBRIDGE),
    LOOKOUT_TOWER(0, ResourceType.ROCK, 10, 2000, UnitType.WORKER,0,DefensiveStructureType.LOOKOUT_TOWER),
    PERIMETER_TOWER(0,ResourceType.ROCK,10,1000, UnitType.WORKER,0,DefensiveStructureType.PERIMETER_TOWER),
    DEFENSE_TURRET(0,ResourceType.ROCK,15,1200, UnitType.WORKER,0,DefensiveStructureType.PERIMETER_TOWER),
    SQUARE_TOWER(0, ResourceType.ROCK,35,1600, UnitType.WORKER,0,DefensiveStructureType.SQUARE_TOWER),
    ROUND_TOWER(0,ResourceType.ROCK,40,2000, UnitType.WORKER,0,DefensiveStructureType.ROUND_TOWER),
    //Siege equipment
    MANGONEL(50, null, 0, 100, UnitType.ENGINEER,2, SiegeType.MANGONEL),
    BALLISTA(50, null, 0, 100, UnitType.ENGINEER,2,SiegeType.BALLISTA),
    CATAPULT(150, null, 0, 300, UnitType.ENGINEER,2,SiegeType.CATAPULT),
    TREBUCHET(150, null, 0, 500, UnitType.ENGINEER,3,SiegeType.TREBUCHET),
    //Camp equipment
    BARRACK(0, ResourceType.ROCK,15,500,  UnitType.WORKER,0,CampType.BARRACK),
    SIEGE_TENT(0 ,null, 0, 100,  UnitType.WORKER,0,CampType.SIEGE_TENT),
    ENGINEER_GUILD(100, ResourceType.WOOD, 10, 500,  UnitType.WORKER,0,CampType.ENGINEER_GUILD),
    MERCENARY_POST(0, ResourceType.WOOD, 10, 500,  UnitType.WORKER,0,CampType.MERCENARY_POST),
    //Stock
    STOCKPILE(0 , null, 0, 300, UnitType.WORKER,0,StockType.STOCKPILE),
    FOOD_STOCKPILE(0, ResourceType.WOOD, 5, 300, UnitType.WORKER,0,StockType.FOOD_STOCKPILE),
    ARMOURY(0, ResourceType.WOOD,5,300, UnitType.WORKER,0,StockType.ARMOURY),
    //Producer
    ARMOURER(100, ResourceType.WOOD, 20, 350, UnitType.WORKER,1,ProducerType.ARMOURER),
    BLACKSMITH(100, ResourceType.WOOD, 20, 350, UnitType.WORKER,1,ProducerType.BLACKSMITH),
    FLETCHER(100, ResourceType.WOOD, 20, 350, UnitType.WORKER,1,ProducerType.FLETCHER),
    POLE_TURNER(100, ResourceType.WOOD, 10, 300, UnitType.WORKER,1,ProducerType.POLE_TURNER),
    OIL_SMELTER(100, ResourceType.WOOD, 10, 300, UnitType.ENGINEER,1,ProducerType.OIL_SMELTER),
    BAKERY(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.BAKERY),
    DAIRY_PRODUCTS(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.DAIRY_PRODUCTS),
    BREWERY(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.BREWERY),
    MILL(0, ResourceType.WOOD, 20, 300, UnitType.WORKER, 3, ProducerType.MILL),
    //Mine
    QUARRY(0, ResourceType.WOOD, 20, 300, UnitType.WORKER, 3, MineType.QUARRY),
    IRON_MINE(0, ResourceType.WOOD, 20, 300, UnitType.WORKER, 2, MineType.IRON_MINE),
    WOOD_CUTTER(0, ResourceType.WOOD, 3, 100, UnitType.WORKER, 1, MineType.WOOD_CUTTER),
    PITCH_RIG(0 , ResourceType.WOOD, 20, 150, UnitType.WORKER, 1, MineType.PITCH_RIG);
    private final Integer GOLD;
    private final ResourceType RESOURCES;
    private final Integer RESOURCE_NUMBER;
    private final Integer HP_IN_FIRST;
    private final UnitType workerNeeded;
    private final Integer numberOfWorker;
    public final Enum<?> specificConstant;

    BuildingType(Integer GOLD, ResourceType RESOURCES, Integer RESOURCE_NUMBER, Integer HP_IN_FIRST, UnitType workerNeeded, Integer numberOfWorker, Enum<?> specificConstant) {
        this.GOLD = GOLD;
        this.RESOURCES = RESOURCES;
        this.RESOURCE_NUMBER = RESOURCE_NUMBER;
        this.HP_IN_FIRST = HP_IN_FIRST;
        this.workerNeeded = workerNeeded;
        this.numberOfWorker = numberOfWorker;
        this.specificConstant = specificConstant;
    }

    public Integer getGOLD() {
        return GOLD;
    }

    public ResourceType getRESOURCES() {
        return RESOURCES;
    }

    public Integer getRESOURCE_NUMBER() {
        return RESOURCE_NUMBER;
    }

    public Integer getHP_IN_FIRST() {
        return HP_IN_FIRST;
    }

    public UnitType getWorkerNeeded() {
        return workerNeeded;
    }

    public Integer getNumberOfWorker() {
        return numberOfWorker;
    }
}
