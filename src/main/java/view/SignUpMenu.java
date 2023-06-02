package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Randomize;

public class SignUpMenu extends Application {
    private final Style style;
    private Stage stage;
    public SignUpMenu() {
        style = new Style();

    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
        Pane pane = new Pane();
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(SignUpMenu.class.getResource("/images/background/loginBack.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        signUpInfo(pane);
        stage.getScene().setRoot(pane);
        stage.setTitle("Signup Menu");
        stage.setFullScreen(true);
        stage.show();
    }

    public void signUpInfo(Pane pane) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        HBox nickNameAndUserName = new HBox();
        Label userError = new Label();
        TextField userName = new TextField();
        Label nickNameError = new Label();
        TextField nickName = new TextField();
        usernameAndNickname(nickNameAndUserName, userError, userName, nickNameError, nickName);


        VBox passwordAndConfirmationFiledVbox = new VBox();
        Rectangle checkRandomPassword = new Rectangle();
        passwordAndConfirmationFiledVbox.setSpacing(10);
        Label passwordError = new Label();
        passwordError.setFont(style.Font0(15));
        HBox passwordAndConfirmationHbox = new HBox();
        passwordAndConfirmationHbox.setSpacing(20);
        passwordAndConfirmationHbox.setAlignment(Pos.CENTER);
        HBox passwordFiled = new HBox();
        passwordFiledDisableEnableHandle(passwordAndConfirmationHbox, passwordFiled, checkRandomPassword, passwordError,true);
        HBox confirmation = new HBox();
        passwordFiledDisableEnableHandle(passwordAndConfirmationHbox, confirmation, checkRandomPassword, passwordError,false);
        passwordAndConfirmationHbox.getChildren().addAll(passwordFiled, confirmation);
        passwordAndConfirmationFiledVbox.getChildren().addAll(passwordError, passwordAndConfirmationHbox);

        HBox randomPasswordFiled = new HBox();
        randomPasswordFiled.setAlignment(Pos.CENTER);
        randomPasswordFiled.setSpacing(7);
        style.checkBox0(checkRandomPassword);
        Label randomPassword = new Label("Random Password");
        HBox.setMargin(randomPassword, new Insets(0, 423, 0 ,0));
        randomPassword.setTextFill(Color.rgb(170,139,100,0.8));
        randomPassword.setFont(style.Font0(20));
        randomPasswordFiled.getChildren().addAll(checkRandomPassword, randomPassword);

        VBox emailBox = new VBox();
        emailBox.setSpacing(10);
        TextField email = new TextField();
        Label emailError = new Label();
        emailError.setFont(style.Font0(15));
        email.setFont(style.Font0(24));
        style.textFiled0(email,"Email" ,620, 70);
        email.setPadding(new Insets(0,30,0,30));
        emailBox.getChildren().addAll(emailError, email);

        HBox buttonFiled = new HBox();
        buttonFiled.setSpacing(20);
        buttonFiled.setAlignment(Pos.CENTER);
        Button signUp = new Button();
        VBox.setMargin(signUp, new Insets(100, 0, 0 ,0));
        style.button0(signUp, "SIGNUP", 300, 60);
        signUp.setFont(style.Font0(20));
        Button back = new Button();
        VBox.setMargin(back, new Insets(100, 0, 0 ,0));
        style.button0(back, "BACK", 300, 60);
        back.setFont(style.Font0(20));
        VBox.setMargin(buttonFiled, new Insets(100, 0, 0 ,0));
        buttonFiled.getChildren().addAll(signUp, back);
        vBox.getChildren().addAll(nickNameAndUserName, passwordAndConfirmationFiledVbox, randomPasswordFiled,emailBox, buttonFiled);
        vBox.setLayoutX(670);  vBox.setLayoutY(181);
        pane.getChildren().add(vBox);
        buttonHandel(buttonFiled);
        atMomentError(userError, nickNameError, emailError, userName, nickName, email);
    }
    public void usernameAndNickname(HBox nickNameAndUserName, Label userError, TextField userName, Label nickNameError, TextField nickName) {
        nickNameAndUserName.setSpacing(20);
        VBox userFiled = new VBox();
        userFiled.setSpacing(10);
        userError.setFont(style.Font0(15));
        userName.setFont(style.Font0(24));
        style.textFiled0(userName,"Username" ,300, 70);
        userName.setPadding(new Insets(0,30,0,30));
        userFiled.getChildren().addAll(userError, userName);
        VBox nickNameFiled = new VBox();
        nickNameFiled.setSpacing(10);
        nickNameError.setFont(style.Font0(15));
        nickName.setFont(style.Font0(24));
        style.textFiled0(nickName,"Nickname" ,300, 70);
        nickName.setPadding(new Insets(0,30,0,30));
        nickNameFiled.getChildren().addAll(nickNameError, nickName);
        nickNameAndUserName.getChildren().addAll(userFiled, nickNameFiled);
    }

    public void passwordFiledDisableEnableHandle(HBox passwordAndConfirmationHbox,HBox hBox, Rectangle randomPassword, Label passwordError,boolean isPassword) {
        hBox.setPrefSize(300, 70);
        hBox.setMaxSize(300, 70);
        PasswordField password = new PasswordField();
        password.setPrefSize(240, 70);
        password.setBackground(Background.EMPTY);
        if (isPassword) password.setPromptText("Password");
        else password.setPromptText("Confirmation");
        password.setFont(style.Font0(24));
        password.setStyle("-fx-text-fill: rgba(170,139,100,0.8); -fx-prompt-text-fill: rgba(86,73,57,0.8);");
        password.setPadding(new Insets(0,0,0,30));
        Button button = new Button();
        button.setPrefSize(60, 70);
        BackgroundSize backgroundSize = new BackgroundSize(30, 30, false, false, false, false);
        Image image = new Image(LoginMenu.class.getResource("/images/buttons/invisi.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        button.setBackground(new Background(backgroundImage));
        button.setOpacity(0.6);
        hBox.getChildren().addAll(password, button);
        hBox.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        button.setOnMouseEntered(mouseEvent -> button.setOpacity(0.2));
        button.setOnMouseExited(mouseEvent -> button.setOpacity(0.6));
        button.setOnMouseClicked(mouseEvent -> changeInvisiToVisi(hBox, passwordError,isPassword));
        randomPassword.fillProperty().addListener(((observableValue, paint, t1) -> {
            if (t1 instanceof ImagePattern && !(paint instanceof ImagePattern) && isPassword)
                ((TextField)hBox.getChildren().get(0)).setText(Randomize.randomPassword());
            if (paint instanceof ImagePattern && !(t1 instanceof ImagePattern) && isPassword)
                ((TextField)hBox.getChildren().get(0)).setText("");
        }));
        passwordAndConfirmationListener(passwordAndConfirmationHbox, password, passwordError, isPassword);
    }

    public void changeInvisiToVisi(HBox hBox, Label passwordError,boolean isPassword) {
        TextField password;
        if (((Button) hBox.getChildren().get(1)).getBackground().getImages().get(0).getImage().getUrl()
                .equals(LoginMenu.class.getResource("/images/buttons/invisi.png").toExternalForm()))
            password = new TextField();
        else password = new PasswordField();
        String passwordString = ((TextField) hBox.getChildren().get(0)).getText();
        hBox.getChildren().remove(0);
        password.setText(passwordString);
        password.setPrefSize(240, 70);
        password.setBackground(Background.EMPTY);
        if (isPassword) password.setPromptText("Password");
        else password.setPromptText("Confirmation");
        password.setFont(style.Font0(24));
        password.setStyle("-fx-text-fill: rgba(170,139,100,0.8); -fx-prompt-text-fill: rgba(86,73,57,0.8);");
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
        //passwordAndConfirmationListener(password,passwordError,isPassword);

    }

    public void buttonHandel(HBox buttonBox) {
        buttonBox.getChildren().get(1).setOnMouseClicked(mouseEvent -> new LoginMenu().start(stage));
    }

    public void atMomentError(Label userError, Label nickNameError, Label emailError, TextField userName, TextField nickName, TextField email) {
        nickName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (nickName.getText().length() == 0) nickNameError.setText("Please fill mandatory parts!");
            else nickNameError.setText("");
        });
        userName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (userName.getText().length() == 0) userError.setText("Please fill mandatory parts!");
            else if (userName.getText().matches(".*[^A-Za-z0-9_]+.*")) userError.setText("Incorrect format of username!");
            else userError.setText("");
        });
        email.textProperty().addListener((observableValue, oldValue, newValue) -> {
           if (email.getText().length() == 0) email.setText("Please fill mandatory parts!");
           else if (!email.getText().matches("^[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]+$")) emailError.setText("Incorrect format of email!");
           else emailError.setText("");
        });
    }

    public void passwordAndConfirmationListener(HBox passwordAndConfirmationHbox, TextField password,Label passwordError,boolean isPassword) {
        password.textProperty().addListener((observableValue, s, t1) -> {
            if (isPassword) {
                if (password.getText().length() == 0) passwordError.setText("Please fill mandatory parts!");
                else if (password.getText().length() < 6 || !password.getText().matches(".*[a-z]+.*") ||
                        !password.getText().matches(".*[A-Z]+.*") || !password.getText().matches(".*[0-9]+.*") ||
                        !password.getText().matches(".*\\W+.*"))
                    passwordError.setText("Weak password. Please set a strong password!");
                else if (!((TextField)((HBox)((HBox)passwordAndConfirmationHbox.getChildren().get(1)).getChildren().get(1)).getChildren().get(0)).getText().equals(password.getText()))
                    passwordError.setText("Confirmation did not match with password!");
                else passwordError.setText("");
            }
            else {
                if (password.getText().length() == 0) passwordError.setText("Please fill mandatory parts!");
                else if (!((TextField)((HBox)((HBox)passwordAndConfirmationHbox.getChildren().get(1)).getChildren().get(1)).getChildren().get(0)).getText().equals(password.getText()))
                    passwordError.setText("Confirmation did not match with password!");
                else passwordError.setText("");
            }
        });
    }
}
