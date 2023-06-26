package model;

import javafx.scene.image.Image;

public enum Weapons {
    BOW(ResourceType.WOOD,2, 14, new Image(Weapons.class.getResource("/images/weapon/BOW.png").toExternalForm())),
    CROSSBOW(ResourceType.WOOD,3, 16, new Image(Weapons.class.getResource("/images/weapon/CROSSBOW.png").toExternalForm())),
    SPEAR(ResourceType.WOOD,1, 15, new Image(Weapons.class.getResource("/images/weapon/SPEAR.png").toExternalForm())),
    PIKE(ResourceType.WOOD,1, 18, new Image(Weapons.class.getResource("/images/weapon/PIKE.png").toExternalForm())),
    MACE(ResourceType.IRON,1, 18, new Image(Weapons.class.getResource("/images/weapon/MACE.png").toExternalForm())),
    SWORDS(ResourceType.IRON,1, 20, new Image(Weapons.class.getResource("/images/weapon/SWORDS.png").toExternalForm())),
    LADDER(ResourceType.WOOD, 1, 14, new Image(Weapons.class.getResource("/images/weapon/BOW.png").toExternalForm())),
    LEATHER_ARMOUR(ResourceType.COW, 1, 15, new Image(Weapons.class.getResource("/images/weapon/LEATHER_ARMOUR.png").toExternalForm())),
    METAL_ARMOUR(ResourceType.IRON,1, 18, new Image(Weapons.class.getResource("/images/weapon/METAL_ARMOUR.png").toExternalForm()));

    private final ResourceType resourceType;
    private final Integer resourceAmount;
    private final Integer cost;
    private Image texture;

    Weapons(ResourceType resourceType, Integer resourceAmount, Integer cost, Image texture) {
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
        this.cost = cost;
        this.texture = texture;
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

    public Image getTexture() {
        return texture;
    }
}
