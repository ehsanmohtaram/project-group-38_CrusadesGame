package model;

import javafx.scene.paint.Color;

public enum Flags {
    RED(Color.rgb(218, 34 , 34)),
    BLUE(Color.rgb(10, 150 , 180)),
    BLACK(Color.rgb(0, 0 , 0)),
    YELLOW(Color.rgb(218, 200 , 34)),
    PURPLE(Color.rgb(150, 50 , 200)),
    GREEN(Color.rgb(30, 200 , 34)),
    WHITE(Color.rgb(218, 218 , 218)),
    ORANGE(Color.rgb(218, 100 , 34));

    private final Color flagColor;

    Flags(Color flagColor) {
        this.flagColor = flagColor;
    }

    public Color getFlagColor() {
        return flagColor;
    }
}
