package model;

public enum ResourceType {
    WOOD(2, null, 0),
    ROCK(3, null, 0),
    IRON(5, null, 0),
    WHEAT(1, null, 0),
    OIL(2, null, 0),
    COW(4, null, 0),
    BARLEY(1, null, 0),
    FLOUR(4, ResourceType.WHEAT, 10),
    LEATHER(3, ResourceType.COW, 1),
    HOP(5, ResourceType.BARLEY, 20);
    private final int price;
    private final ResourceType baseSource;
    private final  Integer resourceAmount;

    ResourceType(int price, ResourceType baseSource, Integer resourceAmount) {
        this.price = price;
        this.baseSource = baseSource;
        this.resourceAmount = resourceAmount;
    }

    public int getPrice() {
        return price;
    }

    public ResourceType getBaseSource() {
        return baseSource;
    }

    public Integer getResourceAmount() {
        return resourceAmount;
    }
}
