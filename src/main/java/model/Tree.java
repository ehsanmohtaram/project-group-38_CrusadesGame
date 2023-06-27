package model;

import javafx.scene.image.Image;
import view.LoginMenu;

public enum Tree {
    CHERRY(new Image(LoginMenu.class.getResource("/images/tree/cherry.png").toExternalForm())),
    OLIVE(new Image(LoginMenu.class.getResource("/images/tree/olive.png").toExternalForm())),
    COCONUT(new Image(LoginMenu.class.getResource("/images/tree/coconut.png").toExternalForm())),
    DATE(new Image(LoginMenu.class.getResource("/images/tree/palm.png").toExternalForm())),
    DESSERT(new Image(LoginMenu.class.getResource("/images/tree/dessert.png").toExternalForm()));

    private Image texture;

    Tree(Image texture) {
        this.texture = texture;
    }

    public Image getTexture() {
        return texture;
    }

    public static Tree findEnumByName(String TreeName) {
        for (Tree searchForType : Tree.values())
            if (searchForType.name().toLowerCase().equals(TreeName))
                return searchForType;
        return null;
    }
}
