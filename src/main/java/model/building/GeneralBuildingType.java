package model.building;

import model.Resource;

public enum GeneralBuildingType {
    FLETCHER(1,15,Resource.WOOD,10);
    private final Integer NUMBER_OF_WORKER;
    private final Integer RATE;
    private final Resource RESOURCE_NEEDED;
    private final Integer AMOUNT_OF_RESOURCE;

    GeneralBuildingType(Integer NUMBER_OF_WORKER, Integer RATE, Resource RESOURCE_NEEDED, Integer AMOUNT_OF_RESOURCE) {
        this.NUMBER_OF_WORKER = NUMBER_OF_WORKER;
        this.RATE = RATE;
        this.RESOURCE_NEEDED = RESOURCE_NEEDED;
        this.AMOUNT_OF_RESOURCE = AMOUNT_OF_RESOURCE;
    }
}
