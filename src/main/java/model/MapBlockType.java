package model;


public enum MapBlockType {
    EARTH(true , true , true),
    GRAVEL(true , true , true),
    SLATE(true , false , false),
    ROCK(false , false , false),
    IRON(true , false , false),
    GRASSLAND(true , true , true),
    MEADOW(true , true , true),
    DENSE_MEADOW(true , true , true),
    SEA(false , false , false),
    BEACH(true , false , false),
    SMALL_POND(false , false , false),
    BIG_POUND(false , false , false),
    RIVER(false , false , false),
    SHALLOW_WATER(true , false , false),
    PLAIN(true , false , false),
    OIL(true , false , false);

    private boolean isAccessible;
    private boolean isBuildable;
    private boolean isCultivable;

    MapBlockType(boolean isAccessible, boolean isBuildable, boolean isCultivable) {
        this.isAccessible = isAccessible;
        this.isBuildable = isBuildable;
        this.isCultivable = isCultivable;
    }
}
