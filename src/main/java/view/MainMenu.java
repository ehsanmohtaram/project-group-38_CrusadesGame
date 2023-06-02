package view;

import controller.Controller;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainMenu extends Application {
    private final Style style;
    private Stage stage;
    public MainMenu() {
        style = new Style();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setFullScreen(true);
        stage.setResizable(false);
        Pane pane = new Pane();
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(SignUpMenu.class.getResource("/images/background/loginBack.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        stage.getScene().setRoot(pane);
        mainInfo(pane);
        stage.setTitle("Main Menu");
        stage.show();
    }

    public void mainInfo(Pane pane) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(50);
        Button newMap = new Button();
        style.button0(newMap, "New Map", 400, 80);
        newMap.setFont(style.Font0(25));
        Button defaultMap = new Button();
        style.button0(defaultMap, "Default Map", 400, 80);
        defaultMap.setFont(style.Font0(25));
        Button profileMenu = new Button();
        style.button0(profileMenu, "Profile", 400, 80);
        profileMenu.setFont(style.Font0(25));
        Button logout = new Button();
        style.button0(logout, "Logout", 400, 80);
        logout.setFont(style.Font0(25));
        vBox.getChildren().addAll(newMap, defaultMap, profileMenu, logout);
        vBox.setLayoutX(890);  vBox.setLayoutY(213);
        pane.getChildren().add(vBox);
        mainMenuButtonHandle(vBox);

    }

    public void mainMenuButtonHandle(VBox vBox) {
        vBox.getChildren().get(2).setOnMouseClicked(mouseEvent -> {
            try {new ProfileMenu().start(stage);}
            catch (Exception ignored) {}
        });
        vBox.getChildren().get(3).setOnMouseClicked(mouseEvent -> {
            if (Controller.currentUser.getLoggedIn()) Controller.currentUser.setLoggedIn(false);
            Controller.currentUser = Controller.loggedInUser = null;
            try {new LoginMenu().start(stage);}
            catch (Exception ignored) {}
        });
    }
    //profile/ logout/ map/

//    public String run() {
//        HashMap<String , String> options;
//        String input, result;
//        while (true) {
//            input = CommandParser.getScanner().nextLine();
//            if (commandParser.validate(input,"logout",null) != null) {
//                System.out.println("User logged out successfully!");
//                if (Controller.currentUser.getLoggedIn()) Controller.currentUser.setLoggedIn(false);
//                Controller.currentUser = null;
//                return "logout";
//            } else if (commandParser.validate(input,"profile menu",null) != null) {
//                System.out.println("Entered profile menu!");
//                return "profile";
//            } else if ((options = commandParser.validate(input,"new map","x|width/y|height/n|name")) != null) {
//                result = controller.createNewMap(options);
//                System.out.println(result);
//                if(result.equals("Map was created successfully!")) return "selectMap";
//            } else if (commandParser.validate(input,"default map",null) != null) {
//                System.out.println(controller.showDefaultMaps());
//                System.out.println("Please select one of default maps : (after selection you can still modify the map)");
//                while (true){
//                    input = CommandParser.getScanner().nextLine();
//                    if(input.equals("back")) break;
//                    result = controller.selectDefaultMap(input);
//                    System.out.println(result);
//                    if(result.equals("Map was created successfully!")) return "selectMap";
//                }
//            } else if (commandParser.validate(input,"choose from my maps",null) != null) {
//                result = controller.chooseFromMyMap();
//                System.out.println(result);
//                if (!result.equals("You do not have any map from past")) {
//                    System.out.println("Please choose one of the following maps to play : ");
//                    input = CommandParser.getScanner().nextLine();
//                    result = controller.chooseNumber(input);
//                    if (!result.equals("start")) System.out.println(result);
//                    else return "previous map";
//                }
//            }
//            else if (commandParser.validate(input,"show current menu",null) != null)
//                System.out.println("Main Menu");
//            else System.out.println("invalid command");
//        }
//    }

}
