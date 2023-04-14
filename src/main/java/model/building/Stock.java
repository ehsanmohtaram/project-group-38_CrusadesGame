package model.building;

import model.Land;

public class Stock extends Building{
    public Stock(Land position, BuildingType buildingType) {
        super(position, buildingType);
    }

    public Integer getCAPACITY() {
        StockType stockType = (StockType) getBuildingType().specificConstant;
        return stockType.CAPACITY;
    }
}
