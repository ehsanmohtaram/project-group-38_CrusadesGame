package view.controller;
import controller.Controller;
import controller.GameController;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import model.Kingdom;
import model.Map;
import model.MapBlock;
import model.building.*;
import view.BuildingMenu;
import view.LoginMenu;
import view.Style;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
        gameTools.setLayoutY(630);
        gameTools.setLayoutX(-20);
        BackgroundSize backgroundSize = new BackgroundSize(1540, 300, false, false, false, false);
        Image image = new Image(LoginMenu.class.getResource("/images/menus/menu.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        gameTools.setBackground(new Background(backgroundImage));
        addMenuButton(gameTools);
        mapDesignMenu.getChildren().add(gameTools);
    }

    public void addMenuButton(Pane gameTools) {
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        Pane mainPane = new Pane();
        addStockAndHeadButton(mainPane);
        HBox buttonHolder = new HBox();
        Rectangle defensive = new Rectangle(50, 50);
        defensive.setFill(new ImagePattern(new Image(GameUI.class.getResource("/images/buttons/building_B.png").toExternalForm())));
        Rectangle producer = new Rectangle(50, 50);
        producer.setFill(new ImagePattern(new Image(GameUI.class.getResource("/images/buttons/building-B1.png").toExternalForm())));
        Rectangle camp = new Rectangle(50, 50);
        camp.setFill(new ImagePattern(new Image(GameUI.class.getResource("/images/buttons/building-B2.png").toExternalForm())));
        Rectangle stock = new Rectangle(50, 50);
        stock.setFill(new ImagePattern(new Image(GameUI.class.getResource("/images/buttons/building-B3.png").toExternalForm())));
        Rectangle siege = new Rectangle(50, 50);
        siege.setFill(new ImagePattern(new Image(GameUI.class.getResource("/images/buttons/building-B4.png").toExternalForm())));
        buttonHolder.getChildren().addAll(defensive, producer, camp, stock, siege);
        buttonHolder.setViewOrder(-1);
        rectangles.add(defensive); rectangles.add(producer); rectangles.add(camp); rectangles.add(stock); rectangles.add(siege);
        mainPane.getChildren().add(buttonHolder);
        balanceShow(mainPane);
        buttonHolder.setLayoutX(340);
        buttonHolder.setLayoutY(240);
        gameTools.getChildren().add(mainPane);
        int counter = 0;
        for (Rectangle rectangle : rectangles) {
            int finalCounter = counter;
            rectangle.setOnMouseClicked(mouseEvent -> {
                if(mainPane.getChildren().size() == 4) {
                    mainPane.getChildren().remove(2); mainPane.getChildren().remove(2);
                }
                for (Rectangle opacityManger : rectangles) opacityManger.setOpacity(1);
                rectangle.setOpacity(0.5);
                buildingMenuShow(mainPane , finalCounter);
            });
            counter++;
        }
    }
    public void addStockAndHeadButton(Pane mainPane) {
        for (Kingdom kingdom : gameMap.getPlayers()) {
            redirectToBuildingMenu((Rectangle)kingdom.getHeadquarter().getPosition().getChildren().get(0), mainPane);
            redirectToBuildingMenu((Rectangle)kingdom.getBuildings().get(1).getPosition().getChildren().get(0), mainPane);
        }
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
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        coinValue.setText(decimalFormat.format(gameMap.getKingdomByOwner(Controller.currentUser).getBalance()));
    }

    public void buildingMenuShow(Pane mainPane, int type) {
        Label buildingName = new Label();
        buildingName.setFont(style.Font0(15));
        buildingName.setTextFill(Color.BEIGE);
        HBox buildingHolder = new HBox();
        for (BuildingType buildingType : BuildingType.values()) {
            if (type == 0 && ((buildingType.specificConstant instanceof DefensiveStructureType && !buildingType.name().equals(BuildingType.HEAD_QUARTER.name())) || buildingType.name().equals(BuildingType.STAIRS.name()))) {
                addBuildingToPane(buildingType, buildingHolder, buildingName, mainPane);
            }
            else if (type == 1 && (buildingType.specificConstant instanceof ProducerType || buildingType.specificConstant instanceof MineType)) {
                addBuildingToPane(buildingType, buildingHolder, buildingName, mainPane);
            }
            else if (type == 2 && buildingType.specificConstant instanceof CampType) {
                addBuildingToPane(buildingType, buildingHolder, buildingName, mainPane);
            }
            else if (type == 3 && (buildingType.specificConstant instanceof StockType || buildingType.specificConstant == null)) {
                addBuildingToPane(buildingType, buildingHolder, buildingName, mainPane);
            }
            else if (type == 4 && buildingType.specificConstant instanceof SiegeType) {
                addBuildingToPane(buildingType, buildingHolder, buildingName, mainPane);
            }
        }
        ScrollPane scrollPane = new ScrollPane(buildingHolder);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setBlendMode(BlendMode.DARKEN);
        scrollPane.setPrefSize(780, 120);
        scrollPane.setLayoutX(350); scrollPane.setLayoutY(120);
        buildingName.setLayoutX(350); buildingName.setLayoutY(60);
        mainPane.getChildren().addAll(buildingName, scrollPane);
    }

    public void addBuildingToPane(BuildingType buildingType, HBox buildingHolder, Label buildingName,Pane mainPane ) {
        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(100, 100);
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, false, false, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(buildingType.getTexture(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        stackPane.setBackground(new Background(backgroundImage));
        buildingHolder.getChildren().add(stackPane);
        stackPane.setOnMouseEntered(mouseEvent -> buildingName.setText(buildingType.name().toLowerCase().replaceAll("_"," ")));
        stackPane.setOnMouseExited(mouseEvent -> buildingName.setText(""));
        dragAndDropBuilding(stackPane, buildingType ,mainPane);
    }


    public void dragAndDropBuilding(StackPane stackPane, BuildingType buildingType, Pane mainPane) {
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
                pauseTransition.setOnFinished(actionEvent -> setBuilding(typeOfBuilding, mainPane));
                pauseTransition.play();
            }
            isDragActive = 0;
        });
    }

    public void setBuilding(BuildingType buildingType, Pane mainPane) {
        HashMap<String, String> option = new HashMap<>();
        option.put("t", buildingType.name());
        option.put("x", Integer.toString(mouseOnBlock.getxPosition()));
        option.put("y", Integer.toString(mouseOnBlock.getyPosition()));
        String result = gameController.dropBuilding(option);
        if (result.equals("done")) {
            Rectangle rectangle = new Rectangle(100, 100);
            redirectToBuildingMenu(rectangle, mainPane);
            rectangle.setFill(new ImagePattern(buildingType.getTexture()));
            mouseOnBlock.getChildren().add(rectangle);
            updateBalance();
        }
        else {
            System.out.println(result);
        }
    }

    public void redirectToBuildingMenu(Rectangle building, Pane manePane) {
        building.setOnMouseClicked(mouseEvent -> {
            if (!((MapBlock)building.getParent()).getBuildings()
                    .getOwner().equals(gameMap.getKingdomByOwner(Controller.currentUser))) return;
            if(manePane.getChildren().size() == 4) {
                manePane.getChildren().remove(2);
                manePane.getChildren().remove(2);
                for(Node node : ((HBox) manePane.getChildren().get(0)).getChildren()) node.setOpacity(1);
            }
            new BuildingMenu(manePane, ((MapBlock)building.getParent()).getBuildings().getBuildingType(), (MapBlock)building.getParent(),gameMap, coinValue).buildingMenuUISetup();
        });
    }


}
