package model.building;

import model.Kingdom;
import model.MapBlock;

public class Trap {
    private Kingdom owner;
    private MapBlock locationBlock;
    private TrapType trapType;
    private boolean isActive;

    public Trap(Kingdom owner, MapBlock locationBlock, TrapType trapType) {
        this.owner = owner;
        this.locationBlock = locationBlock;
        this.trapType = trapType;
        locationBlock.setTrap(this);
        isActive = true;
        if(trapType.equals(TrapType.BITUMEN_TRENCH))
            isActive = false;
    }

    public Kingdom getOwner() {
        return owner;
    }

    public MapBlock getLocationBlock() {
        return locationBlock;
    }

    public TrapType getTrapType() {
        return trapType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
