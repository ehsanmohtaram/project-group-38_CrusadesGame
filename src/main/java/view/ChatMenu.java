package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.User;

public class ChatMenu extends Application {

    private final Style style;
    private Stage stage;

    public ChatMenu() {
        style = new Style();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setTitle("Chat Menu");
        Pane pane = new Pane();
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(ChatMenu.class.getResource("/images/background/loginBack.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        stage.getScene().setRoot(pane);
        chatInfo(pane);
        stage.show();
    }

    private void chatInfo(Pane pane) {
        HBox chatRoom = new HBox();
        chatRoom.setBackground(Background.fill(Color.rgb(170,139,100,0.3)));
        chatRoom.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(170,139,100,0.3) ; -fx-background: rgba(170,139,100,0.3) ;");
        chatRoom.setSpacing(15);
        chatRoom.setAlignment(Pos.CENTER);
        chatRoom.setPrefSize(1300, 750);
        chatRoom.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(20), BorderStroke.THIN)));
        VBox contacts = new VBox();
        contacts.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(15), BorderStroke.THIN)));
        contacts.setPrefSize(400, 720);
        contacts.setMaxHeight(720);
        StackPane chatEnvironment = new StackPane();
        chatEnvironment.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(15), BorderStroke.THIN)));
        chatEnvironment.setPrefSize(855, 720);
        chatEnvironment.setMaxHeight(720);
        contactsInfo(contacts, chatEnvironment);
        chatRoom.getChildren().addAll(contacts, chatEnvironment);
        chatRoom.setLayoutX(100); chatRoom.setLayoutY(100);
        pane.getChildren().add(chatRoom);
    }

    public void contactsInfo(VBox contacts, StackPane chatEnvironment) {
        contacts.setAlignment(Pos.CENTER);
        contacts.setSpacing(15);
        StackPane userFiled = new StackPane();
        VBox usernames = new VBox();
        ScrollPane scrollPane = new ScrollPane(usernames);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMaxSize(370, 650);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background : transparent;");
        TextField searchBox = new TextField();
        style.textFiled0(searchBox, "Search Contacts", 290, 70);
        searchBox.setPadding(new Insets(0,30,0,30));
        searchBox.setFont(style.Font0(20));
        Rectangle back = new Rectangle(70, 70, new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm())));
        back.setOnMouseClicked(mouseEvent -> {
            if (back.getFill().equals(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm()))))
                new MainMenu().start(stage);
            else {
                searchBox.setText("");
                usernames.getChildren().clear();
                userFiled.getChildren().clear();
                back.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm())));
            }

        });

        addUser(usernames, searchBox, scrollPane, userFiled, back);
        StackPane newChatTypeBox = new StackPane();
        HBox backAndSearch = new HBox();
        backAndSearch.setAlignment(Pos.CENTER);
        backAndSearch.setSpacing(10);
        backAndSearch.setMaxSize(370,70);
        backAndSearch.getChildren().addAll(back, searchBox);
        userFiled.setOnMouseEntered(mouseEvent -> {
            userFiled.getChildren().add(newChatTypeBox);
            StackPane.setAlignment(newChatTypeBox, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(newChatTypeBox, new Insets(0, 20, 20 ,0));
            handleChatType(newChatTypeBox, chatEnvironment);
        });
        userFiled.setOnMouseExited(mouseEvent -> {
            userFiled.getChildren().remove(newChatTypeBox);
            newChatTypeBox.getChildren().clear();
        });
        userFiled.setPrefHeight(605);
        userFiled.setMaxWidth(370);
        userFiled.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        contacts.getChildren().addAll(backAndSearch, userFiled);
    }

    private void handleChatType(StackPane newChatTypeBox, StackPane chatEnvironment) {
        newChatTypeBox.setMaxSize(60, 60);
        Image openChat = new Image(ChatMenu.class.getResource("/images/buttons/chat_open.png").toExternalForm());
        Image closeChat = new Image(ChatMenu.class.getResource("/images/buttons/chat_close.png").toExternalForm());
        Rectangle chooseChatType = new Rectangle(60, 60, new ImagePattern(openChat));
        StackPane.setAlignment(chooseChatType, Pos.BOTTOM_RIGHT);
        chooseChatType.setViewOrder(-1);
        VBox typeOfChats = new VBox();
        typeOfChats.setStyle("-fx-background-radius: 15; -fx-background-color: rgba(170,139,100,0.8); -fx-background: rgba(170,139,100,0.8);");
        StackPane.setAlignment(typeOfChats, Pos.TOP_RIGHT);
        typeOfChats.setMaxSize(200, 100);
        typeOfChats.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(15), BorderStroke.THIN)));
        makeChatType("Public Chat", typeOfChats, new Image(ChatMenu.class.getResource("/images/buttons/public.png").toExternalForm())); makeChatType("New Group", typeOfChats, new Image(ChatMenu.class.getResource("/images/buttons/group.png").toExternalForm())); makeChatType("New Message", typeOfChats, new Image(ChatMenu.class.getResource("/images/buttons/pv.png").toExternalForm()));
        setEnvironmentByType(typeOfChats, chatEnvironment);
        newChatTypeBox.getChildren().addAll(chooseChatType);
        chooseChatType.setOnMouseClicked(mouseEvent -> {
            if (openChat.equals(((ImagePattern)chooseChatType.getFill()).getImage())) {
                chooseChatType.setFill(new ImagePattern(closeChat));
                newChatTypeBox.getChildren().add(typeOfChats);
                newChatTypeBox.setMaxSize(200, 200);
            }
            else {
                chooseChatType.setFill(new ImagePattern(openChat));
                newChatTypeBox.getChildren().remove(typeOfChats);
                newChatTypeBox.setMaxSize(60, 60);
            }
        });

    }

    private void setEnvironmentByType(VBox typeOfChats, StackPane chatEnvironment) {
        typeOfChats.getChildren().get(0).setOnMouseClicked(mouseEvent -> setEnvironment(chatEnvironment));
        typeOfChats.getChildren().get(1).setOnMouseClicked(mouseEvent -> setEnvironment(chatEnvironment));
        typeOfChats.getChildren().get(2).setOnMouseClicked(mouseEvent -> setEnvironment(chatEnvironment));
    }

    public void makeChatType(String text, VBox typeOfChat, Image image) {
        HBox hbox = new HBox();
        hbox.setOnMouseEntered(mouseEvent -> hbox.setStyle("-fx-background-radius: 15; -fx-background-color: gray; -fx-background: gray;"));
        hbox.setOnMouseExited(mouseEvent -> hbox.setStyle("-fx-background-radius: 15; -fx-background-color: transparent; -fx-background: transparent;"));
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(10);
        hbox.setPrefSize(190, 30);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Rectangle rectangle = new Rectangle(20, 20, new ImagePattern(image));
        Label label = new Label(text);
        label.setTextFill(Color.BLACK);
        label.setFont(style.Font0(15));
        hbox.getChildren().addAll(rectangle, label);
        typeOfChat.getChildren().add(hbox);
    }

    public void addUser(VBox usernames, TextField searchBox, ScrollPane scrollPane, StackPane userFiled, Rectangle back) {
        searchBox.textProperty().addListener((observableValue, s, t1) -> {
            back.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back1.png").toExternalForm())));
            usernames.getChildren().clear();
            userFiled.getChildren().clear();
            for (User user : User.users) {
                if (!user.getUserName().matches("^" + searchBox.getText() + ".*")) continue;
                HBox userHolder = new HBox();
                userHolder.setOnMouseEntered(event ->
                        userHolder.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(128,128,128, 0.4) ; -fx-background: rgba(128,128,128, 0.4);")
                );
                userHolder.setOnMouseExited(event -> userHolder.setStyle("-fx-background-radius: 20; -fx-background-color: transparent ; -fx-background: transparent;"));
                userHolder.setPadding(new Insets(10));
                userHolder.setAlignment(Pos.CENTER_LEFT);
                Circle circle = new Circle(30, new ImagePattern(new Image(user.getAvatar())));
                Label username = new Label(user.getUserName());
                username.setPadding(new Insets(0,0,20,20));
                username.setFont(style.Font0(25));
                username.setTextFill(Color.rgb(170,139,100,0.8));
                userHolder.setPrefSize(360, 70);
                userHolder.getChildren().addAll(circle, username);
                usernames.getChildren().add(userHolder);
            }
            userFiled.getChildren().add(scrollPane);
        });

    }

    public void setEnvironment(StackPane chatEnvironment) {
        chatEnvironment.getChildren().clear();
        HBox hBox = new HBox();
        hBox.setMaxSize(700, 70);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(15);
        Rectangle send = new Rectangle(70,70, new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/send.png").toExternalForm())));
        send.setOpacity(0.3);
        TextField chatSender = new TextField();
        chatSender.textProperty().addListener((observableValue, s, t1) -> {
            if(chatSender.getText().equals("")) send.setOpacity(0.3);
            else send.setOpacity(1);
        } );
        hBox.getChildren().addAll(chatSender, send);
        style.textFiled0(chatSender, "Message",615, 70);
        chatSender.setPadding(new Insets(0,30, 0, 30));
        chatSender.setFont(style.Font0(20));
        StackPane.setMargin(hBox, new Insets(0,0,15,0));
        StackPane.setAlignment(hBox, Pos.BOTTOM_CENTER);
        chatEnvironment.getChildren().add(hBox);

    }
}
