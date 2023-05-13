package model;


public enum MapBlockType {
    EARTH(true , true , true, "\u001B[48;5;137m", true),
    GRAVEL(true , true , true, "\u001B[48;5;137m", true),
    SLATE(false , false , false, "\u001B[48;5;102m", false),
    ROCK(true, false , false, "\u001B[48;5;241m", false),
    IRON(true , false , false, "\u001B[48;5;189m", false),
    GRASSLAND(true , true , true, "\u001B[48;5;107m", true),
    MEADOW(true , true , true, "\u001B[48;5;70m", true),
    DENSE_MEADOW(true , true , true, "\u001B[48;5;82m", true),
    SEA(false , false , false, "\u001B[48;5;24m", false),
    BEACH(true , false , false, "\u001B[48;5;186m", false),
    SMALL_POND(false , false , false, "\u001B[48;5;37m", false),
    BIG_POUND(false , false , false, "\u001B[48;5;37m", false),
    RIVER(false , false , false,  "\u001B[48;5;31m", false),
    SHALLOW_WATER(true , false , false , "\u001B[48;5;30m", false),
    PLAIN(true , false , false,"\u001B[48;5;23m", false),
    HOLE(false , false , false,"\u001B[48;5;88m", false),
    OIL(true , false , false, "\u001B[48;5;16m", false);

    private boolean isAccessible;
    private boolean isBuildable;
    private boolean isCultivable;
    private boolean canBeDug;
    private String color;

    MapBlockType(boolean isAccessible, boolean isBuildable, boolean isCultivable, String color, boolean canBeDug) {
        this.isAccessible = isAccessible;
        this.isBuildable = isBuildable;
        this.isCultivable = isCultivable;
        this.canBeDug = canBeDug;
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

    public boolean canBeDug() {
        return canBeDug;
    }

    public String getColor() {
        return color;
    }
}
