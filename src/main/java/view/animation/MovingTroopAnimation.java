package view.animation;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.unit.UnitType;
import view.LoginMenu;

public class MovingTroopAnimation extends Transition {
    private String folderName;
    private boolean isRight;
    private Rectangle rectangle;

    public MovingTroopAnimation(UnitType unitType, Rectangle soldier) {
        this.folderName = unitType.getAnimationCategory();
        this.isRight = true;
        this.rectangle = soldier;
        setCycleCount(-1);
        setCycleDuration(Duration.seconds(1));
        rectangle.setFill(new ImagePattern(new Image(LoginMenu.class.getResource("/images/animation/unit/" + folderName + "/right/1.png").toExternalForm())));
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    @Override
    protected void interpolate(double v) {
        int i = 1;
        if(0 < v && v <= 0.125) i = 1;
        else if (0.125 < v && v <= 0.25) i = 2;
        else if (0.25 < v && v <= 0.375) i = 3;
        else if (0.375 < v && v <= 0.5) i = 4;
        else if (0.5 < v && v <= 0.625) i = 5;
        else if (0.625 < v && v <= 0.75) i = 6;
        else if (0.75 < v && v <= 0.875) i = 7;
        else if(0.875 < v && v <= 1) i = 8;
        if(isRight)
            rectangle.setFill(new ImagePattern(new Image(LoginMenu.class.getResource("/images/animation/unit/" + folderName + "/right/" + i + ".png").toExternalForm())));
        else
            rectangle.setFill(new ImagePattern(new Image(LoginMenu.class.getResource("/images/animation/unit/" + folderName + "/left/" + i + ".png").toExternalForm())));
    }
}
