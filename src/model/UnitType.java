package model;

public enum UnitType {
    ;
    private final Integer HP_IN_START;
    private final Integer ATTACK_RATE;
    private final Integer DEFENCE_RATE;
    private final Integer VELOCITY;
    private final Integer ATTACK_RANGE;
    private final Boolean IS_ARAB;

    UnitType(Integer HP_IN_START, Integer ATTACK_RATE, Integer DEFENCE_RATE, Integer VELOCITY, Integer ATTACK_RANGE, Boolean IS_ARAB) {
        this.HP_IN_START = HP_IN_START;
        this.ATTACK_RATE = ATTACK_RATE;
        this.DEFENCE_RATE = DEFENCE_RATE;
        this.VELOCITY = VELOCITY;
        this.ATTACK_RANGE = ATTACK_RANGE;
        this.IS_ARAB = IS_ARAB;
    }
}
