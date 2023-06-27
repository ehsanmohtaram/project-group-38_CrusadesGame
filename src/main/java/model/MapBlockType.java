package model;


import javafx.scene.image.Image;
import view.LoginMenu;

public enum MapBlockType {
    EARTH(true , true , true, "\u001B[48;5;137m", true,  new Image(LoginMenu.class.getResource("/images/landTextures/earth.jpg").toExternalForm())),
    GRAVEL(true , true , true, "\u001B[48;5;137m", true, new Image(LoginMenu.class.getResource("/images/landTextures/gravel.jpg").toExternalForm())),
    SLATE(true , false , false, "\u001B[48;5;102m", false, new Image(LoginMenu.class.getResource("/images/landTextures/slate.jpg").toExternalForm())),
    ROCK(false, false , false, "\u001B[48;5;241m", false, new Image(LoginMenu.class.getResource("/images/landTextures/rock.jpg").toExternalForm())),
    IRON(true , false , false, "\u001B[48;5;189m", false, new Image(LoginMenu.class.getResource("/images/landTextures/iron.jpg").toExternalForm())),
    GRASSLAND(true , true , true, "\u001B[48;5;107m", true, new Image(LoginMenu.class.getResource("/images/landTextures/grassland.jpg").toExternalForm())),
    MEADOW(true , true , true, "\u001B[48;5;70m", true, new Image(LoginMenu.class.getResource("/images/landTextures/meadow.jpg").toExternalForm())),
    DENSE_MEADOW(true , true , true, "\u001B[48;5;82m", true, new Image(LoginMenu.class.getResource("/images/landTextures/denseMeadow.jpg").toExternalForm())),
    SEA(false , false , false, "\u001B[48;5;24m", false, new Image(LoginMenu.class.getResource("/images/landTextures/sea.jpg").toExternalForm())),
    BEACH(true , false , false, "\u001B[48;5;186m", false, new Image(LoginMenu.class.getResource("/images/landTextures/beach.jpg").toExternalForm())),
    SMALL_POND(false , false , false, "\u001B[48;5;37m", false, new Image(LoginMenu.class.getResource("/images/landTextures/pond.jpg").toExternalForm())),
    BIG_POND(false , false , false, "\u001B[48;5;37m", false, new Image(LoginMenu.class.getResource("/images/landTextures/pond.jpg").toExternalForm())),
    RIVER(false , false , false,  "\u001B[48;5;31m", false, new Image(LoginMenu.class.getResource("/images/landTextures/river.jpg").toExternalForm())),
    SHALLOW_WATER(true , false , false , "\u001B[48;5;30m", false, new Image(LoginMenu.class.getResource("/images/landTextures/shallow.jpg").toExternalForm())),
    PLAIN(true , false , false,"\u001B[48;5;23m", false, new Image(LoginMenu.class.getResource("/images/landTextures/plain.jpg").toExternalForm())),
    HOLE(false , false , false,"\u001B[48;5;88m", false, new Image(LoginMenu.class.getResource("/images/landTextures/hole.jpg").toExternalForm())),
    OIL(true , false , false, "\u001B[48;5;16m", false, new Image(LoginMenu.class.getResource("/images/landTextures/oil.jpg").toExternalForm()));

    private boolean isAccessible;
    private boolean isBuildable;
    private boolean isCultivable;
    private boolean canBeDug;
    private String color;
    private Image texture;

    MapBlockType(boolean isAccessible, boolean isBuildable, boolean isCultivable, String color, boolean canBeDug, Image texture) {
        this.isAccessible = isAccessible;
        this.isBuildable = isBuildable;
        this.isCultivable = isCultivable;
        this.canBeDug = canBeDug;
        this.color = color;
        this.texture = texture;
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

    public Image getTexture() {
        return texture;
    }
}

