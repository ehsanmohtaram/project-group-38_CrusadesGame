package view.controller;
import controller.Controller;
import controller.GameController;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Map;
import model.MapBlock;
import model.building.BuildingType;
import model.building.SiegeType;
import view.LoginMenu;
import view.Style;
import java.util.HashMap;

public class GameUI {
    private final Style style;
    private final GameController gameController;
    private final Pane mapDesignMenu;
    private final Map gameMap;
    private int isDragActive;
    private Label coinValue;
    private BuildingType typeOfBuilding;
    public static MapBlock mouseOnBlock = null;

    public GameUI(Pane mapDesignPane, Map gameMap) {
        this.mapDesignMenu = mapDesignPane;
        this.gameController = new GameController(gameMap);
        this.gameMap = gameMap;
        style = new Style();
        isDragActive = 0;
        typeOfBuilding = null;
    }


    public void runGame () {
        addToolBar();
    }
    public void addToolBar() {
        Pane gameTools = new StackPane();
        gameTools.setPrefSize(1540,300);
        gameTools.setLayoutY(650);
        gameTools.setLayoutX(-20);
        BackgroundSize backgroundSize = new BackgroundSize(1540, 300, false, false, false, false);
        Image image = new Image(LoginMenu.class.getResource("/images/menus/menu.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        gameTools.setBackground(new Background(backgroundImage));
        addMenuButton(gameTools);
        mapDesignMenu.getChildren().add(gameTools);
    }

    public void addMenuButton(Pane gameTools) {
        Pane mainPane = new Pane();
        HBox buttonHolder = new HBox();
        Rectangle building = new Rectangle(50, 50);
        building.setFill(new ImagePattern(new Image(GameUI.class.getResource("/images/buttons/building_B.png").toExternalForm())));
        Rectangle weapon = new Rectangle(50, 50);
        weapon.setFill(new ImagePattern(new Image(GameUI.class.getResource("/images/buttons/building_B.png").toExternalForm())));
        Rectangle untitled = new Rectangle(50, 50);
        untitled.setFill(new ImagePattern(new Image(GameUI.class.getResource("/images/buttons/building_B.png").toExternalForm())));
        buttonHolder.getChildren().addAll(building, weapon, untitled);
        mainPane.getChildren().add(buttonHolder);
        balanceShow(mainPane);
        buttonHolder.setLayoutX(340);
        buttonHolder.setLayoutY(240);
        gameTools.getChildren().add(mainPane);
        building.setOnMouseClicked(mouseEvent -> {
            if(mainPane.getChildren().size() == 2) {
                building.setOpacity(0.5);
                buildingMenuShow(mainPane);
            }
            else {
                building.setOpacity(1);
                mainPane.getChildren().remove(1);
            }

        });
    }
    public void balanceShow(Pane mainPane) {
        VBox balance = new VBox();
        Rotate rotate = new Rotate();
        rotate.setAngle(10);
        balance.getTransforms().add(rotate);
        balance.setAlignment(Pos.CENTER);
        Label coin = new Label(" COIN");
        coin.setFont(style.Font0(15));
        coin.setTextFill(Color.rgb(141,136 ,40));
        coinValue = new Label();
        updateBalance();
        coinValue.setFont(style.Font0(15));
        coinValue.setTextFill(Color.rgb(141,136 ,40));
        balance.getChildren().addAll(coin, coinValue);
        balance.setSpacing(10);
        balance.setLayoutX(1370);
        balance.setLayoutY(175);
        mainPane.getChildren().add(balance);
    }

    public void updateBalance() {
        coinValue.setText(Double.toString(gameMap.getKingdomByOwner(Controller.currentUser).getBalance()));
    }

    public void buildingMenuShow(Pane mainPane) {
        HBox buildingHolder = new HBox();
        for (BuildingType buildingType : BuildingType.values()) {
            if (!(buildingType.specificConstant instanceof SiegeType)) {
                StackPane stackPane = new StackPane();
                stackPane.setPrefSize(100, 100);
                BackgroundSize backgroundSize = new BackgroundSize(100, 100, false, false, false, false);
                BackgroundImage backgroundImage = new BackgroundImage(buildingType.getTexture(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                stackPane.setBackground(new Background(backgroundImage));
                buildingHolder.getChildren().add(stackPane);
                Label name = new Label(buildingType.name());
                HBox nameBox = new HBox();
                nameBox.setAlignment(Pos.CENTER);
                nameBox.setMaxSize(80, 10);
                nameBox.setBackground(Background.fill(Color.GRAY));
                nameBox.getChildren().add(name);
                name.setFont(style.Font0(10));
                stackPane.setOnMouseEntered(mouseEvent -> stackPane.getChildren().add(nameBox));
                stackPane.setOnMouseExited(mouseEvent -> stackPane.getChildren().remove(nameBox));
                dragAndDropBuilding(stackPane, buildingType);
            }
        }
        ScrollPane scrollPane = new ScrollPane(buildingHolder);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setBlendMode(BlendMode.DARKEN);
        scrollPane.setPrefSize(780, 120);
        scrollPane.setLayoutX(350);
        scrollPane.setLayoutY(120);
        mainPane.getChildren().add(scrollPane);
    }


    public void dragAndDropBuilding(StackPane stackPane, BuildingType buildingType) {
        Rectangle movingImage = new Rectangle(100, 100);
        stackPane.setOnDragDetected(mouseEvent -> {
            isDragActive = 1;
            movingImage.setFill(Color.TRANSPARENT);
            mapDesignMenu.getChildren().add(movingImage);
        });
        stackPane.setOnMouseDragged(mouseEvent -> {
            movingImage.setFill(new ImagePattern(buildingType.getTexture()));
            movingImage.setOpacity(0.5);
            movingImage.setLayoutX(mouseEvent.getScreenX() - 50);
            movingImage.setLayoutY(mouseEvent.getScreenY() - 50);
            typeOfBuilding = buildingType;
        });
        mapDesignMenu.getScene().getRoot().setOnMouseReleased(mouseEvent -> {
            if (isDragActive == 1) {
                mapDesignMenu.getChildren().remove(mapDesignMenu.getChildren().size() - 1);
                PauseTransition pauseTransition = new PauseTransition(Duration.millis(10));
                pauseTransition.setOnFinished(actionEvent -> setBuilding(typeOfBuilding));
                pauseTransition.play();
            }
            isDragActive = 0;
        });
    }

    public void setBuilding(BuildingType buildingType) {
        HashMap<String, String> option = new HashMap<>();
        option.put("t", buildingType.name());
        option.put("x", Integer.toString(mouseOnBlock.getxPosition()));
        option.put("y", Integer.toString(mouseOnBlock.getyPosition()));
        String result = gameController.dropBuilding(option);
        if (result.equals("done")) {
            Rectangle rectangle = new Rectangle(100, 100);
            rectangle.setFill(new ImagePattern(buildingType.getTexture()));
            mouseOnBlock.getChildren().add(rectangle);
            updateBalance();
        }
        else {
            System.out.println(result);
        }
    }

}
