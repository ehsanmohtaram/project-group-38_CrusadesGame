package model;

public enum Food {
    BREAD(4, ResourceType.FLOUR, 3),
    MEAT(8,null, 0),
    APPLE(3, null, 0),
    CHEESE(4, ResourceType.COW, 1);

    private final Integer price;
    private final ResourceType resourceType;
    private final Integer resourceAmount;

    Food(Integer price, ResourceType resourceType, Integer resourceAmount) {
        this.price = price;
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
    }

    public Integer getPrice() {
        return price;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Integer getResourceAmount() {
        return resourceAmount;
    }
}
