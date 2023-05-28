package model.building;

import model.Kingdom;
import model.MapBlock;

public class Mine extends Building{
    private ProduceMode mineMode;
    public Mine(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
        mineMode = ProduceMode.ACTIVE;
    }

    public void setMineMode(ProduceMode mineMode) {
        this.mineMode = mineMode;
    }

    public ProduceMode getMineMode() {
        return mineMode;
    }
}
