package model.building;

import model.MapBlock;

public class Stock extends Building{
    public Stock(MapBlock position, BuildingType buildingType) {
        super(position, buildingType);
    }

    public Integer getCAPACITY() {
        StockType stockType = (StockType) getBuildingType().specificConstant;
        return stockType.CAPACITY;
    }
}
