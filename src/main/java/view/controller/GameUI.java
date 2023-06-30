package view.controller;
import controller.Controller;
import controller.GameController;
import controller.MapDesignController;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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
    private final Pane maPane;
    private int isDragActive;
    private Label coinValue;
    private BuildingType typeOfBuilding;
    public static MapBlock mouseOnBlock = null;
    public static Clipboard clipboard = null;
    public static ClipboardContent clipboardContent;

    public GameUI(Pane mapDesignPane, Map gameMap, Pane mapPane) {
        clipboard = Clipboard.getSystemClipboard();
        clipboardContent = new ClipboardContent();
        this.mapDesignMenu = mapDesignPane;
        this.maPane = mapPane;
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
        gameTools.setLayoutY(580);
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
        balanceShowAndNextTurn(mainPane);
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
            redirectToBuildingMenu((Rectangle)kingdom.getHeadquarter().getPosition().getChildren().get(kingdom.getHeadquarter().getPosition().getChildren().size() - 1), mainPane);
            redirectToBuildingMenu((Rectangle)kingdom.getBuildings().get(1).getPosition().getChildren().get(kingdom.getBuildings().get(1).getPosition().getChildren().size() - 1), mainPane);
        }
    }
    public void balanceShowAndNextTurn(Pane mainPane) {
        VBox balance = new VBox();
        Rotate rotate = new Rotate();
        rotate.setAngle(12);
        balance.getTransforms().add(rotate);
        balance.setAlignment(Pos.CENTER);
        Label coin = new Label(" COIN");
        coin.setFont(style.Font0(15));
        coin.setTextFill(Color.rgb(141,136 ,40));
        coinValue = new Label();
        updateBalance();
        coinValue.setFont(style.Font0(15));
        coinValue.setTextFill(Color.rgb(141,136 ,40));
        Label nextTurn = new Label("NEXT");
        nextTurn.setFont(style.Font0(15));
        nextTurn.setTextFill(Color.rgb(141,136 ,40));
        nextTurn.setOnMouseEntered(mouseEvent -> nextTurn.setTextFill(Color.rgb(185,182,182,0.5)));
        nextTurn.setOnMouseExited(mouseEvent -> nextTurn.setTextFill(Color.rgb(141,136 ,40)));
        nextTurn.setOnMouseClicked(mouseEvent -> {
            gameController.nextTurn();
            if(mainPane.getChildren().size() == 4) {
                mainPane.getChildren().remove(2); mainPane.getChildren().remove(2);
            }
            for (Node opacityManger : ((HBox)mainPane.getChildren().get(0)).getChildren()) opacityManger.setOpacity(1);
            updateBalance();
        });
        balance.getChildren().addAll(coin, coinValue, nextTurn);
        addClipboard(balance, mainPane);
        balance.setSpacing(5);
        balance.setLayoutX(1372);
        balance.setLayoutY(160);
        mainPane.getChildren().add(balance);
    }

    public void addClipboard(VBox vBox, Pane mainPane) {
        Rectangle rectangle = new Rectangle(25, 20, new ImagePattern(new Image(GameUI.class.getResource("/images/buttons/clipboard.png").toExternalForm())));
        Rectangle buildingHolder = new Rectangle(70, 70);
        rectangle.setOnMouseClicked(mouseEvent -> {
            if (clipboard.hasImage()) {
                VBox popUp = new VBox();
                popUp.setViewOrder(-2);
                Button ok = new Button();
                Pane mapDesignMenu = (Pane) mainPane.getParent().getParent();
                style.popUp0(mainPane, popUp, ok, 10, 10, 125, 125, 180, 50, 100, 15, 1200, 130, "", 20);
                popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
                popUp.setAlignment(Pos.CENTER);
                popUp.getChildren().clear();
                mapDesignMenu.getChildren().get(0).setDisable(true);
                for (int i = 0; i < mainPane.getChildren().size() - 1; i++)
                    mainPane.getChildren().get(i).setDisable(true);
                if (clipboard.getImage() != null) buildingHolder.setFill(new ImagePattern(clipboard.getImage()));
                popUp.getChildren().add(buildingHolder);
                popUp.setOnMouseClicked(mouseEvent2 -> {
                    mainPane.getChildren().remove(popUp);
                    mapDesignMenu.getChildren().get(0).setDisable(false);
                    for (int i = 0; i < mainPane.getChildren().size(); i++)
                        mainPane.getChildren().get(i).setDisable(false);
                });
            }
        });
        vBox.getChildren().add(rectangle);
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
            Rectangle rectangle = new Rectangle(70, 70);
            redirectToBuildingMenu(rectangle, mainPane);
            rectangle.setFill(new ImagePattern(buildingType.getTexture()));
            mouseOnBlock.getChildren().add(rectangle);
            updateBalance();
        }
        else {
            VBox popUp = new VBox();
            Button ok = new Button();
            style.popUp0(mainPane, popUp, ok, 20, 20, 450, 70, 180, 50, 100, 15,500, 100, result, 20);
            popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
            mapDesignMenu.getChildren().get(0).setDisable(true);
            for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
            ok.setOnMouseClicked(mouseEvent -> {
                mainPane.getChildren().remove(popUp);
                mapDesignMenu.getChildren().get(0).setDisable(false);
                for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
            });
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
            new BuildingMenu(manePane, ((MapBlock)building.getParent()).getBuildings().getBuildingType(), (MapBlock)building.getParent(),gameMap, coinValue, gameController).buildingMenuUISetup();
        });
    }

    public static void copyProcess() {
        if (MapDesignController.selectedBlocks.size() == 1 && MapDesignController.selectedBlocks.get(0).getBuildings() != null)
            clipboardContent.putImage(MapDesignController.selectedBlocks.get(0).getBuildings().getBuildingType().getTexture());
        clipboard.setContent(clipboardContent);
    }

    public void pasteProcess() {
        if (MapDesignController.selectedBlocks.size() == 1 && clipboardContent.hasImage()) {
            Rectangle rectangle = new Rectangle(70, 70, new ImagePattern(clipboardContent.getImage()));
            HashMap<String, String> option = new HashMap<>();
            BuildingType buildingType = null;
            for (BuildingType buildingType1 : BuildingType.values())
                if (buildingType1.getTexture().getUrl().equals(clipboardContent.getImage().getUrl())) buildingType = buildingType1;
            option.put("t", buildingType.name());
            option.put("x", Integer.toString(MapDesignController.selectedBlocks.get(0).getxPosition()));
            option.put("y", Integer.toString(MapDesignController.selectedBlocks.get(0).getyPosition()));
            String result = gameController.dropBuilding(option);
            if (result.equals("done")) MapDesignController.selectedBlocks.get(0).getChildren().add(rectangle);
        }
    }


}
