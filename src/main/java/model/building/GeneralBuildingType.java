package model.building;

import model.ResourceType;

public enum GeneralBuildingType {
    FLETCHER(1,15, ResourceType.WOOD,10);
    public final Integer NUMBER_OF_WORKER;
    public final Integer RATE;
    public final ResourceType RESOURCE_Type_NEEDED;
    public final Integer AMOUNT_OF_RESOURCE;

    GeneralBuildingType(Integer NUMBER_OF_WORKER, Integer RATE, ResourceType RESOURCE_Type_NEEDED, Integer AMOUNT_OF_RESOURCE) {
        this.NUMBER_OF_WORKER = NUMBER_OF_WORKER;
        this.RATE = RATE;
        this.RESOURCE_Type_NEEDED = RESOURCE_Type_NEEDED;
        this.AMOUNT_OF_RESOURCE = AMOUNT_OF_RESOURCE;
    }

}
