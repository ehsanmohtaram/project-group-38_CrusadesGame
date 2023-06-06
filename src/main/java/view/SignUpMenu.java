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
import model.User;
import view.controller.InformationController;

public class SignUpMenu extends Application {
    private final Style style;
    private final InformationController info;
    private Stage stage;
    public SignUpMenu() {
        info = new InformationController();
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
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        HBox nickNameAndUserName = new HBox();
        Label userError = new Label();
        userError.setPrefHeight(0);
        userError.setTextFill(Color.TRANSPARENT);
        TextField userName = new TextField();
        Label nickNameError = new Label();
        nickNameError.setTextFill(Color.TRANSPARENT);
        TextField nickName = new TextField();
        usernameAndNickname(nickNameAndUserName, userError, userName, nickNameError, nickName);

        VBox passwordAndConfirmationFiledVbox = new VBox();
        Rectangle checkRandomPassword = new Rectangle();
        Label passwordError = new Label();
        passwordError.setTextFill(Color.TRANSPARENT);
        passwordError.setFont(style.Font0(0));
        HBox passwordAndConfirmationHbox = new HBox();
        passwordAndConfirmationHbox.setSpacing(20);
        passwordAndConfirmationHbox.setAlignment(Pos.CENTER);
        HBox passwordFiled = new HBox();
        PasswordField confirmation = new PasswordField();
        style.textFiled0(confirmation, "Confirmation", 300, 70);
        confirmation.setFont(style.Font0(24));
        passwordFiledDisableEnableHandle(passwordAndConfirmationHbox, passwordFiled, confirmation,checkRandomPassword, passwordError);
        passwordAndConfirmationHbox.getChildren().addAll(passwordFiled, confirmation);
        passwordAndConfirmationFiledVbox.getChildren().addAll(passwordError, passwordAndConfirmationHbox);

        HBox randomPasswordFiledAndSlogan = new HBox();
        randomPasswordFiledAndSlogan.setAlignment(Pos.CENTER);
        randomPasswordFiledAndSlogan.setSpacing(7);
        style.checkBox0(checkRandomPassword);
        Label randomPassword = new Label("Random Password");
        HBox.setMargin(randomPassword, new Insets(0, 320, 0 ,0));
        randomPassword.setTextFill(Color.rgb(170,139,100,0.8));
        randomPassword.setFont(style.Font0(20));
        Rectangle sloganCheck = new Rectangle();
        style.checkBox0(sloganCheck);
        Label slogan = new Label("Slogan");
        slogan.setTextFill(Color.rgb(170,139,100,0.8));
        slogan.setFont(style.Font0(20));
        HBox.setMargin(slogan, new Insets(0, 0, 0 ,0));
        randomPasswordFiledAndSlogan.getChildren().addAll(checkRandomPassword, randomPassword, sloganCheck, slogan);
        TextField sloganText = new TextField();
        Rectangle sloganRandomCheck = new Rectangle();
        style.checkBox0(sloganCheck);
        sloganCheck.fillProperty().addListener(((observableValue, paint, t1) -> {
            if (t1 instanceof ImagePattern && !(paint instanceof ImagePattern)) openSloganFiled(sloganText, sloganRandomCheck, vBox);
            else if (paint instanceof ImagePattern && !(t1 instanceof ImagePattern)) {
                vBox.getChildren().remove(3);  vBox.getChildren().remove(3); vBox.setLayoutY(181);
            }
        }));
        VBox emailBox = new VBox();
        TextField email = new TextField();
        Label emailError = new Label();
        emailError.setTextFill(Color.TRANSPARENT);
        emailError.setFont(style.Font0(0));
        email.setFont(style.Font0(24));
        style.textFiled0(email,"Email" ,620, 70);
        email.setPadding(new Insets(0,30,0,30));
        emailBox.getChildren().addAll(emailError, email);

        VBox securityFiledVbox = new VBox();
        HBox securityFiled = new HBox();
        TextField securityAnswer = new TextField();
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        Label securityError = new Label();
        securityError.setTextFill(Color.TRANSPARENT);
        securityError.setFont(style.Font0(0));
        securityAnswer.setFont(style.Font0(24));
        makeSecurityQuestion(choiceBox, securityAnswer, securityFiled);
        securityFiledVbox.getChildren().addAll(securityError, securityFiled);

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
        VBox.setMargin(buttonFiled, new Insets(20, 0, 0 ,0));
        buttonFiled.getChildren().addAll(signUp, back);
        vBox.getChildren().addAll(nickNameAndUserName, passwordAndConfirmationFiledVbox, randomPasswordFiledAndSlogan,emailBox, securityFiledVbox,buttonFiled);
        vBox.setLayoutX(670);  vBox.setLayoutY(181);
        pane.getChildren().add(vBox);
        buttonHandel(buttonFiled);
        atMomentError(userError, emailError, nickNameError, securityError,userName, email, nickName, securityAnswer, choiceBox);
        signUp.setOnMouseClicked(mouseEvent -> {
            if (checkForErrors(userError, nickNameError, emailError, passwordError, securityError)) {
                info.updateAllInfoTogether(userName, nickName, email, confirmation, sloganText, securityAnswer, choiceBox.getValue());
            }
        });
    }

