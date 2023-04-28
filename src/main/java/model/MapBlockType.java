package model;


public enum MapBlockType {
    EARTH(true , true , true, "\u001B[48;5;137m"),
    GRAVEL(true , true , true, "\u001B[48;5;137m"),
    SLATE(true , false , false, "\u001B[48;5;102m"),
    ROCK(false , false , false, "\u001B[48;5;241m"),
    IRON(true , false , false, "\u001B[48;5;189m"),
    GRASSLAND(true , true , true, "\u001B[48;5;107m"),
    MEADOW(true , true , true, "\u001B[48;5;70m"),
    DENSE_MEADOW(true , true , true, "\u001B[48;5;82m" ),
    SEA(false , false , false, "\u001B[48;5;24m"),
    BEACH(true , false , false, "\u001B[48;5;186m"),
    SMALL_POND(false , false , false, "\u001B[48;5;37m"),
    BIG_POUND(false , false , false, "\u001B[48;5;37m"),
    RIVER(false , false , false,  "\u001B[48;5;31m"),
    SHALLOW_WATER(true , false , false , "\u001B[48;5;30m"),
    PLAIN(true , false , false,"\u001B[48;5;23m" ),
    OIL(true , false , false, "\u001B[48;5;16m");

    private boolean isAccessible;
    private boolean isBuildable;
    private boolean isCultivable;
    private String color;

    MapBlockType(boolean isAccessible, boolean isBuildable, boolean isCultivable, String color) {
        this.isAccessible = isAccessible;
        this.isBuildable = isBuildable;
        this.isCultivable = isCultivable;
        this.color = color;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public boolean isBuildable() {
        return isBuildable;
    }

    public boolean isCultivable() {
        return isCultivable;
    }

    public String getColor() {
        return color;
    }
}
