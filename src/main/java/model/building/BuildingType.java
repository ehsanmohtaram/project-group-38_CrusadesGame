package model.building;
import model.ResourceType;


public enum BuildingType {
    CATHEDRAL(0,null,0,1000, null),
    CHURCH(0,null,0,1000, null),
    //Defensive structure
    HEAD_QUARTER(0,null,0,10000, DefensiveStructureType.HEAD_QUARTER),
    WALL(0,ResourceType.ROCK, 5,200, DefensiveStructureType.WALL),
    BIG_WALL(0,ResourceType.ROCK, 10 ,400, DefensiveStructureType.BIG_WALL),
    SMALL_STONE_GATEHOUSE(0,null,0,1000, DefensiveStructureType.HEAD_QUARTER),
    BIG_STONE_GATEHOUSE(0,ResourceType.ROCK,20,2000,DefensiveStructureType.BIG_STONE_GATEHOUSE),
    DRAWBRIDGE(0,ResourceType.WOOD,10,100, DefensiveStructureType.DRAWBRIDGE),
    LOOKOUT_TOWER(0, ResourceType.ROCK, 10, 2000,DefensiveStructureType.LOOKOUT_TOWER),
    PERIMETER_TOWER(0,ResourceType.ROCK,10,1000, DefensiveStructureType.PERIMETER_TOWER),
    DEFENSE_TURRET(0,ResourceType.ROCK,15,1200, DefensiveStructureType.PERIMETER_TOWER),
    SQUARE_TOWER(0, ResourceType.ROCK,35,1600, DefensiveStructureType.SQUARE_TOWER),
    ROUND_TOWER(0,ResourceType.ROCK,40,2000, DefensiveStructureType.ROUND_TOWER),
    //Siege equipment
    MANGONEL(50, null, 0, 100, SiegeType.MANGONEL),
    BALLISTA(50, null, 0, 100, SiegeType.BALLISTA),
    CATAPULT(150, null, 0, 300, SiegeType.CATAPULT),
    TREBUCHET(150, null, 0, 500, SiegeType.TREBUCHET),
    //Camp equipment
    BARRACK(0, ResourceType.ROCK,15,500, CampType.BARRACK),
    SIEGE_TENT(0 ,null, 0, 100, CampType.SIEGE_TENT),
    ENGINEER_GUILD(100, ResourceType.WOOD, 10, 500, CampType.ENGINEER_GUILD),
    MERCENARY_POST(0, ResourceType.WOOD, 10, 500, CampType.MERCENARY_POST),
    //Stock
    STOCKPILE(0 , null, 0, 300, StockType.STOCKPILE),
    FOOD_STOCKPILE(0, ResourceType.WOOD, 5, 300, StockType.FOOD_STOCKPILE),
    ARMOURY(0, ResourceType.WOOD,5,300, StockType.ARMOURY),
    //Producer
    ARMOURER(100, ResourceType.WOOD, 20, 350, ProducerType.ARMOURER),
    BLACKSMITH(100, ResourceType.WOOD, 20, 350, ProducerType.BLACKSMITH),
    FLETCHER(100, ResourceType.WOOD, 20, 350, ProducerType.FLETCHER),
    POLE_TURNER(100, ResourceType.WOOD, 10, 300, ProducerType.POLE_TURNER),
    OIL_SMELTER(100, ResourceType.WOOD, 10, 300, ProducerType.OIL_SMELTER),
    BAKERY(0, ResourceType.WOOD, 10, 250, ProducerType.BAKERY),
    DAIRY_PRODUCTS(0, ResourceType.WOOD, 10, 250, ProducerType.DAIRY_PRODUCTS),
    BREWERY(0, ResourceType.WOOD, 10, 250, ProducerType.BREWERY);
    private final Integer GOLD;
    private final ResourceType RESOURCES;
    private final Integer RESOURCE_NUMBER;
    private final Integer HP_IN_FIRST;
    public final Enum<?> specificConstant;

    BuildingType(Integer GOLD, ResourceType RESOURCES, Integer RESOURCE_NUMBER, Integer HP_IN_FIRST, Enum<?> specificConstant) {
        this.GOLD = GOLD;
        this.RESOURCES = RESOURCES;
        this.RESOURCE_NUMBER = RESOURCE_NUMBER;
        this.HP_IN_FIRST = HP_IN_FIRST;
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
}
