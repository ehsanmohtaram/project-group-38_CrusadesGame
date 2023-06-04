package model;


import javafx.scene.image.Image;
import view.LoginMenu;

public enum MapBlockType {
    EARTH(true , true , true, "\u001B[48;5;137m", true, "/images/landTextures/earth.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/earth.jpg").toExternalForm())),
    GRAVEL(true , true , true, "\u001B[48;5;137m", true,"/images/landTextures/gravel.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/gravel.jpg").toExternalForm())),
    SLATE(true , false , false, "\u001B[48;5;102m", false,"/images/landTextures/slate.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/slate.jpg").toExternalForm())),
    ROCK(false, false , false, "\u001B[48;5;241m", false,"/images/landTextures/rock.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/rock.jpg").toExternalForm())),
    IRON(true , false , false, "\u001B[48;5;189m", false,"/images/landTextures/iron.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/iron.jpg").toExternalForm())),
    GRASSLAND(true , true , true, "\u001B[48;5;107m", true,"/images/landTextures/grassland.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/grassland.jpg").toExternalForm())),
    MEADOW(true , true , true, "\u001B[48;5;70m", true,"/images/landTextures/meadow.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/meadow.jpg").toExternalForm())),
    DENSE_MEADOW(true , true , true, "\u001B[48;5;82m", true,"/images/landTextures/denseMeadow.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/denseMeadow.jpg").toExternalForm())),
    SEA(false , false , false, "\u001B[48;5;24m", false,"/images/landTextures/sea.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/sea.jpg").toExternalForm())),
    BEACH(true , false , false, "\u001B[48;5;186m", false,"/images/landTextures/beach.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/beach.jpg").toExternalForm())),
    SMALL_POND(false , false , false, "\u001B[48;5;37m", false,"/images/landTextures/pond.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/pond.jpg").toExternalForm())),
    BIG_POND(false , false , false, "\u001B[48;5;37m", false,"/images/landTextures/pond.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/pond.jpg").toExternalForm())),
    RIVER(false , false , false,  "\u001B[48;5;31m", false,"/images/landTextures/river.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/river.jpg").toExternalForm())),
    SHALLOW_WATER(true , false , false , "\u001B[48;5;30m", false,"/images/landTextures/shallow.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/shallow.jpg").toExternalForm())),
    PLAIN(true , false , false,"\u001B[48;5;23m", false,"/images/landTextures/plain.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/plain.jpg").toExternalForm())),
    HOLE(false , false , false,"\u001B[48;5;88m", false,"/images/landTextures/hole.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/hole.jpg").toExternalForm())),
    OIL(true , false , false, "\u001B[48;5;16m", false,"/images/landTextures/oil.jpg", new Image(LoginMenu.class.getResource("/images/landTextures/oil.jpg").toExternalForm()));

    private boolean isAccessible;
    private boolean isBuildable;
    private boolean isCultivable;
    private boolean canBeDug;
    private String color;
    private String textureAddress;
    private Image texture;

    MapBlockType(boolean isAccessible, boolean isBuildable, boolean isCultivable, String color, boolean canBeDug, String textureAddress, Image texture) {
        this.isAccessible = isAccessible;
        this.isBuildable = isBuildable;
        this.isCultivable = isCultivable;
        this.canBeDug = canBeDug;
        this.color = color;
        this.textureAddress = textureAddress;
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

    public String getTextureAddress() {
        return textureAddress;
    }

    public Image getTexture() {
        return texture;
    }
}

