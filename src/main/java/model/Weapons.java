package model;

public enum Weapons {
    BOW(ResourceType.WOOD,2),
    CROSSBOW(ResourceType.WOOD,3),
    SPEAR(ResourceType.WOOD,1),
    PIKE(ResourceType.WOOD,1),
    MACE(ResourceType.IRON,1),
    SWORDS(ResourceType.IRON,1),
    LADDER(ResourceType.WOOD, 1),
    LEATHER_ARMOR(ResourceType.LEATHER, 1),
    METAL_ARMOUR(ResourceType.IRON,1);

    private final ResourceType resourceType;
    private final Integer resourceAmount;

    Weapons(ResourceType resourceType, Integer resourceAmount) {
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Integer getResourceAmount() {
        return resourceAmount;
    }
}
