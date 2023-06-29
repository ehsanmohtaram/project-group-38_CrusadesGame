package view.animation;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import view.LoginMenu;

public class RollingPaper extends Transition {
    private Rectangle rectangle;
    private boolean isFirstTime = true;

    public RollingPaper(Rectangle rectangle) {
        this.rectangle = rectangle;
        rectangle.setFill(new ImagePattern(new Image(LoginMenu.class.getResource("/images/animation/paper/1.png").toExternalForm())));
        setCycleCount(1);
        setCycleDuration(Duration.millis(800));
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    @Override
    protected void interpolate(double v) {
        int i = 1;
        if(0 < v && v <= 0.2) i = 1;
        else if (0.2 < v && v <= 0.4) i = 2;
        else if (0.4 < v && v <= 0.6) i = 3;
        else if (0.6 < v && v <= 0.8) i = 4;
        else if(0.8 < v && v <= 1) i = 5;
        if(isFirstTime)
            rectangle.setFill(new ImagePattern(new Image(LoginMenu.class.getResource("/images/animation/paper/" + i + ".png").toExternalForm())));
        else
            rectangle.setFill(new ImagePattern(new Image(LoginMenu.class.getResource("/images/animation/paper/" + (6 - i) + ".png").toExternalForm())));
    }
}
