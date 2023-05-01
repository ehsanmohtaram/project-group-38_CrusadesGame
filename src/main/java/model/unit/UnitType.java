package model.unit;

import model.Weapons;

public enum UnitType {
    ARCHER(20, 5, 4, 5, 7,false, Weapons.BOW, null),
    CROSSBOWMEN(30, 5, 2, 3, 7,false, Weapons.CROSSBOW, Weapons.LEATHER_ARMOR),
    SPEARMEN(10, 10, 3, 1, 5,false,Weapons.SPEAR,null),
    PIKEMEN(40, 10, 2, 2, 10,false, Weapons.SPEAR, Weapons.METAL_ARMOUR),
    MACEMEN(30,15,3, 1, 13,false,Weapons.MACE, Weapons.LEATHER_ARMOR),
    SWORDSMEN(10, 20, 1,1,8,false, Weapons.SWORDS, Weapons.METAL_ARMOUR),
    KNIGHT(40, 20, 5,2,15,false, Weapons.SWORDS, Weapons.METAL_ARMOUR),
    TUNNELER(10, 10, 4 , 1 ,5, null, null, null),
    LADDERMEN(10, 0, 4,1,4, null,Weapons.LADDER,null),
    ENGINEER(10 , 0 , 3, 1,4, null, null, null),
    BLACKMONK(30, 10, 2, 1 ,8, null, Weapons.MACE, null),
    ARCHERBOW(20, 5, 4, 5 ,7, true),
    SALVES(5, 3, 4, 1 ,5, true),
    SLINGERS(10, 5, 4, 3 ,5, true),
    ASSASSINS(30, 10, 3, 1 ,9, true),
    HORSEARCHERS(30, 10, 5, 9 , 12, true),
    ARABIANSWORDSMEN(40, 15, 5, 15 ,16, true),
    FIRETHROWERS(20, 15, 5, 4 , 9,true);
    private final Integer HP_IN_START;
    private final Integer ATTACK_RATE;
    private final Integer VELOCITY;
    private final Integer ATTACK_RANGE;
    private final Integer PRICE;
    private final boolean IS_ARAB;
    private Weapons WEAPEN_NEEDED;
    private Weapons Armour_Nedded;

    UnitType(Integer HP_IN_START, Integer ATTACK_RATE, Integer VELOCITY, Integer ATTACK_RANGE,Integer PRICE, Boolean IS_ARAB) {
        this.HP_IN_START = HP_IN_START;
        this.ATTACK_RATE = ATTACK_RATE;
        this.VELOCITY = VELOCITY;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.PRICE = PRICE;
        this.IS_ARAB = IS_ARAB;
    }

    UnitType(Integer HP_IN_START, Integer ATTACK_RATE, Integer VELOCITY, Integer ATTACK_RANGE, Integer PRICE, Boolean IS_ARAB, Weapons WEAPEN_NEEDED, Weapons armourNedded) {
        this.HP_IN_START = HP_IN_START;
        this.ATTACK_RATE = ATTACK_RATE;
        this.VELOCITY = VELOCITY;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.PRICE = PRICE;
        this.IS_ARAB = IS_ARAB;
        this.WEAPEN_NEEDED = WEAPEN_NEEDED;
        Armour_Nedded = armourNedded;
    }

    public Integer getHP_IN_START() {
        return HP_IN_START;
    }

    public Integer getATTACK_RATE() {
        return ATTACK_RATE;
    }

    public Integer getVELOCITY() {
        return VELOCITY;
    }

    public Integer getATTACK_RANGE() {
        return ATTACK_RANGE;
    }

    public Boolean getIS_ARAB() {
        return IS_ARAB;
    }
}
