package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view.controller.MapDesignMenuController;

import java.util.Objects;

public class DesignMapMenu extends Application {
    private final MapDesignMenuController menuController;
    private Stage stage;
    private Style style;
    private Integer finalWidth;

    private Integer finalHeight;
//    private final CommandParser commandParser;

    public DesignMapMenu() {
        menuController = new MapDesignMenuController();
        this.style = new Style();
        finalWidth = 60;
        finalHeight = 60;
//        commandParser = new CommandParser();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setResizable(false);
        Pane pane = new Pane();
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(Objects.requireNonNull(LoginMenu.class.getResource("/images/background/designMenu.jpg")).toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        createMenu(pane);
        stage.getScene().setRoot(pane);
        stage.setTitle("design Menu");
        stage.show();
    }

    private void createMenu(Pane pane) {
        Label name = new Label("new map");
        style.label0(name, 250 , 70);
        name.setAlignment(Pos.CENTER);
        name.setFont(style.Font0(35));
        Label width = new Label(finalWidth.toString());
//        width.setFont(style.Font0(35));
        Label height = new Label(finalHeight.toString());
//        height.setFont(style.Font0(35));
        Label cross = new Label("*");
//        cross.setFont(style.Font0(35));
        HBox size = new HBox(width, cross , height);
        for (Node child : size.getChildren()) {
            ((Label) child).setFont(style.Font0(35));
            style.label0((Label) child , 80 , 70);
        }
        size.setAlignment(Pos.CENTER);
        size.setSpacing(15);
        VBox details = new VBox(name, size);
        details.setSpacing(15);
        changeAttributes(width, height);
//        details.setAlignment(Pos.CENTER);
        details.setLayoutX(1000);
        details.setLayoutY(300);
        pane.getChildren().add(details);

    }

    private void changeAttributes(Label width, Label height) {
        width.setOnMouseEntered(e -> {
            width.requestFocus();
            width.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyEvent.getCode().getName().equals("Right")){
                        finalWidth += 5;
                        width.setText(finalWidth.toString());
                    }
                    if(keyEvent.getCode().getName().equals("Left")){
                        finalWidth -= 5;
                        width.setText(finalWidth.toString());
                    }
                }
            });
        });
        width.setOnMouseExited(e -> {
            width.getParent().requestFocus();
        });
        height.setOnMouseEntered(e -> {
            height.requestFocus();
            height.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyEvent.getCode().getName().equals("Right")){
                        finalWidth += 5;
                        height.setText(finalWidth.toString());
                    }
                    if(keyEvent.getCode().getName().equals("Left")){
                        finalWidth -= 5;
                        height.setText(finalWidth.toString());
                    }
                }
            });
        });
        height.setOnMouseExited(e -> {
            height.getParent().requestFocus();
        });
    }

    public String run(){
//        System.out.println("You can design your map here. When your map is ready input 'start game'");
//        HashMap<String , String> options;
//        String input, result;
//        while (true) {
//            input = CommandParser.getScanner().nextLine();
//            if ((options = commandParser.validate(input,"set texture",
//                    "x|positionX/y|positionY/x1/y1/x2/y2/t|type")) != null)
//                System.out.println(mapDesignController.setTexture(options));
//            else if ((options = commandParser.validate(input,"clear","x|positionX/y|positionY")) != null) {
//                System.out.println(mapDesignController.clear(options));
//            }else if ((options = commandParser.validate(input,"drop rock","x|positionX/y|positionY/d|direction")) != null) {
//                System.out.println(mapDesignController.dropRock(options));
//            }else if ((options = commandParser.validate(input,"drop tree","x|positionX/y|positionY/t|type")) != null) {
//                System.out.println(mapDesignController.dropTree(options));
//            }else if ((options = commandParser.validate(input,"add user","x|positionX/y|positionY/u|user/f|flag")) != null) {
//                System.out.println(mapDesignController.addUserToMap(options));
//            }else if ((options = commandParser.validate(input,"drop building","x|positionX/y|positionY/t|type/f|flag")) != null) {
//                System.out.println(mapDesignController.dropBuilding(options));
//            }else if ((options = commandParser.validate(input,"drop unit","x|positionX/y|positionY/t|type/f|flag/c|count")) != null) {
//                System.out.println(mapDesignController.dropUnit(options));
//            }else if ((options = commandParser.validate(input,"show map","x|positionX/y|positionY")) != null) {
//                result = mapDesignController.showMap(options);
//                System.out.println(result);
//                if(result.matches("map:\\sin[\\S\\s]+")) {
//                    System.out.println("you entered map menu");
//                    return "map";
//                }
//            }else if(commandParser.validate(input,"start game",null) != null) {
//                result = mapDesignController.startPlaying();
//                if (!result.equals("start")) System.out.println(result);
//                else return "start";
//            } else if (commandParser.validate(input,"show current menu",null) != null)
//                System.out.println("design map Menu");
//            else System.out.println("invalid command");
//        }
        return null;
    }


}
