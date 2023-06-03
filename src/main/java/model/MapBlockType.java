package model;


public enum MapBlockType {
    EARTH(true , true , true, "\u001B[48;5;137m", true, "/images/landTextures/earth.jpg"),
    GRAVEL(true , true , true, "\u001B[48;5;137m", true,"/images/landTextures/gravel.jpg"),
    SLATE(true , false , false, "\u001B[48;5;102m", false,"/images/landTextures/slate.jpg"),
    ROCK(false, false , false, "\u001B[48;5;241m", false,"/images/landTextures/rock.jpg"),
    IRON(true , false , false, "\u001B[48;5;189m", false,"/images/landTextures/iron.jpg"),
    GRASSLAND(true , true , true, "\u001B[48;5;107m", true,"/images/landTextures/grassland.jpg"),
    MEADOW(true , true , true, "\u001B[48;5;70m", true,"/images/landTextures/meadow.jpg"),
    DENSE_MEADOW(true , true , true, "\u001B[48;5;82m", true,"/images/landTextures/denseMeadow.jpg"),
    SEA(false , false , false, "\u001B[48;5;24m", false,"/images/landTextures/sea.jpg"),
    BEACH(true , false , false, "\u001B[48;5;186m", false,"/images/landTextures/beach.jpg"),
    SMALL_POND(false , false , false, "\u001B[48;5;37m", false,"/images/landTextures/pond.jpg"),
    BIG_POND(false , false , false, "\u001B[48;5;37m", false,"/images/landTextures/pond.jpg"),
    RIVER(false , false , false,  "\u001B[48;5;31m", false,"/images/landTextures/river.jpg"),
    SHALLOW_WATER(true , false , false , "\u001B[48;5;30m", false,"/images/landTextures/shallow.jpg"),
    PLAIN(true , false , false,"\u001B[48;5;23m", false,"/images/landTextures/plain.jpg"),
    HOLE(false , false , false,"\u001B[48;5;88m", false,"/images/landTextures/hole.jpg"),
    OIL(true , false , false, "\u001B[48;5;16m", false,"/images/landTextures/oil.jpg");

    private boolean isAccessible;
    private boolean isBuildable;
    private boolean isCultivable;
    private boolean canBeDug;
    private String color;
    private String textureAddress;

    MapBlockType(boolean isAccessible, boolean isBuildable, boolean isCultivable, String color, boolean canBeDug, String textureAddress) {
        this.isAccessible = isAccessible;
        this.isBuildable = isBuildable;
        this.isCultivable = isCultivable;
        this.canBeDug = canBeDug;
        this.color = color;
        this.textureAddress = textureAddress;
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

    public String getTextureAddress() {
        return textureAddress;
    }
}

