package model;

import javafx.scene.image.Image;

public enum Food {
    BREAD(4, ResourceType.FLOUR, 3, new Image(Food.class.getResource("/images/food/BREAD.png").toExternalForm())),
    MEAT(8,null, 0, new Image(Food.class.getResource("/images/food/MEAT.png").toExternalForm())),
    APPLE(3, null, 0, new Image(Food.class.getResource("/images/food/APPLE.png").toExternalForm())),
    CHEESE(4, ResourceType.COW, 1, new Image(Food.class.getResource("/images/food/CHEESE.png").toExternalForm()));

    private final Integer price;
    private final ResourceType resourceType;
    private final Integer resourceAmount;
    private final Image texture;

    Food(Integer price, ResourceType resourceType, Integer resourceAmount, Image texture) {
        this.price = price;
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
        this.texture = texture;
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

    public Image getTexture() {
        return texture;
    }
}