    public boolean signUpFirstErrorHandling() {
        return true;
    }

    public boolean checkForErrors(Label userError, Label nickNameError, Label emailError, Label passwordError, Label securityError) {
        return !userError.getTextFill().equals(Color.INDIANRED) && !nickNameError.getTextFill().equals(Color.INDIANRED) && !emailError.getTextFill().equals(Color.INDIANRED) &&
                !passwordError.getTextFill().equals(Color.INDIANRED) && !securityError.getTextFill().equals(Color.INDIANRED);
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

    public void passwordFiledDisableEnableHandle(HBox passwordAndConfirmationHbox,HBox hBox, TextField confirmation,Rectangle randomPassword, Label passwordError) {
        hBox.setPrefSize(300, 70);
        hBox.setMaxSize(300, 70);
        PasswordField password = new PasswordField();
        password.setPrefSize(240, 70);
        password.setBackground(Background.EMPTY);
        password.setPromptText("Password");
        confirmation.setPromptText("Confirmation");
        password.setFont(style.Font0(24));
        confirmation.setFont(style.Font0(24));
        confirmation.setPadding(new Insets(0,30,0,30));
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
        button.setOnMouseClicked(mouseEvent -> changeInvisiToVisi(passwordAndConfirmationHbox, hBox, confirmation,passwordError));
        randomPassword.fillProperty().addListener(((observableValue, paint, t1) -> {
            if (t1 instanceof ImagePattern && !(paint instanceof ImagePattern))
                ((TextField)hBox.getChildren().get(0)).setText(Randomize.randomPassword());
            if (paint instanceof ImagePattern && !(t1 instanceof ImagePattern))
                ((TextField)hBox.getChildren().get(0)).setText("");
        }));
        passwordAndConfirmationListener(password, confirmation,passwordError);
    }

    public void changeInvisiToVisi(HBox passwordAndConfirmationHbox,HBox hBox, TextField confirmationBox, Label passwordError) {
        TextField password;
        TextField confirmation;
        if (((Button) hBox.getChildren().get(1)).getBackground().getImages().get(0).getImage().getUrl()
                .equals(LoginMenu.class.getResource("/images/buttons/invisi.png").toExternalForm())) {
            password = new TextField();
            confirmation = new TextField();
        }
        else {
            password = new PasswordField();
            confirmation = new PasswordField();
        }
        String passwordString = ((TextField) hBox.getChildren().get(0)).getText();
        String confirmationString = confirmationBox.getText();
        hBox.getChildren().remove(0);
        passwordAndConfirmationHbox.getChildren().remove(1);
        password.setText(passwordString);
        confirmation.setText(confirmationString);
        password.setPrefSize(240, 70);
        password.setBackground(Background.EMPTY);
        password.setPromptText("Password");
        confirmation.setPromptText("Confirmation");
        style.textFiled0(confirmation, "Confirmation", 300, 70);
        confirmation.setFont(style.Font0(24));
        confirmation.setPadding(new Insets(0,30,0,30));
        password.setFont(style.Font0(24));
        password.setStyle("-fx-text-fill: rgba(170,139,100,0.8); -fx-prompt-text-fill: rgba(86,73,57,0.8);");
        password.setPadding(new Insets(0,0,0,30));
        hBox.getChildren().add(0, password);
        passwordAndConfirmationHbox.getChildren().add(1, confirmation);
        BackgroundSize backgroundSize = new BackgroundSize(30, 30, false, false, false, false);
        Image image;
        if (((Button) hBox.getChildren().get(1)).getBackground().getImages().get(0).getImage().getUrl()
                .equals(LoginMenu.class.getResource("/images/buttons/invisi.png").toExternalForm()))
            image = new Image(LoginMenu.class.getResource("/images/buttons/visi.png").toExternalForm());
        else image = new Image(LoginMenu.class.getResource("/images/buttons/invisi.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        ((Button)hBox.getChildren().get(1)).setBackground(new Background(backgroundImage));
        passwordAndConfirmationListener(password, confirmation,passwordError);
    }

    public void buttonHandel(HBox buttonBox) {
        buttonBox.getChildren().get(1).setOnMouseClicked(mouseEvent -> {
            try {new LoginMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        buttonBox.getChildren().get(0).setOnMouseClicked(mouseEvent -> {

        });
    }

    public void setError(Label label, String result, boolean justColorChange) {
        if (result != null) {
            label.setTextFill(Color.INDIANRED);
            if (!justColorChange) {
                label.setPadding(new Insets(5, 0, 10, 0));
                label.setFont(style.Font0(15));
            }
            label.setText(result);
        }
        else {
            label.setTextFill(Color.TRANSPARENT);
            if (!justColorChange) {
                label.setPadding(new Insets(0));
                label.setFont(style.Font0(0));
            }
            label.setText("");
        }
    }

    public void openSloganFiled(TextField slogan, Rectangle randomSlogan,VBox vBox) {
        vBox.setLayoutY(100);
        HBox randomSloganHbox = new HBox();
        randomSloganHbox.setAlignment(Pos.CENTER);
        randomSloganHbox.setSpacing(7);
        style.checkBox0(randomSlogan);
        Label randomSloganLabel = new Label("Random Slogan");
        HBox.setMargin(randomSloganLabel, new Insets(0, 433, 0 ,0));
        randomSloganLabel.setTextFill(Color.rgb(170,139,100,0.8));
        randomSloganLabel.setFont(style.Font0(20));
        randomSloganHbox.getChildren().addAll(randomSlogan, randomSloganLabel);
        slogan.setFont(style.Font0(24));
        style.textFiled0(slogan,"Slogan" ,620, 70);
        slogan.setPadding(new Insets(0,30,0,30));
        vBox.getChildren().add(3, slogan);
        vBox.getChildren().add(4, randomSloganHbox);
        randomSlogan.fillProperty().addListener(((observableValue, paint, t1) -> {
            if (t1 instanceof ImagePattern && !(paint instanceof ImagePattern)) slogan.setText(Randomize.randomSlogan());
            else if (paint instanceof ImagePattern && !(t1 instanceof ImagePattern)) slogan.setText("");
        }));

    }

    public void makeSecurityQuestion(ChoiceBox<String> choiceBox, TextField securityAnswer, HBox hBox) {
        choiceBox.getItems().add(User.questions.get(0));
        choiceBox.getItems().add(User.questions.get(1));
        choiceBox.getItems().add(User.questions.get(2));
        choiceBox.setBackground(Background.EMPTY);
        choiceBox.setPrefSize(300 , 70);
        choiceBox.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        choiceBox.setStyle("-fx-background-color : transparent;" +
                "-fx-font-size: 15 ;" +
                "-fx-padding: 0 20 0 20;" +
                "-fx-font-family : 'Comic Sans MS';"+
                "-fx-control-inner-background : rgba(170,139,100,0.8);" +
                "-fx-background-radius: 10;");
        style.textFiled0(securityAnswer, "Security Answer", 300, 70);
        securityAnswer.setFont(style.Font0(24));
        securityAnswer.setPadding(new Insets(0,30,0,30));
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.getChildren().addAll(choiceBox, securityAnswer);
    }

    public void atMomentError(Label userError, Label emailError, Label nicknameError, Label securityError,TextField userName, TextField email, TextField nickname, TextField securityFiled, ChoiceBox<String> choiceBox) {
        userName.textProperty().addListener((observableValue, oldValue, newValue) -> {
            info.updateTextYouWant(userName, 0);
            setError(userError, info.usernameError(), true);
        });
        email.textProperty().addListener((observableValue, oldValue, newValue) -> {
            info.updateTextYouWant(email, 3);
            setError(emailError, info.emailError(), false);
        });
        nickname.textProperty().addListener((observableValue, oldValue, newValue) -> {
            info.updateTextYouWant(nickname, 4);
            setError(nicknameError, info.nicknameError(), true);
        });
        securityFiled.textProperty().addListener((observableValue, oldValue, newValue) -> {
            info.updateTextYouWant(securityFiled, 5);
            setError(securityError, info.securityError(choiceBox), false);
        });
        choiceBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            info.updateTextYouWant(securityFiled, 5);
            setError(securityError, info.securityError(choiceBox), false);
        });

    }

    public void passwordAndConfirmationListener(TextField password, TextField confirmation,Label passwordError) {
        password.textProperty().addListener((observableValue, s, t1) -> {
            info.updateTextYouWant(password, 1);
            info.updateTextYouWant(confirmation, 2);
            setError(passwordError, info.passwordError(), false);
        });
        confirmation.textProperty().addListener((observableValue, s, t1) -> {
            info.updateTextYouWant(password, 1);
            info.updateTextYouWant(confirmation, 2);
            setError(passwordError, info.confirmationError(), false);
        });
    }

}
