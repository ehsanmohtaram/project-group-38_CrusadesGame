package model.unit;

import model.Weapons;

public enum UnitType {
    ARCHER(20, 5, 4, 5, 7,0, Weapons.BOW, null),
    CROSSBOWMEN(30, 5, 2, 3, 7,0, Weapons.CROSSBOW, Weapons.LEATHER_ARMOR),
    SPEAR_MAN(10, 10, 3, 1, 5,0,Weapons.SPEAR,null),
    PIKE_MAN(40, 10, 2, 2, 10, 0, Weapons.SPEAR, Weapons.METAL_ARMOUR),
    MACE_MAN(30,15,3, 1, 13,0,Weapons.MACE, Weapons.LEATHER_ARMOR),
    SWORDSMEN(10, 20, 1,1,8,0, Weapons.SWORDS, Weapons.METAL_ARMOUR),
    KNIGHT(40, 20, 5,2,15,0, Weapons.SWORDS, Weapons.METAL_ARMOUR),
    TUNNELER(10, 10, 4 , 1 ,5, -1, null, null),
    LADDER_MAN(10, 0, 4,1,4, -1,Weapons.LADDER,null),
    ENGINEER(10 , 0 , 3, 1,4, -1, null, null),
    BLACK_MONK(30, 10, 2, 1 ,8, -1, Weapons.MACE, null),
    ARCHER_BOW(20, 5, 4, 5 ,7, 1, null ,null),
    SALVES(5, 3, 4, 1 ,5, 1, null ,null),
    SLINGERS(10, 5, 4, 3 ,5, 1, null ,null),
    ASSASSINS(30, 10, 3, 1 ,9, 1, null ,null),
    HORSE_ARCHERS(30, 10, 5, 9 , 12, 1, null ,null),
    ARABIAN_SWORDSMAN(40, 15, 5, 15 ,16, 1, null ,null),
    FIRE_THROWERS(20, 15, 5, 4 , 9,1, null ,null);
    private final Integer HP_IN_START;
    private final Integer ATTACK_RATE;
    private final Integer VELOCITY;
    private final Integer ATTACK_RANGE;
    private final Integer PRICE;
    private final Integer IS_ARAB;
    private final Weapons WEAPON_NEEDED;
    private final Weapons Armour_Needed;


    UnitType(Integer HP_IN_START, Integer ATTACK_RATE, Integer VELOCITY, Integer ATTACK_RANGE, Integer PRICE, Integer IS_ARAB, Weapons WEAPON_NEEDED, Weapons armourNeeded) {
        this.HP_IN_START = HP_IN_START;
        this.ATTACK_RATE = ATTACK_RATE;
        this.VELOCITY = VELOCITY;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.PRICE = PRICE;
        this.IS_ARAB = IS_ARAB;
        this.WEAPON_NEEDED = WEAPON_NEEDED;
        Armour_Needed = armourNeeded;
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

    public Integer getIS_ARAB() {
        return IS_ARAB;
    }

    public Integer getPRICE() {
        return PRICE;
    }

    public Weapons getWEAPON_NEEDED() {
        return WEAPON_NEEDED;
    }

    public Weapons getArmour_Needed() {
        return Armour_Needed;
    }
}
