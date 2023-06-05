package view;

import controller.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Database;
import model.User;
import view.controller.LoginMenuController;
import java.io.File;
import java.util.Random;

public class LoginMenu extends Application {
    private static boolean firstLogin = false;
    private final Style style;
    private final LoginMenuController loginMenuController;
    private Stage stage;
    private static MediaPlayer startMedia;

    public LoginMenu() {
        this.style = new Style();
        loginMenuController = new LoginMenuController();
    }

    public static void main(String[] args) {
        Database.setArrayOfUsers();
        launch(args);
        Database.updateJson();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setFullScreen(true);
        stage.setResizable(false);
        Pane pane = new Pane();
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(LoginMenu.class.getResource("/images/background/loginBack.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        loginInfo(pane);
        if (!firstLogin) {
            Scene scene = new Scene(pane, primScreenBounds.getWidth(), primScreenBounds.getHeight());
            stage.setScene(scene);
            firstLogin = true;
            //Media media = new Media(LoginMenu.class.getResource("/musics/out.mp3").toExternalForm());
            //startMedia = new MediaPlayer(media);
            //startMedia.setCycleCount(-1);
            //startMedia.play();
        }
        else stage.getScene().setRoot(pane);
        for (User user : User.users)
            if (user.getLoggedIn()) {
                Controller.currentUser = Controller.loggedInUser = user;
                new MainMenu().start(stage);
            }
        stage.setTitle("Login Menu");
        stage.show();
    }

    public void loginInfo(Pane pane) {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        TextField userName = new TextField();
        userName.setFont(style.Font0(24));
        style.textFiled0(userName,"Username" ,400, 70);
        userName.setPadding(new Insets(0,30,0,30));
        HBox passwordFiled = new HBox();
        passwordFiledDisableEnableHandle(passwordFiled);
        HBox hBox0 = new HBox();
        hBox0.setSpacing(7);
        hBox0.setAlignment(Pos.CENTER);
        Rectangle checkStayLogin = new Rectangle();
        style.checkBox0(checkStayLogin);
        Label stayLogin = new Label("Stay Login");
        stayLogin.setTextFill(Color.rgb(170,139,100,0.8));
        stayLogin.setFont(style.Font0(20));
        HBox.setMargin(stayLogin, new Insets(0, 95, 0 ,0));
        Label forgotPassword = new Label("Forgot Password?");
        forgotPassword.setTextFill(Color.rgb(170,139,100,0.8));
        forgotPassword.setFont(style.Font0(20));
        hBox0.getChildren().addAll(checkStayLogin, stayLogin, forgotPassword);
        Button login = new Button();
        VBox.setMargin(login, new Insets(100, 0, 0 ,0));
        style.button0(login, "LOGIN", 400, 60);
        login.setFont(style.Font0(20));
        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(10);
        Label newToGame = new Label("New to Game?");
        newToGame.setTextFill(Color.rgb(170,139,100,0.8));
        newToGame.setFont(style.Font0(20));
        Label signUpMenu = new Label("Create an account");
        signUpMenu.setTextFill(Color.rgb(170,139,100,0.8));
        signUpMenu.setFont(style.Font0(20));
        hBox1.getChildren().addAll(newToGame, signUpMenu);
        vBox.getChildren().addAll(userName, passwordFiled, hBox0);
        Rectangle captchaImage = new Rectangle();
        TextField captchaInput = new TextField();
        makeCaptcha(vBox, captchaImage, captchaInput);
        vBox.getChildren().addAll(login, hBox1);
        vBox.setLayoutX(890);  vBox.setLayoutY(213);
        pane.getChildren().addAll(vBox);
        hyperLinkHandel(forgotPassword, signUpMenu);
        login.setOnMouseClicked(mouseEvent -> {
            loginMenuController.getInfoFromMenu(stage, (TextField) (passwordFiled.getChildren().get(0)), userName, captchaInput, captchaImage, checkStayLogin);
            loginMenuController.checkLoginValidation();
            ImageView imageView = new ImageView(LoginMenu.class.getResource("/images/captcha/" + searchDirectory()).toExternalForm());
            captchaImage.setFill(new ImagePattern(imageView.getImage()));
        });
    }

    public void makeCaptcha(VBox vBox, Rectangle rectangle, TextField textField) {
        HBox hBox0 = new HBox();
        hBox0.setSpacing(40);
        HBox hbox1 = new HBox();
        hbox1.setStyle("-fx-background-color: rgba(86,73,57,0.3); -fx-background-radius: 10;");
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPrefSize(200, 60);
        hbox1.setBorder(new Border(new BorderStroke(Color.rgb(86,73,57,1), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        hBox0.setAlignment(Pos.CENTER);
        rectangle.setWidth(200);
        rectangle.setHeight(60);
        ImageView imageView = new ImageView(LoginMenu.class.getResource("/images/captcha/" + searchDirectory()).toExternalForm());
        rectangle.setFill(new ImagePattern(imageView.getImage()));
        rectangle.setOpacity(0.4);
        rectangle.setBlendMode(BlendMode.MULTIPLY);
        hbox1.getChildren().add(rectangle);
        textField.setFont(style.Font0(24));
        textField.setAlignment(Pos.CENTER);
        textField.setPadding(new Insets(0, 25, 0, 25));
        style.textFiled0(textField, "", 160, 60);
        hBox0.getChildren().addAll(hbox1,textField);
        vBox.getChildren().add(hBox0);
        changeCaptchaImage(rectangle);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 4) textField.setText(oldValue);
            if (newValue.matches(".*[^\\d+].*")) textField.setText(oldValue);
        });
    }

    public String searchDirectory() {
        File directory = new File("src/main/resources/images/captcha");
        File[] files = directory.listFiles();
        Random fileSize = new Random();
        int randomNumber;
        do {randomNumber = fileSize.nextInt(49);}
        while (files[randomNumber].getName().matches("\\..*"));
        return files[randomNumber].getName();
    }

    public void changeCaptchaImage(Rectangle rectangle) {
        rectangle.setOnMouseClicked(mouseEvent -> {
            ImageView imageView = new ImageView(LoginMenu.class.getResource("/images/captcha/" + searchDirectory()).toExternalForm());
            rectangle.setFill(new ImagePattern(imageView.getImage()));
        });
    }

    public void hyperLinkHandel(Label forgotPassword, Label newAccount) {
        newAccount.setOnMouseEntered(mouseEvent -> newAccount.setTextFill(Color.rgb(100, 100,100,1)));
        newAccount.setOnMouseExited(mouseEvent -> newAccount.setTextFill(Color.rgb(170,139,100,0.8)));
        newAccount.setOnMouseClicked(mouseEvent -> new SignUpMenu().start(stage));
        forgotPassword.setOnMouseEntered(mouseEvent -> forgotPassword.setTextFill(Color.rgb(100, 100,100,1)));
        forgotPassword.setOnMouseExited(mouseEvent -> forgotPassword.setTextFill(Color.rgb(170,139,100,0.8)));
    }

    public void passwordFiledDisableEnableHandle(HBox hBox) {
        hBox.setPrefSize(400, 70);
        hBox.setMaxSize(400, 70);
        PasswordField password = new PasswordField();
        password.setPrefSize(300, 70);
        password.setBackground(Background.EMPTY);
        password.setPromptText("Password");
        password.setFont(style.Font0(24));
        password.setStyle("-fx-text-fill: rgba(170,139,100,0.8); -fx-prompt-text-fill: rgba(86,73,57,0.8);");
        password.setPadding(new Insets(0,0,0,30));
        Button button = new Button();
        button.setPrefSize(100, 70);
        BackgroundSize backgroundSize = new BackgroundSize(30, 30, false, false, false, false);
        Image image = new Image(LoginMenu.class.getResource("/images/buttons/invisi.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        button.setBackground(new Background(backgroundImage));
        button.setOpacity(0.6);
        hBox.getChildren().addAll(password, button);
        hBox.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        button.setOnMouseEntered(mouseEvent -> button.setOpacity(0.2));
        button.setOnMouseExited(mouseEvent -> button.setOpacity(0.6));
        button.setOnMouseClicked(mouseEvent -> changeInvisiToVisi(hBox));
    }


    public void changeInvisiToVisi(HBox hBox) {
        TextField password;
        if (((Button) hBox.getChildren().get(1)).getBackground().getImages().get(0).getImage().getUrl()
                .equals(LoginMenu.class.getResource("/images/buttons/invisi.png").toExternalForm()))
             password = new TextField();
        else password = new PasswordField();
        String passwordString = ((TextField) hBox.getChildren().get(0)).getText();
        hBox.getChildren().remove(0);
        password.setText(passwordString);
        password.setPrefSize(300, 70);
        password.setBackground(Background.EMPTY);
        password.setPromptText("Password");
        password.setFont(style.Font0(24));
        password.setStyle("-fx-text-fill: rgba(170,139,100,0.8);; -fx-prompt-text-fill: rgba(86,73,57,0.8);");
        password.setPadding(new Insets(0,0,0,30));
        hBox.getChildren().add(0, password);
        BackgroundSize backgroundSize = new BackgroundSize(30, 30, false, false, false, false);
        Image image;
        if (((Button) hBox.getChildren().get(1)).getBackground().getImages().get(0).getImage().getUrl()
                .equals(LoginMenu.class.getResource("/images/buttons/invisi.png").toExternalForm()))
            image = new Image(LoginMenu.class.getResource("/images/buttons/visi.png").toExternalForm());
        else image = new Image(LoginMenu.class.getResource("/images/buttons/invisi.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        ((Button)hBox.getChildren().get(1)).setBackground(new Background(backgroundImage));
    }




    public void particleMaker(Pane pane) {
        Media media = new Media(LoginMenu.class.getResource("/movies/fire.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitHeight(1080);
        mediaView.setFitHeight(1920);
        mediaView.setBlendMode(BlendMode.LIGHTEN);
        mediaView.setOpacity(0.8);
        pane.getChildren().add(mediaView);
        mediaPlayer.setCycleCount(-1);
        mediaPlayer.setRate(0.3);
        mediaPlayer.play();
    }





    public String run() {
//        HashMap<String, String> optionPass;
//        String command ,result;
//        while (true) {
//            command = CommandParser.getScanner().nextLine();
//            if (commandParser.validate(command,"exit",null) != null) return "exit";
//            if (commandParser.validate(command,"show current menu",null) != null) System.out.println("Login Menu");
//            else if ((optionPass = commandParser.validate(command,"user create","u|username/?p|password/n|nickname/e|email/s|slogan")) != null) {
//                result = loginController.createUser(optionPass);
//                if (result != null) System.out.println(result);
//            }
//            else if ((optionPass = commandParser.validate(command,"user login","u|username/p|password/s|stay-logged-in")) != null) {
//                result = loginController.login(optionPass);
//                if (result.equals("login")) {System.out.println("User logged in successfully!"); return "login";}
//                else System.out.println(result);
//            }
//            else if ((optionPass = commandParser.validate(command,"forgot my password","u|username")) != null)
//                System.out.println(loginController.forgetPassword(optionPass));
//            else System.out.println("Invalid command!");
//        }
        return null;
        }
}
