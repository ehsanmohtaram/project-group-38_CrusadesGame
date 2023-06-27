package model;

import javafx.scene.image.Image;
import view.LoginMenu;

public enum Direction {
    NORTH(new Image(LoginMenu.class.getResource("/images/direction/up.png").toExternalForm()), 0 , 1),
    SOUTH(new Image(LoginMenu.class.getResource("/images/direction/down.png").toExternalForm()), 2, 1),
    WEST(new Image(LoginMenu.class.getResource("/images/direction/left.png").toExternalForm()), 1 , 0),
    EAST(new Image(LoginMenu.class.getResource("/images/direction/right.png").toExternalForm()), 1 , 2);
    private Image image;
    private int x;
    private int y;

    Direction(Image image, int y , int x) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
