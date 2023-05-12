package model.building;

import model.Food;
import model.ResourceType;
import model.Weapons;

public enum ProducerType {
    ARMOURER(Weapons.METAL_ARMOUR, null, 4, false),
    BLACKSMITH(Weapons.PIKE, Weapons.SWORDS, 2, false),
    FLETCHER(Weapons.BOW, Weapons.CROSSBOW, 6, false),
    POLE_TURNER(Weapons.SPEAR, Weapons.MACE, 4, false),
    OIL_SMELTER(ResourceType.OIL, null, 8, false),
    BAKERY(Food.BREAD, null, 8, false),
    DAIRY_PRODUCTS(Food.CHEESE, Weapons.LEATHER_ARMOUR, 8, false),
    BREWERY(ResourceType.HOP, null, 8, false),
    MILL(ResourceType.FLOUR, null, 10, false),
    Hunting_ground(Food.MEAT, null, 6 , false),
    WHEAT_FARM(ResourceType.WHEAT, null, 12, true),
    BARLEY_FARM(ResourceType.BARLEY, null, 12, true),
    APPLE_GARDEN(Food.APPLE, null, 10, true);

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
