package model.building;

import model.Kingdom;
import model.MapBlock;

public class Building {
    protected Integer hp;
    private MapBlock position;
    private final BuildingType buildingType;
    private final Kingdom owner;
    public Building(MapBlock position, BuildingType buildingType, Kingdom owner) {
        this.position = position;
        this.buildingType = buildingType;
        this.hp = buildingType.getHP_IN_FIRST();
        this.owner = owner;
    }

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

    public void decreaseHP(int amount){
        hp -= amount;
    }
}
