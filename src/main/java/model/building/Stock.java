package model.building;

import model.*;

import java.util.*;

public class Stock extends Building{
    private HashMap<Enum<?>, Integer> resourceValues;
    public Stock(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
        resourceValues = new HashMap<>();
        initializeHashMapData();
    }

    public void initializeHashMapData() {
        if (getSpecificConstant().equals(BuildingType.ARMOURY))
            for (Weapons weapons : Weapons.values()) resourceValues.put(weapons, 0);
        if (getSpecificConstant().equals(BuildingType.FOOD_STOCKPILE))
            for (Food food : Food.values()) resourceValues.put(food, 0);
        if (getSpecificConstant().equals(BuildingType.STOCKPILE))
            for (ResourceType resourceType : ResourceType.values()) resourceValues.put(resourceType, 0);
    }

    private void addResourceToStock(Enum<?> resourceType, Integer value) {
        resourceValues.put(resourceType, resourceValues.get(resourceType) + value);
    }

    public HashMap<Enum<?>, Integer> getResourceValues() {
        return resourceValues;
    }
}
