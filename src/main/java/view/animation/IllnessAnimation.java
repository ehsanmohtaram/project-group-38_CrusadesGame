package view.animation;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.MapBlock;
import model.building.Building;

public class IllnessAnimation extends Transition {
    private MapBlock mapBlock;
    private Image image1 = new Image(IllnessAnimation.class.getResource("/images/illness/1.png").toExternalForm());
    private Image image2 = new Image(IllnessAnimation.class.getResource("/images/illness/2.png").toExternalForm());
    private Image image3 = new Image(IllnessAnimation.class.getResource("/images/illness/3.png").toExternalForm());
    private Image image4 = new Image(IllnessAnimation.class.getResource("/images/illness/4.png").toExternalForm());
    private ImageView imageView = new ImageView(image1);


    public IllnessAnimation(MapBlock mapBlock) {
        this.mapBlock = mapBlock;
        mapBlock.getChildren().add(0,imageView);
        this.setCycleDuration(Duration.seconds(1.5));
        this.setCycleCount(-1);
    }

    @Override
    protected void interpolate(double v) {
        if (v < 0.25){
            imageView.setImage(image1);
        } else if (v < 0.5) {
            imageView.setImage(image2);
        } else if (v < 0.75) {
            imageView.setImage(image3);
        }else if (v < 1) {
            imageView.setImage(image4);
        }
    }
}
