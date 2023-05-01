package model.building;

import model.Kingdom;
import model.MapBlock;

public class Building {
    protected Integer hp;
    private MapBlock position;
    private BuildingType buildingType;
    private Kingdom owner;
    public Building(MapBlock position, BuildingType buildingType, Kingdom owner) {
        this.position = position;
        this.buildingType = buildingType;
        this.hp = buildingType.getHP_IN_FIRST();
        this.owner = owner;
    }

    //TODO category var may be needed

    public Enum<?> getSpecificConstant() {
        return buildingType.specificConstant;
    }

    public Integer getHp() {
        return hp;
    }

    public Kingdom getOwner() {
        return owner;
    }

    public void damage(Integer amount) {
        this.hp = hp - amount;
    }

    public MapBlock getPosition() {
        return position;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setPosition(MapBlock position) {
        this.position = position;
    }
}
