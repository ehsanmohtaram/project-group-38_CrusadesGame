package view;

import controller.Connection;
import controller.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.*;
import view.controller.LoginMenuController;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChatMenu extends Application {
    private final Style style;
    private Stage stage;
    private final Connection connection;

    public ChatMenu() {
        connection  = LoginMenuController.connection;
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
        chatEnvironment.setStyle("-fx-background-radius: 15; -fx-background-color: transparent; -fx-background: transparent;");
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
        Rectangle chooseChatType = new Rectangle(60, 60);
        back.setOnMouseClicked(mouseEvent -> {
            if (((ImagePattern)back.getFill()).getImage().getUrl().equals(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm())).getImage().getUrl()))
                new MainMenu().start(stage);
            else {
                searchBox.setText("");
                usernames.getChildren().clear();
                userFiled.getChildren().clear();
                chooseChatType.setFill(null);
                back.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm())));
            }

        });
        searchBox.textProperty().addListener((observableValue, s, t1) -> addUser(usernames, searchBox, scrollPane, userFiled, back, chatEnvironment));
        StackPane newChatTypeBox = new StackPane();
        HBox backAndSearch = new HBox();
        backAndSearch.setAlignment(Pos.CENTER);
        backAndSearch.setSpacing(10);
        backAndSearch.setMaxSize(370,70);
        backAndSearch.getChildren().addAll(back, searchBox);
        Image next = new Image(ChatMenu.class.getResource("/images/buttons/next.png").toExternalForm());
        userFiled.setOnMouseEntered(mouseEvent -> {
            if (chooseChatType.getFill() instanceof ImagePattern) {
                if (!((ImagePattern)chooseChatType.getFill()).getImage().getUrl().equals(new ImagePattern(next).getImage().getUrl()) &&
                        !((ImagePattern) chooseChatType.getFill()).getImage().getUrl().equals(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/next1.png").toExternalForm())).getImage().getUrl())) {
                    if (!userFiled.getChildren().contains(newChatTypeBox)) userFiled.getChildren().add(newChatTypeBox);
                    StackPane.setAlignment(newChatTypeBox, Pos.BOTTOM_RIGHT);
                    StackPane.setMargin(newChatTypeBox, new Insets(0, 20, 20, 0));
                    handleChatType(newChatTypeBox, chooseChatType, chatEnvironment, searchBox, usernames, scrollPane, userFiled, back);
                }
            }
            else {
                if (!userFiled.getChildren().contains(newChatTypeBox)) userFiled.getChildren().add(newChatTypeBox);
                StackPane.setAlignment(newChatTypeBox, Pos.BOTTOM_RIGHT);
                StackPane.setMargin(newChatTypeBox, new Insets(0, 20, 20, 0));
                handleChatType(newChatTypeBox, chooseChatType, chatEnvironment, searchBox, usernames, scrollPane, userFiled, back);
            }
        });
        userFiled.setOnMouseExited(mouseEvent -> {
            if (chooseChatType.getFill() instanceof ImagePattern) {
                if (!((ImagePattern) chooseChatType.getFill()).getImage().getUrl().equals(new ImagePattern(next).getImage().getUrl()) &&
                        !((ImagePattern) chooseChatType.getFill()).getImage().getUrl().equals(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/next1.png").toExternalForm())).getImage().getUrl())) {
                    userFiled.getChildren().remove(newChatTypeBox);
                    newChatTypeBox.getChildren().clear();
                }
            }
            else {
                userFiled.getChildren().remove(newChatTypeBox);
                newChatTypeBox.getChildren().clear();
            }
        });
        userFiled.setPrefHeight(605);
        userFiled.setMaxWidth(370);
        userFiled.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        contacts.getChildren().addAll(backAndSearch, userFiled);
    }

    private void handleChatType(StackPane newChatTypeBox, Rectangle chooseChatType,StackPane chatEnvironment, TextField searchBox, VBox usernames, ScrollPane scrollPane, StackPane userFiled, Rectangle back) {
        newChatTypeBox.setMaxSize(60, 60);
        Image openChat = new Image(ChatMenu.class.getResource("/images/buttons/chat_open.png").toExternalForm());
        Image closeChat = new Image(ChatMenu.class.getResource("/images/buttons/chat_close.png").toExternalForm());
        Image next = new Image(ChatMenu.class.getResource("/images/buttons/next.png").toExternalForm());
        Image next1 = new Image(ChatMenu.class.getResource("/images/buttons/next1.png").toExternalForm());
        chooseChatType.setFill(new ImagePattern(openChat));
        StackPane.setAlignment(chooseChatType, Pos.BOTTOM_RIGHT);
        chooseChatType.setViewOrder(-1);
        VBox typeOfChats = new VBox();
        typeOfChats.setStyle("-fx-background-radius: 15; -fx-background-color: rgba(170,139,100,0.8); -fx-background: rgba(170,139,100,0.8);");
        StackPane.setAlignment(typeOfChats, Pos.TOP_RIGHT);
        typeOfChats.setMaxSize(200, 100);
        typeOfChats.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(15), BorderStroke.THIN)));
        makeChatType("Public Chat", typeOfChats, new Image(ChatMenu.class.getResource("/images/buttons/public.png").toExternalForm())); makeChatType("New Group", typeOfChats, new Image(ChatMenu.class.getResource("/images/buttons/group.png").toExternalForm())); makeChatType("New Message", typeOfChats, new Image(ChatMenu.class.getResource("/images/buttons/pv.png").toExternalForm()));
        setEnvironmentByType(typeOfChats, chatEnvironment, searchBox, usernames, scrollPane, userFiled, back, chooseChatType, newChatTypeBox);
        if (!newChatTypeBox.getChildren().contains(chooseChatType)) newChatTypeBox.getChildren().addAll(chooseChatType);
        TextField groupName = new TextField();
        ArrayList<User> selectedUser = new ArrayList<>();
        chooseChatType.setOnMouseClicked(mouseEvent -> {
            if (openChat.equals(((ImagePattern)chooseChatType.getFill()).getImage())) {
                chooseChatType.setFill(new ImagePattern(closeChat));
                newChatTypeBox.getChildren().add(typeOfChats);
                newChatTypeBox.setMaxSize(200, 200);
            }
            else if (closeChat.equals(((ImagePattern)chooseChatType.getFill()).getImage())) {
                chooseChatType.setFill(new ImagePattern(openChat));
                newChatTypeBox.getChildren().remove(typeOfChats);
                newChatTypeBox.setMaxSize(60, 60);
            }
            else if (next.getUrl().equals(((ImagePattern)chooseChatType.getFill()).getImage().getUrl())){
                chooseChatType.setFill(new ImagePattern(next));
                for (Node users : usernames.getChildren()) {
                   if (((Rectangle)((HBox) users).getChildren().get(0)).getFill() instanceof ImagePattern)
                       selectedUser.add(User.getUserByUsername(((Label)((HBox) users).getChildren().get(2)).getText()));
                }
                searchBox.setText("");
                userFiled.getChildren().clear();
                usernames.getChildren().clear();
                userFiled.getChildren().add(newChatTypeBox);
                chooseChatType.setFill(new ImagePattern(next1));
                groupSetNameAndMembers(userFiled, selectedUser, groupName);
            }
            else if (!groupName.getText().equals("")) {
                searchBox.setText("");
                back.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm())));
                userFiled.getChildren().clear();
                usernames.getChildren().clear();
                chooseChatType.setFill(null);
                for (User user : selectedUser)
                    Controller.currentUser.setMyChats(new Chat(Controller.currentUser.getUserName(), user.getUserName(), "You Invite To This Group " + Controller.currentUser.getUserName(), ChatType.GROUP, java.time.LocalDateTime.now().getHour(), java.time.LocalDateTime.now().getMinute()));
                setEnvironment(chatEnvironment, 1, null, groupName.getText(), selectedUser.size());
            }
        });

    }

    private void setEnvironmentByType(VBox typeOfChats, StackPane chatEnvironment, TextField searchBox, VBox usernames, ScrollPane scrollPane, StackPane userFiled, Rectangle back, Rectangle chooseChatType, StackPane newChatTypeBox) {
        typeOfChats.getChildren().get(0).setOnMouseClicked(mouseEvent -> setEnvironment(chatEnvironment,0,null, "Public Chat", User.users.size()));
        typeOfChats.getChildren().get(1).setOnMouseClicked(mouseEvent -> selectUserForGroup(usernames, searchBox, scrollPane, userFiled, back, chooseChatType, newChatTypeBox));
        typeOfChats.getChildren().get(2).setOnMouseClicked(mouseEvent -> {searchBox.setText("u"); searchBox.setText("");});
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

    public void addUser(VBox usernames, TextField searchBox, ScrollPane scrollPane, StackPane userFiled, Rectangle back, StackPane chatEnvironment) {
        back.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back1.png").toExternalForm())));
        usernames.getChildren().clear();
        userFiled.getChildren().clear();
        for (User user : User.users) {
            if (!user.getUserName().matches("^" + searchBox.getText() + ".*") || user.equals(Controller.currentUser)) continue;
            HBox userHolder = new HBox();
            userHolder.setOnMouseClicked(mouseEvent -> {
                setEnvironment(chatEnvironment,2, user, "" , 2);
                searchBox.setText("");
                usernames.getChildren().clear();
                userFiled.getChildren().clear();
                back.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm())));
            });
            userHolder.setOnMouseEntered(event -> userHolder.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(128,128,128, 0.4) ; -fx-background: rgba(128,128,128, 0.4);"));
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
    }

    public void selectUserForGroup(VBox usernames, TextField searchBox, ScrollPane scrollPane, StackPane userFiled, Rectangle back, Rectangle chooseChatType, StackPane newChatTypeBox) {
        back.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back1.png").toExternalForm())));
        usernames.getChildren().clear();
        userFiled.getChildren().clear();
        newChatTypeBox.getChildren().remove(1);
        userFiled.getChildren().add(newChatTypeBox);
        newChatTypeBox.setViewOrder(-1);
        chooseChatType.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/next.png").toExternalForm())));
        for (User user : User.users) {
            if (!user.getUserName().matches("^" + searchBox.getText() + ".*") || user.equals(Controller.currentUser)) continue;
            HBox userHolder = new HBox();
            userHolder.setSpacing(20);
            userHolder.setOnMouseEntered(event -> userHolder.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(128,128,128, 0.4) ; -fx-background: rgba(128,128,128, 0.4);"));
            userHolder.setOnMouseExited(event -> userHolder.setStyle("-fx-background-radius: 20; -fx-background-color: transparent ; -fx-background: transparent;"));
            userHolder.setPadding(new Insets(10, 0, 10, 25));
            Rectangle checkBox = new Rectangle(20, 20);
            style.checkBox0(checkBox);
            userHolder.setAlignment(Pos.CENTER_LEFT);
            Circle circle = new Circle(30, new ImagePattern(new Image(user.getAvatar())));
            Label username = new Label(user.getUserName());
            username.setPadding(new Insets(0,0,20,0));
            username.setFont(style.Font0(25));
            username.setTextFill(Color.rgb(170,139,100,0.8));
            userHolder.setPrefSize(360, 70);
            userHolder.getChildren().addAll(checkBox,circle, username);
            usernames.getChildren().add(userHolder);
        }
        userFiled.getChildren().add(scrollPane);
    }

    public void groupSetNameAndMembers(StackPane userFiled, ArrayList<User> selectedUser, TextField groupName) {
        VBox groupSet = new VBox();
        groupSet.setAlignment(Pos.TOP_CENTER);
        groupSet.setSpacing(20);
        Label profileHolder = new Label();
        profileHolder.setPrefSize(120, 120);
        profileHolder.setAlignment(Pos.CENTER);
        profileHolder.setTextFill(Color.rgb(170,139,100,0.8));
        profileHolder.setFont(style.Font0(45));
        profileHolder.setStyle("-fx-background-color: rgba(183,181,39,0.5); -fx-background: rgba(183,181,39,0.5); -fx-background-radius: 60;");
        StackPane.setAlignment(groupSet, Pos.TOP_CENTER);
        StackPane.setMargin(groupSet, new Insets(40, 0,0,0));
        groupName.textProperty().addListener((observableValue, s, t1) -> {
            if (t1.equals("")) profileHolder.setText("");
            else profileHolder.setText(String.valueOf(t1.toUpperCase().charAt(0)));
        });
        Label numberOfMember = new Label((selectedUser.size() + 1) + " member");
        numberOfMember.setTextFill(Color.rgb(170,139,100,0.8));
        numberOfMember.setFont(style.Font0(20));
        numberOfMember.setAlignment(Pos.CENTER_RIGHT);
        style.textFiled0(groupName, "Group Name", 340, 70);
        groupName.setPadding(new Insets(0,30,0,30));
        groupName.setFont(style.Font0(20));
        selectedUser.add(Controller.currentUser);
        VBox showMember = new VBox();
        ScrollPane scrollPane = new ScrollPane(showMember);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMaxSize(370, 250);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background : transparent;");
        for (User user : selectedUser) {
            HBox userHolder = new HBox();
            VBox.setMargin(userHolder, new Insets(0, 10 ,0 ,20));
            userHolder.setPadding(new Insets(10));
            userHolder.setAlignment(Pos.CENTER_LEFT);
            Circle circle = new Circle(30, new ImagePattern(new Image(user.getAvatar())));
            Label username = new Label(user.getUserName());
            username.setPadding(new Insets(0,0,20,20));
            username.setFont(style.Font0(25));
            username.setTextFill(Color.rgb(170,139,100,0.8));
            userHolder.setPrefSize(200, 70);
            userHolder.getChildren().addAll(circle, username);
            showMember.getChildren().add(userHolder);
        }
        groupSet.getChildren().addAll(profileHolder, groupName, numberOfMember, scrollPane);
        userFiled.getChildren().add(groupSet);

    }

    public void setEnvironment(StackPane chatEnvironment, int type, User user, String profileText, int userNumber) {
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
        });
        sendMessage(chatEnvironment, send, chatSender, type, user);
        hBox.getChildren().addAll(chatSender, send);
        style.textFiled0(chatSender, "Message",615, 70);
        chatSender.setPadding(new Insets(0,30, 0, 30));
        chatSender.setFont(style.Font0(20));
        StackPane.setMargin(hBox, new Insets(0,0,15,0));
        StackPane.setAlignment(hBox, Pos.BOTTOM_CENTER);
        HBox heading = new HBox();
        heading.setSpacing(20);
        heading.setPadding(new Insets(0, 0, 0, 25));
        heading.setAlignment(Pos.CENTER_LEFT);
        Circle profile = new Circle(32);
        Label name  = new Label();
        name.setTextFill(Color.rgb(170,139,100,0.8));
        name.setFont(style.Font0(30));
        Label profileHolder = new Label();
        profileHolder.setPrefSize(64, 64);
        profileHolder.setAlignment(Pos.CENTER);
        profileHolder.setTextFill(Color.rgb(170,139,100,0.8));
        profileHolder.setFont(style.Font0(35));
        profileHolder.setStyle("-fx-background-color: rgba(183,181,39,0.5); -fx-background: rgba(183,181,39,0.5); -fx-background-radius: 60;");
        if (type == 2) {
            profile.setFill(new ImagePattern(new Image(user.getAvatar())));
            name.setText(user.getUserName());
            heading.getChildren().addAll(profile, name);
        }
        else if (type == 0 || type == 1) {
            Label number = new Label("( " + userNumber + " member )");
            number.setFont(style.Font0(15));
            number.setTextFill(Color.rgb(170,139,100,0.8));
            name.setText(profileText);
            profileHolder.setText(String.valueOf(profileText.toUpperCase().charAt(0)));
            heading.getChildren().addAll(profileHolder, name, number);
        }
        heading.setPrefSize(855, 85);
        heading.setMaxHeight(85);
        heading.setStyle("-fx-background-color: rgba(170,139,100,0.3); -fx-background: rgba(170,139,100,0.3); -fx-background-radius: 15 15 0 0;");
        StackPane.setAlignment(heading, Pos.TOP_CENTER);
        chatEnvironment.getChildren().addAll(hBox, heading);
    }

    public void sendMessage(StackPane chatEnvironment, Rectangle send, TextField chatSender, int type, User user) {
        ArrayList<String> userReceiver = new ArrayList<>();
        send.setOnMouseClicked(mouseEvent -> {
            if (send.getOpacity() == 1) {
                if (type == 0) {
                    userReceiver.clear();
                    for (User allUser : User.users)
                        Controller.currentUser.setMyChats(new Chat(Controller.currentUser.getUserName(), allUser.getUserName(), chatSender.getText(), ChatType.PUBLIC_CHAT, java.time.LocalDateTime.now().getHour(), java.time.LocalDateTime.now().getMinute()));
                }
                else if (type == 2) {
                    userReceiver.clear();
                    Chat chat = new Chat(Controller.currentUser.getUserName(), user.getUserName(), chatSender.getText(), ChatType.PRIVATE_CHAT, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
                    Controller.currentUser.setMyChats(chat);
                    userReceiver.add(user.getUserName());
                    connection.sendPacket(new SendPacket(Controller.currentUser.getUserName(), userReceiver, ObjectType.Chat, chat));
                    updateChatBox(chatEnvironment, type, user);
                }
                chatSender.setText("");
            }
        });
    }

    public void updateChatBox(StackPane chatEnvironment ,int type, User user) {
        if (chatEnvironment.getChildren().size() == 3) chatEnvironment.getChildren().remove(2);
        VBox vBox = new VBox();
        vBox.setMinSize(610, 540);
        vBox.setAlignment(Pos.BOTTOM_LEFT);
        vBox.setSpacing(5);
        ScrollPane scrollPane = new ScrollPane(vBox);
        vBox.setLayoutY(300);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMaxSize(620, 535);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background : transparent;");
        StackPane.setAlignment(scrollPane, Pos.CENTER);
        StackPane.setMargin(scrollPane, new Insets(0,75,5,0));
        for (Chat chat : Controller.currentUser.getMyChats()) {
            HBox holder = new HBox();
            holder.setSpacing(10);
            holder.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
            holder.setStyle("-fx-background-color: rgba(170,139,100,0.3); -fx-background : rgba(170,139,100,0.3); -fx-background-radius: 10;");
            holder.setPadding(new Insets(10));
            Label textMessage = new Label(chat.getMessageText());
            textMessage.widthProperty().addListener((observableValue, s, t1) -> {
                if (!s.equals(t1)) System.out.println(t1);
            });

            textMessage.setTextFill(Color.rgb(170,139,100,0.8));
            textMessage.setFont(style.Font0(20));
            Label time = new Label(chat.getHours() + ":" + chat.getSeconds());
            time.setTextFill(Color.rgb(170,139,100,0.4));
            time.setFont(style.Font0(12));
            holder.getChildren().addAll(textMessage, time);
            if (chat.getChatType().equals(ChatType.PRIVATE_CHAT) &&
                    (chat.getUserSender().equals(Controller.currentUser.getUserName()) || chat.getUserReceiver().equals(Controller.currentUser.getUserName())))
                vBox.getChildren().add(holder);
        }
        chatEnvironment.getChildren().add(scrollPane);
    }

    public void showRecentChat() {

    }
}
