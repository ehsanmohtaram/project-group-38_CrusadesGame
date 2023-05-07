package model.building;

import model.*;

import java.util.*;

public class Stock extends Building{
    private HashMap<Enum<?>, Integer> resourceValues = new HashMap<>();
    public Stock(MapBlock position, BuildingType buildingType, Kingdom owner) {
        super(position, buildingType, owner);
        if (buildingType.specificConstant.name().equals(BuildingType.ARMOURY.name()))
            for (Weapons weapons : Weapons.values()) resourceValues.put(weapons, 0);
        if (buildingType.specificConstant.name().equals(BuildingType.FOOD_STOCKPILE.name()))
            for (Food food : Food.values()) resourceValues.put(food, 0);
        if (buildingType.specificConstant.name().equals(BuildingType.STOCKPILE.name()))
            for (ResourceType resourceType : ResourceType.values()) resourceValues.put(resourceType, 100);
    }

    public void addResourceToStock(Enum<?> resourceType, Integer value) {
        resourceValues.put(resourceType, value);
    }

    public HashMap<Enum<?>, Integer> getResourceValues() {
        return resourceValues;
    }
}
