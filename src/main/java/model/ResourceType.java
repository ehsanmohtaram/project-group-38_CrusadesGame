package model;

import javafx.scene.image.Image;

public enum ResourceType {
    WOOD(2, null, 0, new Image(ResourceType.class.getResource("/images/resource/WOOD.png").toExternalForm())),
    ROCK(3, null, 0, new Image(ResourceType.class.getResource("/images/resource/ROCK.png").toExternalForm())),
    IRON(5, null, 0, new Image(ResourceType.class.getResource("/images/resource/IRON.png").toExternalForm())),
    WHEAT(1, null, 0, new Image(ResourceType.class.getResource("/images/resource/WHEAT.png").toExternalForm())),
    OIL(2, null, 0, new Image(ResourceType.class.getResource("/images/resource/OIL.png").toExternalForm())),
    RIG(2, null, 0, new Image(ResourceType.class.getResource("/images/resource/WOOD.png").toExternalForm())),
    COW(4, null, 0, new Image(ResourceType.class.getResource("/images/resource/COW.png").toExternalForm())),
    BARLEY(1, null, 0, new Image(ResourceType.class.getResource("/images/resource/BARLEY.png").toExternalForm())),
    FLOUR(4, ResourceType.WHEAT, 10, new Image(ResourceType.class.getResource("/images/resource/FLOUR.png").toExternalForm())),
    HOP(5, ResourceType.BARLEY, 20, new Image(ResourceType.class.getResource("/images/resource/HOP.png").toExternalForm()));
    private final int price;
    private final ResourceType baseSource;
    private final  Integer resourceAmount;
    private final Image texture;

    ResourceType(int price, ResourceType baseSource, Integer resourceAmount, Image texture) {
        this.price = price;
        this.baseSource = baseSource;
        this.resourceAmount = resourceAmount;
        this.texture = texture;
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

    public Image getTexture() {
        return texture;
    }
}
