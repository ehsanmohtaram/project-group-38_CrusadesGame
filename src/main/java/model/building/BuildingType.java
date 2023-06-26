package model.building;
import javafx.scene.image.Image;
import model.ResourceType;
import model.unit.UnitType;


public enum BuildingType {
    INN(100, ResourceType.WOOD, 20, 300, UnitType.WORKER, 1, null, new Image(BuildingType.class.getResource("/images/building/INN.png").toExternalForm())),
    STAIRS(0, ResourceType.ROCK, 3, 10, UnitType.WORKER, 0, null, new Image(BuildingType.class.getResource("/images/building/head.png").toExternalForm())),
    //Defensive structure
    HEAD_QUARTER(0,null,0,4000, UnitType.WORKER,0,DefensiveStructureType.HEAD_QUARTER, new Image(BuildingType.class.getResource("/images/building/head.png").toExternalForm())),
    WALL(0,ResourceType.ROCK, 5,200, UnitType.WORKER,0 ,DefensiveStructureType.WALL, new Image(BuildingType.class.getResource("/images/building/head.png").toExternalForm())),
    BIG_WALL(0,ResourceType.ROCK, 10 ,400, UnitType.WORKER,0,DefensiveStructureType.BIG_WALL, new Image(BuildingType.class.getResource("/images/building/head.png").toExternalForm())),
    SMALL_STONE_GATEHOUSE(0,null,0,1000, UnitType.WORKER,0,DefensiveStructureType.SMALL_STONE_GATEHOUSE, new Image(BuildingType.class.getResource("/images/building/SMALL_STONE_GATEHOUSE.png").toExternalForm())),
    BIG_STONE_GATEHOUSE(0,ResourceType.ROCK,20,2000, UnitType.WORKER,0,DefensiveStructureType.BIG_STONE_GATEHOUSE, new Image(BuildingType.class.getResource("/images/building/BIG_STONE_GATEHOUSE.png").toExternalForm())),
    DRAWBRIDGE(0,ResourceType.WOOD,10,100, UnitType.WORKER,0,DefensiveStructureType.DRAWBRIDGE, new Image(BuildingType.class.getResource("/images/building/DRAWBRIDGE.png").toExternalForm())),
    LOOKOUT_TOWER(0, ResourceType.ROCK, 10, 2000, UnitType.WORKER,0,DefensiveStructureType.LOOKOUT_TOWER, new Image(BuildingType.class.getResource("/images/building/LOOKOUT_TOWER.png").toExternalForm())),
    PERIMETER_TOWER(0,ResourceType.ROCK,10,1000, UnitType.WORKER,0,DefensiveStructureType.PERIMETER_TOWER, new Image(BuildingType.class.getResource("/images/building/PERIMETER_TOWER.png").toExternalForm())),
    DEFENSE_TURRET(0,ResourceType.ROCK,15,1200, UnitType.WORKER,0,DefensiveStructureType.PERIMETER_TOWER, new Image(BuildingType.class.getResource("/images/building/DEFENSE_TURRET.png").toExternalForm())),
    SQUARE_TOWER(0, ResourceType.ROCK,35,1600, UnitType.WORKER,0,DefensiveStructureType.SQUARE_TOWER, new Image(BuildingType.class.getResource("/images/building/SQUARE_TOWER.png").toExternalForm())),
    ROUND_TOWER(0,ResourceType.ROCK,40,2000, UnitType.WORKER,0,DefensiveStructureType.ROUND_TOWER, new Image(BuildingType.class.getResource("/images/building/ROUND_TOWER.png").toExternalForm())),
    //Siege equipment
    MANGONEL(50, null, 0, 100, UnitType.ENGINEER,2, SiegeType.MANGONEL, new Image(BuildingType.class.getResource("/images/building/head.png").toExternalForm())),
    BALLISTA(50, null, 0, 100, UnitType.ENGINEER,2,SiegeType.BALLISTA, new Image(BuildingType.class.getResource("/images/building/head.png").toExternalForm())),
    CATAPULT(150, null, 0, 300, UnitType.ENGINEER,2,SiegeType.CATAPULT, new Image(BuildingType.class.getResource("/images/building/CATAPULT.png").toExternalForm())),
    TREBUCHET(150, null, 0, 500, UnitType.ENGINEER,3,SiegeType.TREBUCHET, new Image(BuildingType.class.getResource("/images/building/TREBUCHET.png").toExternalForm())),
    SIEGE_TOWER(200, null, 0, 500, UnitType.ENGINEER, 4, SiegeType.SIEGE_TOWER, new Image(BuildingType.class.getResource("/images/building/SIEGE_TOWER.png").toExternalForm())),
    BATTERING_RAM(180, null, 0, 500, UnitType.ENGINEER, 4, SiegeType.BATTERING_RAM, new Image(BuildingType.class.getResource("/images/building/BATTERING_RAM.png").toExternalForm())),
    MANTLET(6, null, 0, 100, UnitType.ENGINEER, 1, SiegeType.MANTLET, new Image(BuildingType.class.getResource("/images/building/head.png").toExternalForm())),
    FIRE_BALLISTA(150, null, 0, 400, UnitType.ENGINEER, 2, SiegeType.FIRE_BALLISTA, new Image(BuildingType.class.getResource("/images/building/FIRE_BALLISTA.png").toExternalForm())),
    //Camp equipment
    BARRACK(0, ResourceType.ROCK,15,500,  UnitType.WORKER,0,CampType.BARRACK, new Image(BuildingType.class.getResource("/images/building/BARRACK.png").toExternalForm())),
    SIEGE_TENT(0 ,null, 0, 100,  UnitType.WORKER,0,CampType.SIEGE_TENT, new Image(BuildingType.class.getResource("/images/building/SIEGE_TENT.png").toExternalForm())),
    ENGINEER_GUILD(100, ResourceType.WOOD, 10, 500,  UnitType.WORKER,0,CampType.ENGINEER_GUILD, new Image(BuildingType.class.getResource("/images/building/ENGINEER_GUILD.png").toExternalForm())),
    MERCENARY_POST(0, ResourceType.WOOD, 10, 500,  UnitType.WORKER,0,CampType.MERCENARY_POST, new Image(BuildingType.class.getResource("/images/building/MERCENARY_POST.png").toExternalForm())),
    CATHEDRAL(1000,null,0,500, UnitType.WORKER,0, CampType.CATHEDRAL, new Image(BuildingType.class.getResource("/images/building/CATHEDRAL.png").toExternalForm())),
    CHURCH(250,null,0,200, UnitType.WORKER,0, CampType.CHURCH, new Image(BuildingType.class.getResource("/images/building/CHURCH.png").toExternalForm())),
    STABLE(400, ResourceType.WOOD, 20, 300, UnitType.WORKER, 0, CampType.STABLE, new Image(BuildingType.class.getResource("/images/building/STABLE.png").toExternalForm())),
    //Stock
    STOCKPILE(0 , null, 0, 300, UnitType.WORKER,0,StockType.STOCKPILE, new Image(BuildingType.class.getResource("/images/building/stock.png").toExternalForm())),
    FOOD_STOCKPILE(0, ResourceType.WOOD, 5, 300, UnitType.WORKER,0,StockType.FOOD_STOCKPILE, new Image(BuildingType.class.getResource("/images/building/FOOD_STOCKPILE.png").toExternalForm())),
    ARMOURY(0, ResourceType.WOOD,5,300, UnitType.WORKER,0,StockType.ARMOURY, new Image(BuildingType.class.getResource("/images/building/ARMOURY.png").toExternalForm())),
    //Producer
    ARMOURER(100, ResourceType.WOOD, 20, 350, UnitType.WORKER,1,ProducerType.ARMOURER, new Image(BuildingType.class.getResource("/images/building/ARMOURER.png").toExternalForm())),
    BLACKSMITH(100, ResourceType.WOOD, 20, 350, UnitType.WORKER,1,ProducerType.BLACKSMITH, new Image(BuildingType.class.getResource("/images/building/BLACKSMITH.png").toExternalForm())),
    FLETCHER(100, ResourceType.WOOD, 20, 350, UnitType.WORKER,1,ProducerType.FLETCHER, new Image(BuildingType.class.getResource("/images/building/FLETCHER.png").toExternalForm())),
    POLE_TURNER(100, ResourceType.WOOD, 10, 300, UnitType.WORKER,1,ProducerType.POLE_TURNER, new Image(BuildingType.class.getResource("/images/building/POLE_TURNER.png").toExternalForm())),
    OIL_SMELTER(100, ResourceType.WOOD, 10, 300, UnitType.ENGINEER,1,ProducerType.OIL_SMELTER, new Image(BuildingType.class.getResource("/images/building/OIL_SMELTER.png").toExternalForm())),
    BAKERY(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.BAKERY, new Image(BuildingType.class.getResource("/images/building/BAKERY.png").toExternalForm())),
    DAIRY_PRODUCTS(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.DAIRY_PRODUCTS, new Image(BuildingType.class.getResource("/images/building/DAIRY_PRODUCTS.gif").toExternalForm())),
    BREWERY(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.BREWERY, new Image(BuildingType.class.getResource("/images/building/BREWERY.png").toExternalForm())),
    MILL(0, ResourceType.WOOD, 20, 300, UnitType.WORKER, 3, ProducerType.MILL, new Image(BuildingType.class.getResource("/images/building/MILL.png").toExternalForm())),
    Hunting_ground(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.Hunting_ground, new Image(BuildingType.class.getResource("/images/building/Hunting_ground.png").toExternalForm())),
    WHEAT_FARM(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.WHEAT_FARM, new Image(BuildingType.class.getResource("/images/building/WHEAT_FARM.png").toExternalForm())),
    APPLE_GARDEN(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.APPLE_GARDEN, new Image(BuildingType.class.getResource("/images/building/APPLE_GARDEN.png").toExternalForm())),
    BARLEY_FARM(0, ResourceType.WOOD, 10, 250, UnitType.WORKER,1,ProducerType.BARLEY_FARM, new Image(BuildingType.class.getResource("/images/building/BARLEY_FARM.png").toExternalForm())),
    //Mine
    QUARRY(0, ResourceType.WOOD, 20, 300, UnitType.WORKER, 3, MineType.QUARRY, new Image(BuildingType.class.getResource("/images/building/QUARRY.gif").toExternalForm())),
    IRON_MINE(0, ResourceType.WOOD, 20, 300, UnitType.WORKER, 2, MineType.IRON_MINE, new Image(BuildingType.class.getResource("/images/building/IRON_MINE.png").toExternalForm())),
    WOOD_CUTTER(0, ResourceType.WOOD, 3, 100, UnitType.WORKER, 1, MineType.WOOD_CUTTER, new Image(BuildingType.class.getResource("/images/building/WOOD_CUTTER.png").toExternalForm())),
    PITCH_RIG(0 , ResourceType.WOOD, 20, 150, UnitType.WORKER, 1, MineType.PITCH_RIG, new Image(BuildingType.class.getResource("/images/building/PITCH_RIG.png").toExternalForm())),
    OX_TETHER(0, ResourceType.WOOD, 5, 200, UnitType.WORKER, 1, MineType.OX_TETHER, new Image(BuildingType.class.getResource("/images/building/OX_TETHER.png").toExternalForm())),
    //Other
    SHOP(0, ResourceType.WOOD, 5, 300, UnitType.WORKER, 0, null, new Image(BuildingType.class.getResource("/images/building/SHOP.png").toExternalForm())),
    HOUSE(0, ResourceType.WOOD, 6, 200, UnitType.WORKER, 0, null, new Image(BuildingType.class.getResource("/images/building/HOUSE.gif").toExternalForm()));
    private final Integer GOLD;
    private final ResourceType RESOURCES;
    private final Integer RESOURCE_NUMBER;
    private final Integer HP_IN_FIRST;
    private final UnitType workerNeeded;
    private final Integer numberOfWorker;
    public final Enum<?> specificConstant;
    private final Image texture;

    BuildingType(Integer GOLD, ResourceType RESOURCES, Integer RESOURCE_NUMBER, Integer HP_IN_FIRST, UnitType workerNeeded, Integer numberOfWorker, Enum<?> specificConstant, Image texture) {
        this.GOLD = GOLD;
        this.RESOURCES = RESOURCES;
        this.RESOURCE_NUMBER = RESOURCE_NUMBER;
        this.HP_IN_FIRST = HP_IN_FIRST;
        this.workerNeeded = workerNeeded;
        this.numberOfWorker = numberOfWorker;
        this.specificConstant = specificConstant;
        this.texture = texture;
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

    public Image getTexture() {
        return texture;
    }
}
