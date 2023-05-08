package model;

public enum Weapons {
    BOW(ResourceType.WOOD,2, 14),
    CROSSBOW(ResourceType.WOOD,3, 16),
    SPEAR(ResourceType.WOOD,1, 15),
    PIKE(ResourceType.WOOD,1, 18),
    MACE(ResourceType.IRON,1, 18),
    SWORDS(ResourceType.IRON,1, 20),
    LADDER(ResourceType.WOOD, 1, 14),
    LEATHER_ARMOUR(ResourceType.LEATHER, 1, 15),
    METAL_ARMOUR(ResourceType.IRON,1, 18);

    private final ResourceType resourceType;
    private final Integer resourceAmount;
    private final Integer cost;

    Weapons(ResourceType resourceType, Integer resourceAmount, Integer cost) {
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
        this.cost = cost;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Integer getResourceAmount() {
        return resourceAmount;
    }

    public Integer getCost() {
        return cost;
    }
}
