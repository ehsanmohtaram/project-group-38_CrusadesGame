package model.building;

public enum CampType {
    BARRACK(0,0),
    SIEGE_TENT(0,0);
    private final Integer COST_OF_LADDER_MAN;
    private final Integer COST_OF_ENGINEER;

    CampType(Integer COST_OF_LADDER_MAN, Integer COST_OF_ENGINEER) {
        this.COST_OF_LADDER_MAN = COST_OF_LADDER_MAN;
        this.COST_OF_ENGINEER = COST_OF_ENGINEER;
    }

    public Integer getCOST_OF_LADDER_MAN() {
        return COST_OF_LADDER_MAN;
    }

    public Integer getCOST_OF_ENGINEER() {
        return COST_OF_ENGINEER;
    }
}
