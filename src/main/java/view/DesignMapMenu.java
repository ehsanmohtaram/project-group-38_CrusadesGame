package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    private String name;
    private Label finalName;
//    private final CommandParser commandParser;

    public DesignMapMenu() {
        menuController = new MapDesignMenuController();
        this.style = new Style();
        finalWidth = 10;
        finalHeight = 10;
        name = "new map";
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
        Label name = new Label(this.name);
        this.finalName = name;
        style.label0(name, 250 , 70);
        name.setFont(style.Font0(35));
        TextField changeName = new TextField();
        style.textFiled0(changeName, null, 250 , 70);
        changeName.setFont(style.Font0(35));
        Label width = new Label(finalWidth.toString());
        Label height = new Label(finalHeight.toString());
        Label cross = new Label("*");
        HBox size = new HBox(width, cross , height);
        size.setAlignment(Pos.CENTER);
        for (Node child : size.getChildren()) {
            ((Label) child).setFont(style.Font0(35));
            style.label0((Label) child , 80 , 70);
        }
        size.setSpacing(15);
        Button submit = new Button();
        Button back = new Button();
        style.button0(submit, "create", 150 , 50);
        style.button0(back, "back" , 150 , 50);
        submit.setFont(style.Font0(25));
        back.setFont(style.Font0(25));
        HBox buttons = new HBox(submit, back);
        buttons.setSpacing(10);
        VBox details = new VBox(name, size, buttons);
        details.setSpacing(15);

        changeAttributes(width, height, size);
        processNameChange(name, changeName, details);
        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                startGame(pane);
            }
        });
        details.setAlignment(Pos.CENTER);
        details.setLayoutX(1000);
        details.setLayoutY(300);
        pane.getChildren().add(details);

    }

    private void startGame(Pane pane) {
        menuController.getInfoFromMenu(stage, finalWidth, finalHeight, finalName);
        menuController.createNewMap();
    }

    private void changeAttributes(Label width, Label height, HBox size) {
        width.setOnMouseEntered(e -> {
            width.requestFocus();
            width.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyEvent.getCode().getName().equals("Right")){
                        if(finalWidth < 100) {
                            finalWidth += 5;
                            width.setText(finalWidth.toString());
                        }
                    }
                    if(keyEvent.getCode().getName().equals("Left")){
                        if(finalWidth > 20) {
                            finalWidth -= 5;
                            width.setText(finalWidth.toString());
                        }
                    }
                }
            });
        });
        width.setOnMouseExited(e -> {
            size.requestFocus();
        });
        height.setOnMouseEntered(e -> {
            height.requestFocus();
            height.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if(keyEvent.getCode().getName().equals("Right")){
                        if(finalHeight < 100) {
                            finalHeight += 5;
                            height.setText(finalHeight.toString());
                        }
                    }
                    if(keyEvent.getCode().getName().equals("Left")){
                        if(finalHeight > 20) {
                            finalHeight -= 5;
                            height.setText(finalHeight.toString());
                        }
                    }
                }
            });
        });
        height.setOnMouseExited(e -> {
            size.requestFocus();
        });

    }

    public void processNameChange(Label name, TextField changeName, VBox details){
        name.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2){
                    details.getChildren().remove(name);
                    details.getChildren().add(0, changeName);
                    changeName.requestFocus();
                    changeName.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent keyEvent) {
                            if(keyEvent.getCode().getName().equals("Enter")){
                                name.setText(changeName.getText());
                                details.getChildren().remove(changeName);
                                details.getChildren().add(0, name);
                                details.requestFocus();
                            }
                        }
                    });
                }
            }
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
