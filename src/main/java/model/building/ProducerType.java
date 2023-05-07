package model.building;

import model.Food;
import model.ResourceType;
import model.Weapons;

public enum ProducerType {
    ARMOURER(Weapons.METAL_ARMOUR, null, 8, false),
    BLACKSMITH(Weapons.PIKE, Weapons.SWORDS, 2, false),
    FLETCHER(Weapons.BOW, Weapons.CROSSBOW, 6, false),
    POLE_TURNER(Weapons.SPEAR, null, 4, false),
    OIL_SMELTER(ResourceType.OIL, null, 14, false),
    BAKERY(Food.BREAD, null, 20, false),
    DAIRY_PRODUCTS(Food.CHEESE, Weapons.LEATHER_ARMOUR, 18, false),
    BREWERY(ResourceType.HOP, null, 18, false),
    MILL(ResourceType.FLOUR, null, 25, false),
    Hunting_ground(Food.MEAT, null, 12 , false),
    WHEAT_FARM(ResourceType.WHEAT, null, 40, true),
    BARLEY_FARM(ResourceType.BARLEY, null, 35, true),
    APPLE_GARDEN(Food.APPLE, null, 25, true);

    private final Enum<?> typeOfResource0;
    private final Enum<?> typeOfResource1;
    private final Integer produceRate;
    private final Boolean isFarm;

    ProducerType(Enum<?> typeOfResource0, Enum<?> typeOfResource1, Integer produceRate, Boolean isFarm) {
        this.typeOfResource0 = typeOfResource0;
        this.typeOfResource1 = typeOfResource1;
        this.produceRate = produceRate;
        this.isFarm = isFarm;
    }

    public Enum<?> getTypeOfResource0() {
        return typeOfResource0;
    }

    public Enum<?> getTypeOfResource1() {
        return typeOfResource1;
    }

    public Integer getProduceRate() {
        return produceRate;
    }

    public Boolean getFarm() {
        return isFarm;
    }
}
