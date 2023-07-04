package view.animation;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.building.Building;

public class FireAnimation extends Transition {
    private Building building;
    private int state;
    private ImageView fire = new ImageView(new Image(FireAnimation.class.getResource("/images/fire/1.png").toExternalForm()));
    private Image image1 = new Image(FireAnimation.class.getResource("/images/fire/1.png").toExternalForm());
    private Image image2 = new Image(FireAnimation.class.getResource("/images/fire/2.png").toExternalForm());
    private Image image3 = new Image(FireAnimation.class.getResource("/images/fire/3.png").toExternalForm());
    private Image image4 = new Image(FireAnimation.class.getResource("/images/fire/4.png").toExternalForm());
    private Image image5 = new Image(FireAnimation.class.getResource("/images/fire/5.png").toExternalForm());
    private Image image6 = new Image(FireAnimation.class.getResource("/images/fire/6.png").toExternalForm());


    public FireAnimation(Building building, int state) {
        this.building = building;
        this.state = state;
        building.getPosition().getGraphics().getChildren().add(fire);
        fire.setLayoutY(100);
        fire.setY(100);
        this.setCycleDuration(Duration.seconds(2));
        this.setCycleCount(-1);
    }

    @Override
    protected void interpolate(double v) {
//        if (state > 0) {
            if (v < 0.1666) {
                if (state == 1) {
                    fire.setImage(image1);
                    fire.setScaleX(2);
                    fire.setScaleY(2);
                } else if (state == 2) {
                    fire.setImage(image1);
                    fire.setScaleX(1.5);
                    fire.setScaleY(1.5);
                } else if (state == 3) {
                    fire.setImage(image1);
                    fire.setScaleX(1);
                    fire.setScaleY(1);
                } else {
                    building.getPosition().getGraphics().getChildren().remove(fire);
                    this.stop();
                }
            } else if (v < 0.332) {
                if (state == 1) {
                    fire.setImage(image2);
                    fire.setScaleX(2);
                    fire.setScaleY(2);
                } else if (state == 2) {
                    fire.setImage(image2);
                    fire.setScaleX(1.5);
                    fire.setScaleY(1.5);
                } else if (state == 3) {
                    fire.setImage(image2);
                    fire.setScaleX(1);
                    fire.setScaleY(1);
                } else {
                    building.getPosition().getGraphics().getChildren().remove(fire);
                    this.stop();
                }
            } else if (v < 0.499) {
                if (state == 1) {
                    fire.setImage(image3);
                    fire.setScaleX(2);
                    fire.setScaleY(2);
                } else if (state == 2) {
                    fire.setImage(image3);
                    fire.setScaleX(1.5);
                    fire.setScaleY(1.5);
                } else if (state == 3) {
                    fire.setImage(image3);
                    fire.setScaleX(1);
                    fire.setScaleY(1);
                } else {
                    building.getPosition().getGraphics().getChildren().remove(fire);
                    this.stop();
                }
            } else if (v < 0.5666) {
                if (state == 1) {
                    fire.setImage(image4);
                    fire.setScaleX(2);
                    fire.setScaleY(2);
                } else if (state == 2) {
                    fire.setImage(image4);
                    fire.setScaleX(1.5);
                    fire.setScaleY(1.5);
                } else if (state == 3) {
                    fire.setImage(image4);
                    fire.setScaleX(1);
                    fire.setScaleY(1);
                } else {
                    building.getPosition().getGraphics().getChildren().remove(fire);
                    this.stop();
                }
            } else if (v < 0.833) {
                if (state == 1) {
                    fire.setImage(image5);
                    fire.setScaleX(2);
                    fire.setScaleY(2);
                } else if (state == 2) {
                    fire.setImage(image5);
                    fire.setScaleX(1.5);
                    fire.setScaleY(1.5);
                } else if (state == 3) {
                    fire.setImage(image5);
                    fire.setScaleX(1);
                    fire.setScaleY(1);
                } else {
                    building.getPosition().getGraphics().getChildren().remove(fire);
                    this.stop();
                }
            } else {
                if (state == 1) {
                    fire.setImage(image6);
                    fire.setScaleX(2);
                    fire.setScaleY(2);
                } else if (state == 2) {
                    fire.setImage(image6);
                    fire.setScaleX(1.5);
                    fire.setScaleY(1.5);
                } else if (state == 3) {
                    fire.setImage(image6);
                    fire.setScaleX(1);
                    fire.setScaleY(1);
                }
//                else {
//                    building.getPosition().getChildren().remove(fire);
//                    this.stop();
//                }
            }
//        } else {
//            building.getPosition().getChildren().remove(fire);
//            System.out.println("ali");
//        }
    }
}
