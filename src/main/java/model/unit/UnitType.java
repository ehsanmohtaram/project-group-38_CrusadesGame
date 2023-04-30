package model.unit;

import model.Flags;

public enum UnitType {
    ARCHER(20, 5, 4, 5, 7,false),
    CROSSBOWMEN(30, 5, 2, 3, 7,false),
    SPEARMEN(10, 10, 3, 1, 5,false),
    PIKEMEN(40, 10, 2, 2, 10,false),
    MACEMEN(30,15,3, 1, 13,false),
    SWORDSMEN(10, 20, 1,1,8,false),
    KNIGHT(40, 20, 5,2,15,false),
    TUNNELER(10, 10, 4 , 1 ,5, false),
    LADDERMEN(10, 0, 4,1,4, false),
    ENGINEER(10 , 0 , 3, 1,4, false),
    BLACKMONK(30, 10, 2, 1 ,8, false),
    ARCHERBOW(20, 5, 4, 5 ,7, true),
    SALVES(5, 3, 4, 1 ,5, true),
    SLINGERS(10, 5, 4, 3 ,5, true),
    ASSASSINS(30, 10, 3, 1 ,9, true),
    HORSEARCHERS(30, 10, 5, 9 , 12, true),
    ARABIANSWORDSMEN(40, 15, 5, 15 ,16, true),
    FIRETHROWERS(20, 15, 5, 4 , 9,true);;
    private final Integer HP_IN_START;
    private final Integer ATTACK_RATE;
    private final Integer VELOCITY;
    private final Integer ATTACK_RANGE;
    private final Integer PRICE;
    private final boolean IS_ARAB;

    UnitType(Integer HP_IN_START, Integer ATTACK_RATE, Integer VELOCITY, Integer ATTACK_RANGE,Integer PRICE, Boolean IS_ARAB) {
        this.HP_IN_START = HP_IN_START;
        this.ATTACK_RATE = ATTACK_RATE;
        this.VELOCITY = VELOCITY;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.PRICE = PRICE;
        this.IS_ARAB = IS_ARAB;
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
