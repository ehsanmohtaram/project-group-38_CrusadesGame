package model.building;

import model.Food;
import model.ResourceType;
import model.Weapons;

public enum ProducerType {
    ARMOURER(Weapons.METAL_ARMOUR, null, 8),
    BLACKSMITH(Weapons.PIKE, Weapons.SWORDS, 2),
    FLETCHER(Weapons.BOW, Weapons.CROSSBOW, 6),
    POLE_TURNER(Weapons.SPEAR, null, 4),
    OIL_SMELTER(ResourceType.OIL, null, 14),
    BAKERY(Food.BREAD, null, 20),
    DAIRY_PRODUCTS(Food.CHEESE, Weapons.LEATHER_ARMOUR, 18),
    BREWERY(ResourceType.HOP, null, 18);

    private final Enum<?> typeOfResource0;
    private final Enum<?> typeOfResource1;
    private final Integer produceRate;

    ProducerType(Enum<?> typeOfResource0, Enum<?> typeOfResource1, Integer produceRate) {
        this.typeOfResource0 = typeOfResource0;
        this.typeOfResource1 = typeOfResource1;
        this.produceRate = produceRate;
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
}
