package model.building;

import model.Kingdom;
import model.MapBlock;

public class Trap {
    private Kingdom owner;
    private MapBlock locationBlock;
    private TrapType trapType;

    public Trap(Kingdom owner, MapBlock locationBlock, TrapType trapType) {
        this.owner = owner;
        this.locationBlock = locationBlock;
        this.trapType = trapType;
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



}
