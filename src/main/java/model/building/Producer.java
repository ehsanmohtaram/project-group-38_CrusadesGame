package model.building;

import model.*;

public class Producer extends Building {
    private ProduceMode mode;

    public Producer(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
        mode = ProduceMode.NON_ACTIVE;
    }

    public ProduceMode getMode() {
        return mode;
    }

    public void setMode(ProduceMode mode) {
        this.mode = mode;
    }

}
