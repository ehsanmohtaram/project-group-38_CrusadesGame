package model.building;

import model.ResourceType;

public enum GeneralBuildingType {
    FLETCHER(1,15, ResourceType.WOOD,10);
    private final Integer NUMBER_OF_WORKER;
    private final Integer RATE;
    private final ResourceType RESOURCE_Type_NEEDED;
    private final Integer AMOUNT_OF_RESOURCE;

    GeneralBuildingType(Integer NUMBER_OF_WORKER, Integer RATE, ResourceType RESOURCE_Type_NEEDED, Integer AMOUNT_OF_RESOURCE) {
        this.NUMBER_OF_WORKER = NUMBER_OF_WORKER;
        this.RATE = RATE;
        this.RESOURCE_Type_NEEDED = RESOURCE_Type_NEEDED;
        this.AMOUNT_OF_RESOURCE = AMOUNT_OF_RESOURCE;
    }

    public Integer getNUMBER_OF_WORKER() {
        return NUMBER_OF_WORKER;
    }

    public Integer getRATE() {
        return RATE;
    }

    public ResourceType getRESOURCE_Type_NEEDED() {
        return RESOURCE_Type_NEEDED;
    }

    public Integer getAMOUNT_OF_RESOURCE() {
        return AMOUNT_OF_RESOURCE;
    }
}
