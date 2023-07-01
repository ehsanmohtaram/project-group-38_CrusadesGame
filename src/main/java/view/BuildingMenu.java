package view;

import controller.BuildingController;
import controller.Controller;
import controller.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.*;
import model.building.*;
import model.unit.UnitType;
import view.controller.BuildingMenuController;
import view.controller.GameUI;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class BuildingMenu {
    private final BuildingController buildingController;
    private final Style style;
    private final MapBlock mapBlock;
    private final Building building;
    private final Pane mainPane;
    private final Label coinValue;
    private final BuildingType buildingType;
    private final Map gameMap;
    private final BuildingMenuController buildingMenuController;
    private final GameController gameController;

    public BuildingMenu(Pane mainPane, BuildingType buildingType,MapBlock mapBlock ,Map gameMap, Label coinValue, GameController gameController) {
        style = new Style();
        this.mainPane = mainPane;
        this.buildingType = buildingType;
        this.buildingMenuController = new BuildingMenuController();
        this.gameController = gameController;
        this.gameMap = gameMap;
        this.coinValue = coinValue;
        this.mapBlock = mapBlock;
        building = mapBlock.getBuildings();
        buildingController = new BuildingController(gameMap, building);
    }

    public void buildingMenuUISetup(int type) {
        VBox vBox = new VBox();
        vBox.setSpacing(35);
        Label buildingName = new Label(buildingType.name().replaceAll("_", " "));
        buildingName.setTextFill(Color.BEIGE);
        buildingName.setFont(style.Font0(20));
        Rectangle buildingImage = new Rectangle(100 ,100);
        buildingImage.setFill(new ImagePattern(buildingType.getTexture()));
        vBox.getChildren().addAll(buildingName, buildingImage);
        Pane buildingInformationHolder = new Pane();
        vBox.setLayoutX(350); vBox.setLayoutY(60);
        mainPane.getChildren().addAll(vBox, buildingInformationHolder);
        redirect(buildingInformationHolder, type);
    }

    public void redirect(Pane buildingInformationHolder, int type) {
        if (buildingType.equals(BuildingType.HEAD_QUARTER)) headQuarterRun(buildingInformationHolder, type);
        else if (buildingType.specificConstant instanceof DefensiveStructureType) defensiveBuildingRnu(buildingInformationHolder);
        else if (buildingType.specificConstant instanceof CampType) campBuildingRun(buildingInformationHolder);
        else if (buildingType.specificConstant instanceof StockType) stockBuildingRun(buildingInformationHolder);
        else if (buildingType.specificConstant instanceof ProducerType) produceBuildingRun(buildingInformationHolder);
        else if (buildingType.specificConstant instanceof SiegeType) siegeRun(buildingInformationHolder);
        else if (buildingType.equals(BuildingType.SHOP)) runShop(buildingInformationHolder);
    }

    private void headQuarterRun(Pane buildingInformationHolder, int type) {
        HBox holder = new HBox();
        holder.setSpacing(10);
        holder.setAlignment(Pos.CENTER);
        VBox rateChange = new VBox();
        rateChange.setAlignment(Pos.CENTER);
        rateChange.setSpacing(5);
        VBox rateChanger0 = new VBox();
        rateChanger0.setAlignment(Pos.CENTER);
        rateChanger0.setSpacing(10);
        rateChanger0.setPrefSize(300, 80);
        rateChanger0.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));

        HBox fearRateInfo = new HBox();
        Label fear_L = new Label();
        slider(fearRateInfo, "( Fear Rate : ",  gameMap.getKingdomByOwner(Controller.currentUser).getFearRate(),-5, 0, 11, 212, fear_L);

        HBox taxRateInfo = new HBox();
        Label tax_L = new Label();
        slider(taxRateInfo, "( Tax Rate : ", gameMap.getKingdomByOwner(Controller.currentUser).getTaxRate(), -3, 2, 12, 231, tax_L);

        HBox foodRateInfo = new HBox();
        Label food_L = new Label();
        slider(foodRateInfo, "( Food Rate : ", gameMap.getKingdomByOwner(Controller.currentUser).getFoodRate(), -2, 1, 5, 99, food_L);

        Button fear = new Button();
        style.button0(fear, "FEAR", 100, 30);
        fear.setFont(style.Font0(12));
        fear.setOnMouseClicked(mouseEvent -> {
            rateChanger0.getChildren().clear();
            rateChanger0.getChildren().addAll(fearRateInfo, fear_L);
        });
        Button tax = new Button();
        tax.setFont(style.Font0(12));
        style.button0(tax, "TAX", 100, 30);
        tax.setOnMouseClicked(mouseEvent -> {
            rateChanger0.getChildren().clear();
            rateChanger0.getChildren().addAll(taxRateInfo, tax_L);
        });
        Button food = new Button();
        food.setFont(style.Font0(12));
        style.button0(food, "FOOD", 100, 30);
        food.setOnMouseClicked(mouseEvent -> {
            rateChanger0.getChildren().clear();
            rateChanger0.getChildren().addAll(foodRateInfo, food_L);
        });
        rateChange.getChildren().addAll(food, fear, tax);
        Rectangle popularity = new Rectangle(100, 100, new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/face.png").toExternalForm())));
        popularity.setOpacity(0.8);
        popularity.setOnMouseEntered(event -> popularity.setOpacity(0.4));
        popularity.setOnMouseExited(event -> popularity.setOpacity(0.8));
        HBox face1 = new HBox(); HBox face2 = new HBox();  HBox face3 = new HBox(); HBox face4 = new HBox();
        addPopularityFactor(face1, calculatePopularity(0) ," Tax"); addPopularityFactor(face2,calculatePopularity(1) ," Food"); addPopularityFactor(face3, calculatePopularity(2)," Religion"); addPopularityFactor(face4, calculatePopularity(3), "Fear Factor");
        HBox hBox0 = new HBox();
        hBox0.setSpacing(20);
        hBox0.setAlignment(Pos.CENTER);
        hBox0.getChildren().addAll(face1, face2);
        HBox hBox1 = new HBox();
        hBox1.setSpacing(20);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll(face3, face4);
        popularity.setOnMouseClicked(mouseEvent -> {
            rateChanger0.getChildren().clear();
            rateChanger0.getChildren().addAll(hBox0, hBox1);
        });
        switch (type) {
            case 0 : rateChanger0.getChildren().clear(); rateChanger0.getChildren().addAll(fearRateInfo, fear_L); break;
            case 1 :rateChanger0.getChildren().clear(); rateChanger0.getChildren().addAll(taxRateInfo, tax_L); break;
            case 2 : rateChanger0.getChildren().clear(); rateChanger0.getChildren().addAll(hBox0, hBox1); break;
        }
        holder.getChildren().addAll(rateChange, rateChanger0, popularity);
        buildingInformationHolder.getChildren().add(holder);
        buildingInformationHolder.setLayoutY(120);
        buildingInformationHolder.setLayoutX(520);
    }

    public void slider(HBox fearRateInfo, String text, int firstValue, int min, int type, int range, int boxLen, Label value) {
        fearRateInfo.setAlignment(Pos.CENTER);
        fearRateInfo.setSpacing(10);
        HBox fearRateBox = new HBox();
        fearRateBox.setMinSize(boxLen, 31);
        fearRateBox.setAlignment(Pos.CENTER);
        fearRateBox.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));
        fearRateBox.setSpacing(2);
        ArrayList<Rectangle> fearRate = new ArrayList<>();
        for (int i = 0; i  < range ; i++) {
            Rectangle rectangle = new Rectangle(15, 25);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setStroke(Color.rgb(170,139,100,0.8));
            rectangle.setStrokeWidth(2);
            rectangle.setArcWidth(5); rectangle.setArcHeight(5);
            fearRate.add(rectangle);
        }
        value.setText(text + firstValue + " )");
        for (int i = 0; i < firstValue - min + 1; i++) fearRate.get(i).setFill(Color.BEIGE);
        value.setFont(style.Font0(20));
        value.setAlignment(Pos.CENTER);
        for (Rectangle rectangle : fearRate) {
            rectangle.setOnMouseClicked(mouseEvent -> {
                String result = "done";
                if (type == 0) gameMap.getKingdomByOwner(Controller.currentUser).setFearRate(fearRate.indexOf(rectangle) + min);
                else if (type == 1) result = gameController.setFoodRate(fearRate.indexOf(rectangle) + min);
                else result = gameController.setTaxRate(fearRate.indexOf(rectangle) + min);
                if (result.equals("done")) {
                    for (Rectangle rectangle1 : fearRate) rectangle1.setFill(Color.TRANSPARENT);
                    for (int i = 0; ; i++) {
                        fearRate.get(i).setFill(Color.BEIGE);
                        value.setText(text + (min + i) + " )");
                        if (rectangle.equals(fearRate.get(i))) break;
                    }
                }
            });
        }
        fearRateBox.getChildren().addAll(fearRate);
        fearRateInfo.getChildren().addAll(fearRateBox);
    }

    public void addPopularityFactor(HBox holder ,int value, String text) {
        holder.setSpacing(5);
        Rectangle rectangle = new Rectangle(20 ,20);
        Label text0 = new Label(value + " ");
        text0.setFont(style.Font0(12));
        Label text1 = new Label(text);
        text1.setFont(style.Font0(12));
        if (value < 0) {
            text0.setTextFill(Color.INDIANRED);
            rectangle.setFill(new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/red.png").toExternalForm())));
        }
        else if (value == 0) {
            text0.setTextFill(Color.YELLOW);
            rectangle.setFill(new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/yellow.png").toExternalForm())));
        }
        else {
            text0.setTextFill(Color.GREEN);
            rectangle.setFill(new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/green.png").toExternalForm())));
        }
        holder.getChildren().addAll(text0, rectangle, text1);

    }

    public int calculatePopularity(int type) {
        Kingdom currentKingdom = gameMap.getKingdomByOwner(Controller.currentUser);
        int value = 0, variety = 0;
        switch (type) {
            case 0 : if (currentKingdom.getTaxRate() <= 0) value = -currentKingdom.getTaxRate() * 2 + 1;
                    else value = -currentKingdom.getTaxRate() * 2; break;
            case 1 : for (Food food : currentKingdom.getFoods().keySet()) if (currentKingdom.getFoods().get(food) > 0) variety++;
                    value =  currentKingdom.getFoodRate() * 4;
                    if (variety != 0 ) value += 2; break;
            case 2 : for (Building building : currentKingdom.getBuildings())
                        if (building.getBuildingType().equals(BuildingType.CATHEDRAL) || building.getBuildingType().equals(BuildingType.CHURCH)) value += 2;
                    break;
            case 3 : value = -currentKingdom.getFearRate(); break;
        }
        return value;
    }



    public void defensiveBuildingRnu(Pane buildingInformationHolder) {
        HBox holder = new HBox();
        holder.setAlignment(Pos.CENTER);
        holder.setSpacing(20);
        VBox hpAndText = new VBox();
        hpAndText.setAlignment(Pos.CENTER);
        hpAndText.setSpacing(15);
        Label hp = new Label("HP");
        Label value = new Label(building.getHp() + "/" + buildingType.getHP_IN_FIRST());
        value.setFont(style.Font0(25));
        hp.setFont(style.Font0(30));
        hpAndText.getChildren().addAll(hp, value);
        VBox repairHolder = new VBox();
        repairHolder.setAlignment(Pos.CENTER);
        repairHolder.setSpacing(15);
        Button repair = new Button();
        repair.setFont(style.Font0(20));
        Label repairText = new Label((buildingType.getHP_IN_FIRST() - building.getHp()) / 200 + " rock to repair");
        repairText.setFont(style.Font0(25));
        repairHolder.getChildren().addAll(repair, repairText);
        style.button0(repair, "repair", 200, 40);
        holder.getChildren().addAll(hpAndText, repairHolder);
        buildingInformationHolder.getChildren().add(holder);
        buildingInformationHolder.setLayoutY(130);
        buildingInformationHolder.setLayoutX(530);
        repair.setOnMouseClicked(mouseEvent -> {
            String result = buildingController.repairBuilding();
            if (result.equals("done")) {
                repairText.setText((buildingType.getHP_IN_FIRST() - building.getHp()) / 200 + " rock to repair");
                hp.setText(building.getHp() + "/" + buildingType.getHP_IN_FIRST());
            }
            VBox popUp = new VBox();
            Button ok = new Button();
            Pane mapDesignMenu = (Pane) mainPane.getParent().getParent();
            if (result.equals("done")) result = "Building was repaired successfully!";
            style.popUp0(mainPane, popUp, ok, 20, 20, 450, 70, 180, 50, 100, 15,500, 100, result, 20);
            popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
            mapDesignMenu.getChildren().get(0).setDisable(true);
            for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
            ok.setOnMouseClicked(mouseEvent2 -> {
                mainPane.getChildren().remove(popUp);
                mapDesignMenu.getChildren().get(0).setDisable(false);
                for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
            });
        });
        HBox.setMargin(repairHolder, new Insets(0,0,0,80));
        addAccess(holder);
    }

    public void addAccess(HBox holder) {
        if (!buildingType.equals(BuildingType.BIG_STONE_GATEHOUSE) && !buildingType.equals(BuildingType.SMALL_STONE_GATEHOUSE) &&
                !buildingType.equals(BuildingType.DRAWBRIDGE)) return;
        Rectangle access = new Rectangle(100, 100);
        Image open = new Image(BuildingMenu.class.getResource("/images/buttons/open.png").toExternalForm());
        Image close = new Image(BuildingMenu.class.getResource("/images/buttons/close.png").toExternalForm());
        Image open_c = new Image(BuildingMenu.class.getResource("/images/buttons/open-c.png").toExternalForm());
        Image close_c = new Image(BuildingMenu.class.getResource("/images/buttons/close-c.png").toExternalForm());
        if (buildingController.getAccessType()) access.setFill(new ImagePattern(open));
        else access.setFill(new ImagePattern(close));
        access.setOnMouseClicked(mouseEvent -> {
            String result;
            if (((ImagePattern) access.getFill()).getImage().equals(open_c)) result = buildingController.openAccess("close");
            else result = buildingController.openAccess("open");
            if (result.equals("done")) {
                if (((ImagePattern) access.getFill()).getImage().equals(open_c)) access.setFill(new ImagePattern(close_c));
                else access.setFill(new ImagePattern(open_c));
            }
            else System.out.println(result);
        });
        access.setOnMouseEntered(mouseEvent -> {
            if (((ImagePattern) access.getFill()).getImage().equals(open)) access.setFill(new ImagePattern(open_c));
            else access.setFill(new ImagePattern(close_c));
        });
        access.setOnMouseExited(mouseEvent -> {
            if (((ImagePattern) access.getFill()).getImage().equals(open_c)) access.setFill(new ImagePattern(open));
            else access.setFill(new ImagePattern(close));
        });
        holder.getChildren().add(access);
    }

    public void campBuildingRun(Pane buildingInformationHolder) {
        if (buildingType.equals(BuildingType.STABLE) || buildingType.equals(BuildingType.SIEGE_TENT)) return;
        buildingInformationHolder.setPrefSize(808, 203);
        BackgroundSize backgroundSize = new BackgroundSize(808, 203, false, false, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(BuildingMenu.class.getResource("/images/menus/barrack.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        buildingInformationHolder.setBackground(new Background(backgroundImage));
        HBox units = new HBox();
        units.setAlignment(Pos.CENTER);
        units.setSpacing(36);
        if (buildingType.equals(BuildingType.BARRACK)) setUnitImages(units, 0);
        else if (buildingType.equals(BuildingType.MERCENARY_POST)) setUnitImages(units, 1);
        else if (buildingType.equals(BuildingType.ENGINEER_GUILD)) setUnitImages(units, -1);
        buildingInformationHolder.getChildren().add(units);
        units.setLayoutX(17);
        units.setLayoutY(55);
        if (buildingType.equals(BuildingType.BARRACK) || buildingType.equals(BuildingType.ENGINEER_GUILD)) units.setLayoutY(50);
        buildingInformationHolder.setLayoutY(57);
        buildingInformationHolder.setLayoutX(335);
    }
    //toDo unit
    public void setUnitImages(HBox units, int type) {
        for (UnitType unit : UnitType.values()) {
            if (unit.getIS_ARAB().equals(type)) {
                Rectangle unitHolder = new Rectangle(80, 140);
                unitHolder.setOpacity(0.8);
                unitHolder.setFill(new ImagePattern(unit.getMenuTexture()));
                unitHolder.setOnMouseClicked(e -> {
                    String result = buildingController.createUnit(unit);
                    if(!result.equals("successful")){
                        VBox popUp0 = new VBox();
                        Button ok = new Button();
                        style.popUp0(mainPane, popUp0, ok, 20, 20, 450, 70, 180, 50, 100, 15,500, 100, result, 20);
                        popUp0.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
                        for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
                        ok.setOnMouseClicked(mouseEvent2 -> {
                            mainPane.getChildren().remove(popUp0);
                            for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
                        });
                    }
                    updateBalance();
                });
                units.getChildren().add(unitHolder);
            }
        }
    }

    public void stockBuildingRun(Pane buildingInformationHolder) {
        HBox holder = new HBox();
        holder.setAlignment(Pos.CENTER);
        holder.setSpacing(20);
        if (buildingType.equals(BuildingType.STOCKPILE)) {
            for (ResourceType resourceType : ResourceType.values()) {
                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                vBox.setSpacing(10);
                Rectangle rectangle = new Rectangle(40, 40);
                rectangle.setFill(new ImagePattern(resourceType.getTexture()));
                Label value = new Label("0");
                buildingMenuController.getResourceAmount(gameMap, value, resourceType);
                value.setFont(style.Font0(15));
                value.setTextFill(Color.BLACK);
                vBox.getChildren().addAll(rectangle, value);
                holder.getChildren().add(vBox);
            }
        }
        else if (buildingType.equals(BuildingType.FOOD_STOCKPILE)) {
            for (Food food : Food.values()) {
                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                vBox.setSpacing(10);
                Rectangle rectangle = new Rectangle(40, 40);
                rectangle.setFill(new ImagePattern(food.getTexture()));
                Label value = new Label("0");
                buildingMenuController.getFoodAmount(gameMap, value, food);
                value.setFont(style.Font0(15));
                value.setTextFill(Color.BLACK);
                vBox.getChildren().addAll(rectangle, value);
                holder.getChildren().add(vBox);
            }
        }
        else {
            for (Weapons weapons : Weapons.values()) {
                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                vBox.setSpacing(10);
                Rectangle rectangle = new Rectangle(40, 40);
                rectangle.setFill(new ImagePattern(weapons.getTexture()));
                Label value = new Label("0");
                buildingMenuController.getWeaponAmount(gameMap, value, weapons);
                value.setFont(style.Font0(15));
                value.setTextFill(Color.BLACK);
                vBox.getChildren().addAll(rectangle, value);
                holder.getChildren().add(vBox);
            }
        }
        buildingInformationHolder.getChildren().add(holder);
        buildingInformationHolder.setLayoutY(138);
        buildingInformationHolder.setLayoutX(510);
    }

    public void siegeRun(Pane buildingInformationHolder) {
//        HashMap<String, String> optionPass;
//        String command;
//        while (true) {
//            command = CommandParser.getScanner().nextLine();
//            if (commandParser.validate(command, "back", null) != null) return "back";
//            if ((optionPass = commandParser.validate(command, "move siege", "x|xPosition/y|yPosition")) != null)
//                System.out.println(buildingController.moveSiege(optionPass));
//            else if ((optionPass = commandParser.validate(command, "attack", "x|positionX/y|positionY")) != null)
//                System.out.println(buildingController.attackOnUnit(optionPass));
//            else System.out.println("Invalid command");
//        }
    }

    public void produceBuildingRun(Pane buildingInformationHolder) {
        HBox holder = new HBox();
        holder.setSpacing(15);
        holder.setAlignment(Pos.CENTER);
        ProducerType producerType = (ProducerType) buildingType.specificConstant;
        VBox vBox0 = new VBox();
        vBox0.setAlignment(Pos.CENTER);
        vBox0.setSpacing(10);
        Rectangle rectangle0 = new Rectangle(20, 10);
        rectangle0.setStroke(Color.rgb(170,139,100,0.8));
        rectangle0.setStrokeWidth(2);
        rectangle0.setFill(Color.TRANSPARENT);
        Button button0 = new Button();
        button0.setOpacity(0.4);
        setButtons(button0, producerType.getTypeOfResource0());
        vBox0.getChildren().addAll(button0, rectangle0);
        VBox vBox_0 = new VBox();
        vBox_0.setAlignment(Pos.CENTER);
        vBox_0.setSpacing(10);
        Rectangle rectangle_0 = new Rectangle(20, 10);
        rectangle_0.setFill(Color.TRANSPARENT);
        rectangle_0.setStroke(Color.rgb(170,139,100,0.8));
        rectangle_0.setStrokeWidth(2);
        Button button_0 = new Button();
        setButtons(button_0, producerType.getTypeOfResource0());
        vBox_0.getChildren().addAll(button_0, rectangle_0);
        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.CENTER);
        vBox1.setSpacing(10);
        Rectangle rectangle1 = new Rectangle(20, 10);
        rectangle1.setStroke(Color.rgb(170,139,100,0.8));
        rectangle1.setStrokeWidth(2);
        rectangle1.setFill(Color.TRANSPARENT);
        Button button1 = new Button();
        button1.setOpacity(0.4);
        setButtons(button1, producerType.getTypeOfResource1());
        vBox1.getChildren().addAll(button1, rectangle1);
        VBox vBox_1 = new VBox();
        vBox_1.setAlignment(Pos.CENTER);
        vBox_1.setSpacing(10);
        Rectangle rectangle_1 = new Rectangle(20, 10);
        rectangle_1.setStroke(Color.rgb(170,139,100,0.8));
        rectangle_1.setStrokeWidth(2);
        rectangle_1.setFill(Color.TRANSPARENT);
        Button button_1 = new Button();
        setButtons(button_1 ,producerType.getTypeOfResource1());
        vBox_1.getChildren().addAll(button_1, rectangle_1);
        HBox.setMargin(vBox1, new Insets(0, 0, 0,60));
        getResourceState((Producer) building ,rectangle0, rectangle_0, rectangle1, rectangle_1);
        holder.getChildren().addAll(vBox0, vBox_0);
        if (producerType.getTypeOfResource1() != null) holder.getChildren().addAll(vBox1, vBox_1);
        buildingInformationHolder.getChildren().add(holder);
        changeMode(rectangle0, rectangle_0, rectangle1, rectangle_1, (Producer) building);
        buildingInformationHolder.setLayoutY(140);
        buildingInformationHolder.setLayoutX(570);
    }

    public void getResourceState(Producer producer, Rectangle rectangle0, Rectangle rectangle_0, Rectangle rectangle1, Rectangle rectangle_1) {
        rectangle_0.setFill(Color.TRANSPARENT); rectangle_1.setFill(Color.TRANSPARENT);
        rectangle1.setFill(Color.TRANSPARENT); rectangle0.setFill(Color.TRANSPARENT);
        if (producer.getMode().equals(ProduceMode.FIRST)) {
            rectangle_0.setFill(Color.GOLD);
            rectangle1.setFill(Color.GOLD);
        }
        else if(producer.getMode().equals(ProduceMode.SECOND)) {
            rectangle0.setFill(Color.GOLD);
            rectangle_1.setFill(Color.GOLD);
        }
        else if(producer.getMode().equals(ProduceMode.BOTH)) {
            rectangle_0.setFill(Color.GOLD);
            rectangle_1.setFill(Color.GOLD);
        }
        else if (producer.getMode().equals(ProduceMode.NON_ACTIVE)) {
            rectangle0.setFill(Color.GOLD);
            rectangle1.setFill(Color.GOLD);
        }
    }

    public void changeMode(Rectangle rectangle0, Rectangle rectangle_0, Rectangle rectangle1, Rectangle rectangle_1, Producer producer) {
        HashMap<String, String> options = new HashMap<>();
        rectangle0.setOnMouseClicked(mouseEvent -> {
            if (rectangle_0.getFill().equals(Color.GOLD) && rectangle1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.NON_ACTIVE.name());
            else if (rectangle_1.getFill().equals(Color.GOLD) && rectangle0.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.SECOND.name());
            else if (rectangle_0.getFill().equals(Color.GOLD) && rectangle_1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.SECOND.name());
            else if (rectangle0.getFill().equals(Color.GOLD) && rectangle1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.NON_ACTIVE.name());
            String result = buildingController.setMode(options);
            if (result != null) madeResourcePopUp(result);
            getResourceState(producer, rectangle0, rectangle_0, rectangle1, rectangle_1);
        });
        rectangle_0.setOnMouseClicked(mouseEvent -> {
            if (rectangle_0.getFill().equals(Color.GOLD) && rectangle1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.FIRST.name());
            else if (rectangle_1.getFill().equals(Color.GOLD) && rectangle0.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.BOTH.name());
            else if (rectangle_0.getFill().equals(Color.GOLD) && rectangle_1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.BOTH.name());
            else if (rectangle0.getFill().equals(Color.GOLD) && rectangle1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.FIRST.name());
            buildingController.setMode(options);
            String result = buildingController.setMode(options);
            if (result != null) madeResourcePopUp(result);
            getResourceState(producer, rectangle0, rectangle_0, rectangle1, rectangle_1);
        });
        rectangle1.setOnMouseClicked(mouseEvent -> {
            if (rectangle_0.getFill().equals(Color.GOLD) && rectangle1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.FIRST.name());
            else if (rectangle_1.getFill().equals(Color.GOLD) && rectangle0.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.NON_ACTIVE.name());
            else if (rectangle_0.getFill().equals(Color.GOLD) && rectangle_1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.FIRST.name());
            else if (rectangle0.getFill().equals(Color.GOLD) && rectangle1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.NON_ACTIVE.name());
            buildingController.setMode(options);
            String result = buildingController.setMode(options);
            if (result != null) madeResourcePopUp(result);
            getResourceState(producer, rectangle0, rectangle_0, rectangle1, rectangle_1);
        });
        rectangle_1.setOnMouseClicked(mouseEvent -> {
            if (rectangle_0.getFill().equals(Color.GOLD) && rectangle1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.BOTH.name());
            else if (rectangle_1.getFill().equals(Color.GOLD) && rectangle0.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.SECOND.name());
            else if (rectangle_0.getFill().equals(Color.GOLD) && rectangle_1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.BOTH.name());
            else if (rectangle0.getFill().equals(Color.GOLD) && rectangle1.getFill().equals(Color.GOLD)) options.put("m", ProduceMode.SECOND.name());
            buildingController.setMode(options);
            String result = buildingController.setMode(options);
            if (result != null) madeResourcePopUp(result);
            getResourceState(producer, rectangle0, rectangle_0, rectangle1, rectangle_1);
        });
    }

    public void madeResourcePopUp(String result) {
        VBox popUp = new VBox();
        Button ok = new Button();
        Pane mapDesignMenu = (Pane) mainPane.getParent().getParent();
        style.popUp0(mainPane, popUp, ok, 20, 20, 450, 70, 180, 50, 100, 15,500, 100, result, 20);
        popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
        mapDesignMenu.getChildren().get(0).setDisable(true);
        for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
        ok.setOnMouseClicked(mouseEvent2 -> {
            mainPane.getChildren().remove(popUp);
            mapDesignMenu.getChildren().get(0).setDisable(false);
            for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
        });
        updateBalance();
    }

    public void setButtons(Button button, Enum<?> type) {
        button.setPrefSize(50, 50);
        button.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        BackgroundSize backgroundSize = new BackgroundSize(50, 50, false, false, false, false);
        try {button.setBackground(new Background(new BackgroundImage(((ResourceType) type).getTexture(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize)));}
        catch (Exception ignored) {
            try {button.setBackground(new Background(new BackgroundImage(((Food) type).getTexture(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize)));}
            catch (Exception ignored0) {button.setBackground(new Background(new BackgroundImage(((Weapons) type).getTexture(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize)));}
        }
    }

    public void runShop(Pane buildingInformationHolder) {
        HBox holder = new HBox();
        holder.setAlignment(Pos.CENTER);
        holder.setSpacing(60);
        HBox resourceTypeHolder = new HBox();
        resourceTypeHolder.setSpacing(60);
        Rectangle resource = new Rectangle(70, 70);
        resource.setOpacity(0.8);
        resource.setFill(new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/shop-B1.png").toExternalForm())));
        Rectangle food = new Rectangle(70, 70);
        food.setOpacity(0.8);
        food.setFill(new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/shop-B2.png").toExternalForm())));
        Rectangle weapon = new Rectangle(70, 70);
        weapon.setOpacity(0.8);
        weapon.setFill(new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/shop-B3.png").toExternalForm())));
        Rectangle trade = new Rectangle(70, 70);
        trade.setOpacity(0.8);
        trade.setFill(new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/shop-B4.png").toExternalForm())));
        resourceTypeHolder.getChildren().addAll(resource, food, weapon, trade);
        buildingInformationHolder.setLayoutY(138);
        buildingInformationHolder.setLayoutX(530);
        holder.getChildren().add(resourceTypeHolder);
        buildingInformationHolder.getChildren().add(holder);
        resource.setOnMouseClicked(mouseEvent -> {
            holder.getChildren().clear();
            imageLoader(buildingInformationHolder, holder, 0);
        });
        food.setOnMouseClicked(mouseEvent -> {
            holder.getChildren().clear();
            imageLoader(buildingInformationHolder,holder, 1);
        });
        weapon.setOnMouseClicked(mouseEvent -> {
            holder.getChildren().clear();
            imageLoader(buildingInformationHolder,holder, 2);
        });
        trade.setOnMouseClicked(mouseEvent -> {
            holder.getChildren().clear();
            runTradeMenu(buildingInformationHolder);
        });
    }

    private void imageLoader(Pane buildingInformationHolder,HBox hboxHolder, int type) {
        HBox imageLoader = new HBox();
        ScrollPane scrollPane = new ScrollPane(imageLoader);
        scrollPane.setBlendMode(BlendMode.DARKEN);
        scrollPane.setMaxWidth(230);
        scrollPane.setPrefHeight(100);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        buildingInformationHolder.setLayoutX(530);
        HBox emptyBox = new HBox();
        switch (type) {
            case 0 : for (ResourceType resourceType : ResourceType.values()) {
                        VBox vBox = new VBox();
                        vBox.setAlignment(Pos.CENTER);
                        vBox.setSpacing(10);
                        Rectangle rectangle = new Rectangle(40,40);
                        rectangle.setStyle("-fx-stroke: transparent; -fx-stroke-width: 2;");
                        rectangle.setFill(new ImagePattern(resourceType.getTexture()));
                        Label value = new Label("0");
                        buildingMenuController.getResourceAmount(gameMap, value, resourceType);
                        value.setFont(style.Font0(15));
                        value.setTextFill(Color.BLACK);
                        vBox.getChildren().addAll(rectangle, value);
                        imageLoader.setSpacing(20);
                        imageLoader.getChildren().add(vBox);
                    }
                    break;
            case 1 : for (Food food : Food.values()) {
                        VBox vBox = new VBox();
                        vBox.setAlignment(Pos.CENTER);
                        vBox.setSpacing(10);
                        Rectangle rectangle = new Rectangle(40,40);
                        rectangle.setStyle("-fx-stroke: transparent; -fx-stroke-width: 2;");
                        rectangle.setFill(new ImagePattern(food.getTexture()));
                        Label value = new Label("0");
                        buildingMenuController.getFoodAmount(gameMap, value, food);
                        value.setFont(style.Font0(15));
                        value.setTextFill(Color.BLACK);
                        vBox.getChildren().addAll(rectangle, value);
                        imageLoader.setSpacing(20);
                        imageLoader.getChildren().add(vBox);
                    }
                    break;
            case 2 : for (Weapons weapons : Weapons.values()) {
                        VBox vBox = new VBox();
                        vBox.setAlignment(Pos.CENTER);
                        vBox.setSpacing(10);
                        Rectangle rectangle = new Rectangle(40,40);
                        rectangle.setStyle("-fx-stroke: transparent; -fx-stroke-width: 2;");
                        rectangle.setFill(new ImagePattern(weapons.getTexture()));
                        Label value = new Label("0");
                        buildingMenuController.getWeaponAmount(gameMap, value, weapons);
                        value.setFont(style.Font0(15));
                        value.setTextFill(Color.BLACK);
                        vBox.getChildren().addAll(rectangle, value);
                        imageLoader.setSpacing(20);
                        imageLoader.getChildren().add(vBox);
                    }
                    break;
        }
        handleButtonAndPrices(emptyBox, imageLoader, type);
        hboxHolder.getChildren().addAll(scrollPane, emptyBox);
    }

    public void handleButtonAndPrices(HBox emptyBox, HBox imageLoader, int type) {
        emptyBox.setAlignment(Pos.CENTER);
        emptyBox.setSpacing(20);
        Label sell = new Label("SELL : 0");
        sell.setFont(style.Font0(15));
        Label buy = new Label("BUY : 0");
        buy.setFont(style.Font0(15));
        Button sell_B = new Button();
        sell_B.setFont(style.Font0(15));
        style.button0(sell_B, "sell", 100, 20);
        Button buy_B = new Button();
        buy_B.setFont(style.Font0(15));
        style.button0(buy_B, "buy", 100, 20);
        VBox buttonHolder = new VBox();
        buttonHolder.getChildren().addAll(sell_B, buy_B);
        buttonHolder.setSpacing(10);
        VBox values = new VBox();
        values.getChildren().addAll(sell, buy);
        values.setSpacing(25);
        emptyBox.getChildren().addAll(values, buttonHolder);
        for (Node node : imageLoader.getChildren()) {
            ((VBox)node).getChildren().get(0).setOnMouseClicked(mouseEvent -> {
                for (Node node0 : imageLoader.getChildren()) {
                    ((VBox)node0).getChildren().get(0).setStyle("-fx-stroke: transparent; -fx-stroke-width: 2;");
                }
                ((VBox)node).getChildren().get(0).setStyle("-fx-stroke: #3d3b18; -fx-stroke-width: 2;");
                buildingMenuController.getSellOrBuyPrice((Rectangle)((VBox)node).getChildren().get(0), sell, buy);
            });
        }
        sellAndBuyProceed(sell_B, imageLoader, type, true);
        sellAndBuyProceed(buy_B, imageLoader, type, false);
    }
    public void sellAndBuyProceed(Button command, HBox imageLoader, int type, boolean sellOrBuy) {
        command.setOnMouseClicked(mouseEvent -> {
            String result = null;
            for (Node node : imageLoader.getChildren()) {
                if (!((Rectangle)((VBox) node).getChildren().get(0)).getStroke().equals(Color.TRANSPARENT)) {
                    if (sellOrBuy) result = buildingController.sellFromShop(buildingMenuController.getBuyOrSellType((Rectangle)((VBox) node).getChildren().get(0)));
                    else result = buildingController.buyFromShop(buildingMenuController.getBuyOrSellType((Rectangle)((VBox) node).getChildren().get(0)));
                    if (result.equals("done")) {
                        updateValues(type, (Label) ((VBox) node).getChildren().get(1), (Rectangle) ((VBox) node).getChildren().get(0));
                    }
                    break;
                }
            }
            if (result == null) result = "Please choose a good to buy or sell!";
            else if (result.equals("done")) result = "Command has done successfully!";
            VBox popUp = new VBox();
            Button ok = new Button();
            Pane mapDesignMenu = (Pane) mainPane.getParent().getParent();
            style.popUp0(mainPane, popUp, ok, 20, 20, 450, 70, 180, 50, 100, 15,500, 100, result, 20);
            popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
            mapDesignMenu.getChildren().get(0).setDisable(true);
            for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
            ok.setOnMouseClicked(mouseEvent2 -> {
                mainPane.getChildren().remove(popUp);
                mapDesignMenu.getChildren().get(0).setDisable(false);
                for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
            });
            updateBalance();
        });
    }
    public void updateBalance() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        coinValue.setText(decimalFormat.format(gameMap.getKingdomByOwner(Controller.currentUser).getBalance()));
    }
    public void updateValues(int type, Label value, Rectangle rectangle) {
        switch (type) {
            case 0 : buildingMenuController.getResourceAmount(gameMap, value,ResourceType.valueOf(buildingMenuController.getBuyOrSellType(rectangle)));
            break;
            case 1 : buildingMenuController.getFoodAmount(gameMap, value,Food.valueOf(buildingMenuController.getBuyOrSellType(rectangle)));
            break;
            case 2 : buildingMenuController.getWeaponAmount(gameMap, value,Weapons.valueOf(buildingMenuController.getBuyOrSellType(rectangle)));
            break;
        }

    }

    private void runTradeMenu(Pane buildingInformationHolder) {
        HBox holder = new HBox();
        holder.setAlignment(Pos.CENTER);
        holder.setSpacing(15);
        VBox flags = new VBox();
        Rectangle flagColor = new Rectangle(40, 40);
        flagHandel(flags, flagColor);

        VBox resource = new VBox();
        Rectangle resourceImage = new Rectangle(40, 40);
        resourceHandle(resource, resourceImage);

        HBox fields = new HBox();
        TextField price = new TextField();
        TextField value = new TextField();
        TextField message = new TextField();
        priceAndValue(fields, price, value, message);

        VBox buttonHolder = new VBox();
        Button request = new Button();
        request.setOnMouseClicked(mouseEvent -> {
            String result = buildingMenuController.requestAndDonateManger(gameMap, flagColor, resourceImage, price, value, message);
            if (result.equals("done")) result= "Request has sent successfully!";
            VBox popUp = new VBox();
            Button ok = new Button();
            Pane mapDesignMenu = (Pane) mainPane.getParent().getParent();
            style.popUp0(mainPane, popUp, ok, 20, 20, 450, 70, 180, 50, 100, 15,500, 100, result, 20);
            popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
            mapDesignMenu.getChildren().get(0).setDisable(true);
            for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
            ok.setOnMouseClicked(mouseEvent2 -> {
                mainPane.getChildren().remove(popUp);
                mapDesignMenu.getChildren().get(0).setDisable(false);
                for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
            });
        });
        Button donate = new Button();
        donate.setOnMouseClicked(mouseEvent -> {
            price.setText("0");
            String result = buildingMenuController.requestAndDonateManger(gameMap, flagColor, resourceImage, price, value, message);
            if (result.equals("done")) result= "Request has sent successfully!";
            VBox popUp = new VBox();
            Button ok = new Button();
            Pane mapDesignMenu = (Pane) mainPane.getParent().getParent();
            style.popUp0(mainPane, popUp, ok, 20, 20, 450, 70, 180, 50, 100, 15,500, 100, result, 20);
            popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
            mapDesignMenu.getChildren().get(0).setDisable(true);
            for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
            ok.setOnMouseClicked(mouseEvent2 -> {
                mainPane.getChildren().remove(popUp);
                mapDesignMenu.getChildren().get(0).setDisable(false);
                for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
            });
        });
        requestAndDonate(buttonHolder, request, donate);

        Button notification = new Button();
        BackgroundSize backgroundSize = new BackgroundSize(40, 40, false, false, false, false);
        Image image;
        if (gameMap.getKingdomByOwner(Controller.currentUser).getNotification().size() == 0) image = new Image(BuildingMenu.class.getResource("/images/buttons/silent.png").toExternalForm());
        else {
            handleNotifications(notification);
            image = new Image(BuildingMenu.class.getResource("/images/buttons/notification.png").toExternalForm());
        }
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        notification.setBackground(new Background(backgroundImage));
        notification.setPrefSize(40, 40);
        Button history = new Button();
        handleHistory(history);
        BackgroundImage backgroundImage0 = new BackgroundImage(new Image(BuildingMenu.class.getResource("/images/buttons/history.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        history.setBackground(new Background(backgroundImage0));
        history.setPrefSize(40, 40);
        VBox buttonHolderVbox = new VBox();
        buttonHolderVbox.setAlignment(Pos.CENTER);
        buttonHolderVbox.getChildren().addAll(notification, history);
        holder.getChildren().addAll(flags, resource, fields, buttonHolder, buttonHolderVbox);
        buildingInformationHolder.getChildren().add(holder);
        buildingInformationHolder.setLayoutY(138);
        buildingInformationHolder.setLayoutX(510);
    }

    public void flagHandel(VBox flags, Rectangle flagColor) {
        flags.setAlignment(Pos.CENTER);
        flags.setSpacing(10);
        HBox toggleFlag = new HBox();
        toggleFlag.setSpacing(10);
        Rectangle rightFlag = new Rectangle(15, 15, new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/right.png").toExternalForm())));
        Rectangle leftFlag = new Rectangle(15, 15, new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/left.png").toExternalForm())));
        toggleFlag.getChildren().addAll(rightFlag, leftFlag);
        Color firstColor = null;
        for (Kingdom kingdom : gameMap.getPlayers())
            if (!kingdom.getOwner().equals(Controller.currentUser)) {
                firstColor = kingdom.getFlag().getFlagColor();
                break;
            }
        flagColor.setFill(firstColor);
        flagColor.setOpacity(0.7);
        flags.getChildren().addAll(flagColor, toggleFlag);
        rightFlag.setOnMouseClicked(mouseEvent -> {
            int userNumber = -1;
            for (int i = 0 ; i < gameMap.getPlayers().size() ; i++) {
                if (gameMap.getPlayers().get(i).getFlag().getFlagColor().equals(flagColor.getFill())) {
                    userNumber = i;
                    break;
                }
            }
            if (!gameMap.getPlayers().get((userNumber + 1) % gameMap.getPlayers().size()).getOwner().equals(Controller.currentUser))
                flagColor.setFill(gameMap.getPlayers().get((userNumber + 1) % gameMap.getPlayers().size()).getFlag().getFlagColor());
        });
        leftFlag.setOnMouseClicked(mouseEvent -> {
            int userNumber = 0;
            for (int i = 0 ; i < gameMap.getPlayers().size() ; i++) {
                if (gameMap.getPlayers().get(i).getFlag().getFlagColor().equals(flagColor.getFill())) {
                    userNumber = i;
                    break;
                }
            }
            if (!gameMap.getPlayers().get((userNumber - 1 + gameMap.getPlayers().size()) % gameMap.getPlayers().size()).getOwner().equals(Controller.currentUser))
                flagColor.setFill(gameMap.getPlayers().get((userNumber - 1 + gameMap.getPlayers().size()) % gameMap.getPlayers().size()).getFlag().getFlagColor());
        });
    }

    public void resourceHandle(VBox resource,  Rectangle resourceImage) {
        resource.setAlignment(Pos.CENTER);
        ResourceType[] resourceTypes = new ResourceType[10];
        int counter = 0;
        for (ResourceType resourceType : ResourceType.values()) {
            resourceTypes[counter] = resourceType;
            counter++;
        }
        resource.setSpacing(10);
        HBox toggleResource = new HBox();
        toggleResource.setSpacing(10);
        Rectangle rightResource = new Rectangle(15, 15, new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/right.png").toExternalForm())));
        Rectangle leftResource = new Rectangle(15, 15, new ImagePattern(new Image(BuildingMenu.class.getResource("/images/buttons/left.png").toExternalForm())));
        toggleResource.getChildren().addAll(rightResource, leftResource);
        resourceImage.setFill(new ImagePattern(resourceTypes[0].getTexture()));
        resource.getChildren().addAll(resourceImage, toggleResource);
        rightResource.setOnMouseClicked(mouseEvent -> {
            int resourceNumber = 0;
            for (int i = 0 ; i < resourceTypes.length ; i++) {
                if (resourceTypes[i].getTexture().equals(((ImagePattern)resourceImage.getFill()).getImage())) {
                    resourceNumber = i;
                    break;
                }
            }
            resourceImage.setFill(new ImagePattern(resourceTypes[(resourceNumber + 1) % 10].getTexture()));
        });
        leftResource.setOnMouseClicked(mouseEvent -> {
            int resourceNumber = 0;
            for (int i = 0 ; i < resourceTypes.length ; i++) {
                if (resourceTypes[i].getTexture().equals(((ImagePattern)resourceImage.getFill()).getImage())) {
                    resourceNumber = i;
                    break;
                }
            }
            resourceImage.setFill(new ImagePattern(resourceTypes[(resourceNumber + 9) % 10].getTexture()));
        });
    }
    public void priceAndValue(HBox fields,TextField price, TextField value, TextField message) {
        fields.setSpacing(5);
        VBox priceAndValue = new VBox();
        priceAndValue.setSpacing(5);
        price.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*") || t1.length() > 3) price.setText(s);
        });
        price.setAlignment(Pos.CENTER);
        style.textFiled0(price, "Price", 50, 35);
        price.setFont(style.Font0(12));
        value.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("\\d*") || t1.length() > 3) value.setText(s);
        });
        value.setAlignment(Pos.CENTER);
        style.textFiled0(value, "Value", 50, 35);
        value.setFont(style.Font0(12));
        priceAndValue.getChildren().addAll(value, price);
        style.textFiled0(message, "Your Massage", 200, 75);
        message.setFont(style.Font0(20));
        fields.getChildren().addAll(priceAndValue, message);
    }

    public void requestAndDonate(VBox buttonHolder, Button request, Button donate) {
        buttonHolder.setSpacing(10);
        request.setFont(style.Font0(15));
        style.button0(request, "request", 100, 20);
        donate.setFont(style.Font0(15));
        style.button0(donate, "donate", 100, 20);
        buttonHolder.getChildren().addAll(request, donate);
    }

    public void handleNotifications(Button notification) {
        notification.setOnMouseClicked(mouseEvent -> {
            VBox popUp = new VBox();
            popUp.setViewOrder(-2);
            Button ok = new Button();
            Pane mapDesignMenu = (Pane) mainPane.getParent().getParent();
            style.popUp0(mainPane, popUp, ok, 10, 10, 450, 125, 180, 50, 100, 15,500, 100, "", 20);
            popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
            popUp.getChildren().clear();
            mapDesignMenu.getChildren().get(0).setDisable(true);
            VBox tradeBoxHolder = new VBox();
            tradeBoxHolder.setSpacing(7);
            ScrollPane scrollPane = new ScrollPane(tradeBoxHolder);
            scrollPane.setPrefSize(410, 120);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background : transparent;");
            scrollPane.setBlendMode(BlendMode.DARKEN);
            for (Trade trades : gameMap.getKingdomByOwner(Controller.currentUser).getNotification()) {
                VBox tradeBox = new VBox();
                tradeBox.setAlignment(Pos.CENTER);
                tradeBox.setSpacing(10);
                HBox hBox0 = new HBox();
                hBox0.setAlignment(Pos.CENTER);
                tradeBox.setAlignment(Pos.CENTER);
                tradeBox.setPrefSize(400, 100);
                Label text0 = new Label(trades.getResourceAmount() + " ");
                text0.setTextFill(Color.rgb(170,139,100,0.8));
                text0.setFont(style.Font0(18));
                Rectangle rectangle = new Rectangle(30, 30, new ImagePattern(trades.getResourceType().getTexture()));
                Label text1 = new Label();
                Label text2 = new Label();
                TextField acceptMessage = new TextField();
                style.textFiled0(acceptMessage, "Your Message", 200, 40);
                acceptMessage.setBackground(Background.EMPTY);
                acceptMessage.setFont(style.Font0(15));
                acceptMessage.setBorder(Border.EMPTY);
                acceptMessage.setStyle("-fx-text-box-border : rgba(170,139,100,0.8); -fx-background-radius: 10;");
                Button accept = new Button();
                accept.setOnMouseClicked(mouseEvent1 -> {
                    if (acceptMessage.getText().equals("")) acceptMessage.setText("Request Accepted by " + trades.getUserReceiver().getUserName());
                    buildingMenuController.acceptRequest(style, mainPane, mapDesignMenu, popUp, gameMap, acceptMessage, trades.getId());
                    updateBalance();
                });
                accept.setFont(style.Font0(12));
                style.button0(accept, "accept", 100, 20);
                HBox hBox1 = new HBox();
                hBox1.setAlignment(Pos.CENTER);
                hBox1.setSpacing(20);

                text1.setFont(style.Font0(18));
                text1.setTextFill(Color.rgb(170,139,100,0.8));
                if (trades.getUserSender().equals(Controller.currentUser)) {
                    text1.setText(" : \" "+ trades.getMassageAccept() + " \"");
                    text2.setText("Price : " + trades.getPrice() + "  State : --ACCEPTED--");
                    text2.setFont(style.Font0(18));
                    text2.setTextFill(Color.rgb(170,139,100,0.8));
                    hBox1.getChildren().add(text2);

                }
                else {
                    text1.setText(" Price : " + trades.getPrice() + " \" "+ trades.getMassageRequest() + " \"");
                    text2.setText(" " + trades.getPrice() + "  State : --REQUESTED--");
                    hBox1.getChildren().addAll(acceptMessage, accept);
                }
                hBox0.getChildren().addAll(text0, rectangle, text1);


                tradeBox.getChildren().addAll(hBox0, hBox1);
                tradeBox.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
                tradeBoxHolder.getChildren().add(tradeBox);
            }
            popUp.getChildren().add(0, scrollPane);
            for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
            popUp.setOnMouseClicked(mouseEvent2 -> {
                mainPane.getChildren().remove(popUp);
                mapDesignMenu.getChildren().get(0).setDisable(false);
                for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
            });
            updateBalance();
        });
    }

    public void handleHistory(Button history) {
        history.setOnMouseClicked(mouseEvent -> {
            VBox popUp = new VBox();
            popUp.setViewOrder(-2);
            Button ok = new Button();
            Pane mapDesignMenu = (Pane) mainPane.getParent().getParent();
            style.popUp0(mainPane, popUp, ok, 10, 10, 450, 125, 180, 50, 100, 15,500, 100, "", 20);
            popUp.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
            popUp.getChildren().clear();
            mapDesignMenu.getChildren().get(0).setDisable(true);
            VBox tradeBoxHolder = new VBox();
            tradeBoxHolder.setSpacing(7);
            ScrollPane scrollPane = new ScrollPane(tradeBoxHolder);
            scrollPane.setPrefSize(410, 120);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background : transparent;");
            scrollPane.setBlendMode(BlendMode.DARKEN);
            for (Trade trades : gameMap.getKingdomByOwner(Controller.currentUser).getHistoryTrade()) {
                VBox tradeBox = new VBox();
                tradeBox.setAlignment(Pos.CENTER);
                tradeBox.setSpacing(10);
                HBox hBox0 = new HBox();
                hBox0.setAlignment(Pos.CENTER);
                tradeBox.setAlignment(Pos.CENTER);
                tradeBox.setPrefSize(400, 100);
                Label text0 = new Label(trades.getResourceAmount() + " Units Of ");
                text0.setTextFill(Color.rgb(170,139,100,0.8));
                text0.setFont(style.Font0(18));
                Rectangle rectangle = new Rectangle(30, 30, new ImagePattern(trades.getResourceType().getTexture()));
                Label text1 = new Label();
                Label text2 = new Label();
                text1.setFont(style.Font0(18));
                text1.setTextFill(Color.rgb(170,139,100,0.8));
                if (trades.getUserSender().equals(Controller.currentUser)) {
                    text1.setText(" : \" "+ trades.getMassageRequest() + " \"");
                    text2.setText("Price : " + trades.getPrice() + "  State : --REQUESTED--");
                }
                else {
                    text1.setText("\" "+ trades.getMassageAccept() + " \"");
                    text2.setText("Price : " + trades.getPrice() + "  State : --ACCEPTED--");
                }
                hBox0.getChildren().addAll(text0, rectangle, text1);
                HBox hBox1 = new HBox();
                hBox1.setAlignment(Pos.CENTER);
                text2.setFont(style.Font0(18));
                text2.setTextFill(Color.rgb(170,139,100,0.8));
                hBox1.getChildren().add(text2);
                tradeBox.getChildren().addAll(hBox0, hBox1);
                tradeBox.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
                tradeBoxHolder.getChildren().add(tradeBox);
            }
            popUp.getChildren().add(0, scrollPane);
            for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
            popUp.setOnMouseClicked(mouseEvent2 -> {
                mainPane.getChildren().remove(popUp);
                mapDesignMenu.getChildren().get(0).setDisable(false);
                for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
            });
            updateBalance();
        });
    }
}
