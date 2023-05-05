package model.building;

import model.Food;
import model.ResourceType;
import model.Weapons;

public enum ProducerType {
    ARMOURER(Weapons.METAL_ARMOUR),
    BLACKSMITH(ResourceType.IRON),
    FLETCHER(Weapons.BOW),
    POLE_TURNER(Weapons.SPEAR),
    OIL_SMELTER(ResourceType.OIL),
    BAKERY(Food.BREAD),
    DAIRY_PRODUCTS(Food.CHEESE),
    DAIRY_PRODUCTS_A(Weapons.LEATHER_ARMOR),
    BREWERY(ResourceType.WHEAT);
    private final Enum<?> typeOfResource;

    ProducerType(Enum<?> typeOfResource) {
        this.typeOfResource = typeOfResource;
    }

    public Enum<?> getTypeOfResource() {
        return typeOfResource;
    }
}
