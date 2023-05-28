package model.unit;

import model.Weapons;

public enum UnitType {
    ARCHER(20, 5, 4, 5, 7,0, Weapons.BOW, null, true),
    CROSSBOWMEN(30, 8, 2, 3, 7,0, Weapons.CROSSBOW, Weapons.LEATHER_ARMOUR, true),
    SPEAR_MAN(20, 8, 3, 1, 5,0,Weapons.SPEAR,null, false),
    PIKE_MAN(40, 8, 2, 2, 10, 0, Weapons.SPEAR, Weapons.METAL_ARMOUR, false),
    MACE_MAN(30,12,3, 1, 13,0,Weapons.MACE, Weapons.LEATHER_ARMOUR, false),
    SWORDSMEN(30, 12, 1,1,8,0, Weapons.SWORDS, Weapons.METAL_ARMOUR, false),
    KNIGHT(40, 12, 5,2,15,0, Weapons.SWORDS, Weapons.METAL_ARMOUR, false),
    TUNNELER(15, 8, 4 , 1 ,5, -1, null, null, false),
    LADDER_MAN(10, 0, 4,1,4, -1,Weapons.LADDER,null, false),
    ENGINEER(10 , 15 , 3, 1,4, -1, null, null, null),
    WORKER(10 , 0 ,3 , 0, 4, -1 , null, null, null),
    BLACK_MONK(30, 10, 2, 1 ,8, -2, Weapons.MACE, null, false),
    MONK(30, 0 , 5, 0 , 5, -3,null, null, false),
    ARCHER_BOW(20, 10, 4, 5 ,7, 1, null ,null, true),
    SLAVES(5, 3, 4, 1 ,5, 1, null ,null, false),
    SLINGERS(10, 5, 4, 3 ,5, 1, null ,null, true),
    ASSASSINS(30, 10, 3, 1 ,9, 1, null ,null, false),
    HORSE_ARCHERS(30, 10, 5, 5 , 12, 1, null ,null, true),
    ARABIAN_SWORDSMAN(40, 12, 5, 1 ,16, 1, null ,null, false),
    FIRE_THROWERS(20, 15, 5, 4 , 9,1, null ,null, true),
    MANGONEL(100, 15, 0, 3, 0,-4, null, null, true),
    BALLISTA(100, 20, 0, 5, 0, -4,null, null, true),
    CATAPULT(300, 20, 3, 3 , 0,-4,null, null, true),
    TREBUCHET(500, 20, 0, 4, 0,-4, null, null, true),
    SIEGE_TOWER(500, 0, 2, 4, 0, -4, null, null, false),
    BATTERING_RAM(500, 15, 2, 1, 0, -4,null, null,true),
    MANTLET(30, 0, 5, 0, 0, -4, null, null, false),
    FIRE_BALLISTA(400, 15, 3, 5, 0, -4,null, null, true);
    private final Integer HP_IN_START;
    private final Integer DAMAGE;
    private final Integer VELOCITY;
    private final Integer ATTACK_RANGE;
    private final Integer PRICE;
    private final Integer IS_ARAB;
    private final Weapons WEAPON_NEEDED;
    private final Weapons Armour_Needed;
    private final Boolean CAN_DO_AIR_ATTACK;

    UnitType(Integer HP_IN_START, Integer ATTACK_RATE, Integer VELOCITY, Integer ATTACK_RANGE, Integer PRICE, Integer IS_ARAB, Weapons WEAPON_NEEDED, Weapons armourNeeded, Boolean canDoAirAttack) {
        this.HP_IN_START = HP_IN_START;
        this.DAMAGE = ATTACK_RATE;
        this.VELOCITY = VELOCITY;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.PRICE = PRICE;
        this.IS_ARAB = IS_ARAB;
        this.WEAPON_NEEDED = WEAPON_NEEDED;
        Armour_Needed = armourNeeded;
        this.CAN_DO_AIR_ATTACK = canDoAirAttack;
    }

    public Integer getHP_IN_START() {
        return HP_IN_START;
    }

    public Integer getDAMAGE() {
        return DAMAGE;
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

    public Boolean getCAN_DO_AIR_ATTACK() {
        return CAN_DO_AIR_ATTACK;
    }
}
