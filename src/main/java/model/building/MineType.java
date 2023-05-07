package model.building;

import model.MapBlockType;
import model.ResourceType;

public enum MineType {
    QUARRY(ResourceType.ROCK, MapBlockType.ROCK, 30),
    IRON_MINE(ResourceType.IRON, MapBlockType.IRON, 25),
    WOOD_CUTTER(ResourceType.WOOD, MapBlockType.GRASSLAND, 35),
    PITCH_RIG(ResourceType.RIG, MapBlockType.PLAIN, 10);
    private final ResourceType resourceType;
    private final MapBlockType mapBlockType;
    private final Integer produceRate;

    MineType(ResourceType resourceType, MapBlockType mapBlockType, Integer produceRate) {
        this.resourceType = resourceType;
        this.mapBlockType = mapBlockType;
        this.produceRate = produceRate;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public MapBlockType getMapBlockType() {
        return mapBlockType;
    }

    public Integer getProduceRate() {
        return produceRate;
    }
}
