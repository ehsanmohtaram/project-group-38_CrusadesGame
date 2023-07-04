package view;

import controller.Connection;
import controller.Controller;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;
import view.controller.LoginMenuController;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChatMenu extends Application {
    private final Style style;
    private Stage stage;
    private final Connection connection;
    private ArrayList<String> userReceiver;
    private ArrayList<User> selectedUser;
    private Timeline timeline;
    private int currentSize;

    public ChatMenu() {
        connection  = LoginMenuController.connection;
        style = new Style();
        currentSize = Controller.currentUser.getMyChats().size();
        userReceiver = new ArrayList<>();
        selectedUser = new ArrayList<>();
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
        Image image = new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm());
        Rectangle back = new Rectangle(70, 70, new ImagePattern(image));
        Rectangle chooseChatType = new Rectangle(60, 60);
        showRecentChat(usernames, searchBox, scrollPane, userFiled, back, chatEnvironment);
        back.setOnMouseClicked(mouseEvent -> {
            if (((ImagePattern)back.getFill()).getImage().getUrl().equals(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm())).getImage().getUrl()))
                new MainMenu().start(stage);
            else {
                searchBox.setText("");
                usernames.getChildren().clear();
                userFiled.getChildren().clear();
                chooseChatType.setFill(null);
                back.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm())));
                showRecentChat(usernames, searchBox, scrollPane, userFiled, back, chatEnvironment);
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
        selectedUser.clear();
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
                userReceiver.clear();
                int id = Chat.getIdForGroup();
                ArrayList<String> chatSelectUser = new ArrayList<>();
                ArrayList<String> connectionUser = new ArrayList<>();
                for (User allUsers : selectedUser) {
                    chatSelectUser.add(allUsers.getUserName());
                    if (!Controller.currentUser.equals(allUsers)) connectionUser.add(allUsers.getUserName());
                }
                Chat chat = new Chat(Controller.currentUser.getUserName(), "Server", "You Invite To This Group By " + Controller.currentUser.getUserName(), ChatType.GROUP, id, groupName.getText(),chatSelectUser,java.time.LocalDateTime.now().getHour(),java.time.LocalDateTime.now().getMinute());
                Controller.currentUser.setMyChats(chat);
                SendPacket sendPacket = new SendPacket(Controller.currentUser.getUserName(), connectionUser, ObjectType.Chat, chat);
                connection.sendPacket(sendPacket);
                setEnvironment(chatEnvironment, 1, null, groupName.getText(), selectedUser.size(), id, groupName.getText(), userReceiver);
                showRecentChat(usernames, searchBox, scrollPane, userFiled, back, chatEnvironment);
            }
        });

    }

    private void setEnvironmentByType(VBox typeOfChats, StackPane chatEnvironment, TextField searchBox, VBox usernames, ScrollPane scrollPane, StackPane userFiled, Rectangle back, Rectangle chooseChatType, StackPane newChatTypeBox) {
        typeOfChats.getChildren().get(0).setOnMouseClicked(mouseEvent -> {
            showRecentChat(usernames, searchBox, scrollPane, userFiled, back, chatEnvironment);
            setEnvironment(chatEnvironment,0,null, "Public Chat", User.users.size(), -1, "", null);
        });
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
                setEnvironment(chatEnvironment,2, user, "" , 2, -1, "", null);
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

    public void setEnvironment(StackPane chatEnvironment, int type, User user, String profileText, int userNumber, int groupId, String groupName, ArrayList<String> selectedUser0) {
        currentSize -= 1;
        if (timeline != null && timeline.getStatus().equals(Animation.Status.RUNNING)) timeline.stop();
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
        sendMessage(chatEnvironment, send, chatSender, type, user, groupId, groupName, selectedUser0);
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
        if (type == 2) {
            timeline = new Timeline(new KeyFrame(Duration.millis(100),
                    actionEvent -> {
                if(currentSize != Controller.currentUser.getMyChats().size()) {
                    updateChatBox(chatEnvironment, 2, user,groupId);
                    currentSize = Controller.currentUser.getMyChats().size();
                }}
            ));
            timeline.setCycleCount(-1);
            timeline.play();
        }
        if (type == 0) {
            timeline = new Timeline(new KeyFrame(Duration.millis(100),
                    actionEvent -> {
                        if(currentSize != Controller.currentUser.getMyChats().size()) {
                            updateChatBox(chatEnvironment, 0, user, groupId);
                            currentSize = Controller.currentUser.getMyChats().size();
                        }}
            ));
            timeline.setCycleCount(-1);
            timeline.play();
        }
        if (type == 1) {
            timeline = new Timeline(new KeyFrame(Duration.millis(100),
                    actionEvent -> {
                        if(currentSize != Controller.currentUser.getMyChats().size()) {
                            updateChatBox(chatEnvironment, 1, user, groupId);
                            currentSize = Controller.currentUser.getMyChats().size();
                        }}
            ));
            timeline.setCycleCount(-1);
            timeline.play();
        }
    }

    public void sendMessage(StackPane chatEnvironment, Rectangle send, TextField chatSender, int type, User user, int id, String groupName, ArrayList<String> selectedUser0) {
        send.setOnMouseClicked(mouseEvent -> {
            if (send.getOpacity() == 1) {
                if (type == 0) {
                    userReceiver.clear();
                    Chat chat = new Chat(Controller.currentUser.getUserName(), "Server", chatSender.getText(), ChatType.PUBLIC_CHAT, -1, "", null,java.time.LocalDateTime.now().getHour(), java.time.LocalDateTime.now().getMinute());
                    Controller.currentUser.setMyChats(chat);
                    for (User allUsers : User.users) if (!allUsers.equals(Controller.currentUser)) userReceiver.add(allUsers.getUserName());
                    connection.sendPacket(new SendPacket(Controller.currentUser.getUserName(), userReceiver, ObjectType.Chat, chat));
                    updateChatBox(chatEnvironment, type, user, id);
                }
                else if (type == 2) {
                    userReceiver.clear();
                    Chat chat = new Chat(Controller.currentUser.getUserName(), user.getUserName(), chatSender.getText(), ChatType.PRIVATE_CHAT, -1, "", null,LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
                    Controller.currentUser.setMyChats(chat);
                    userReceiver.add(user.getUserName());
                    connection.sendPacket(new SendPacket(Controller.currentUser.getUserName(), userReceiver, ObjectType.Chat, chat));
                    updateChatBox(chatEnvironment, type, user, id);
                }
                else if (type == 1) {
                    userReceiver.clear();
                    ArrayList<String> member0 = new ArrayList<>();
                    ArrayList<String> member1 = new ArrayList<>();
                    for (Chat chat : Controller.currentUser.getMyChats())
                        if (chat.getGroupId() == id) {member1 = chat.getGroupMember(); break; }
                    for (String allUsers : member1) {
                        if (!allUsers.equals(Controller.currentUser.getUserName())) member0.add(allUsers);
                    }
                    Chat chat = new Chat(Controller.currentUser.getUserName(), "Server", chatSender.getText(), ChatType.GROUP, id, groupName, member1,LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
                    Controller.currentUser.setMyChats(chat);
                    connection.sendPacket(new SendPacket(Controller.currentUser.getUserName(), member0, ObjectType.Chat, chat));
                    updateChatBox(chatEnvironment, type, user, id);
                }
                chatSender.setText("");
            }
        });
    }

    public void updateChatBox(StackPane chatEnvironment ,int type, User user, int id) {
        if (chatEnvironment.getChildren().size() == 3) chatEnvironment.getChildren().remove(2);
        VBox vBox = new VBox();
        vBox.setMinSize(610, 530);
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
            VBox vBox1 = new VBox();
            vBox1.setAlignment(Pos.CENTER);
            vBox1.setSpacing(4);
            holder.setSpacing(10);
            holder.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
            holder.setStyle("-fx-background-color: rgba(170,139,100,0.3); -fx-background : rgba(170,139,100,0.3); -fx-background-radius: 10;");
            holder.setPadding(new Insets(10));
            Text text = new Text(chat.getMessageText());
            text.setFont(style.Font0(20));
            text.setFill(Color.rgb(170,139,100,0.8));
            Text text1 = new Text(chat.getHours() + ":" + chat.getSeconds());
            text1.setFill(Color.rgb(170,139,100,0.4));
            text1.setFont(style.Font0(12));
            holder.setMaxWidth(text.getLayoutBounds().getWidth() + 30 + text1.getLayoutBounds().getWidth());
            vBox1.getChildren().add(text1);
            holder.getChildren().addAll(text, vBox1);
            holder.setOnMouseClicked(mouseEvent -> {
                setEditEnvironment(mouseEvent,chatEnvironment, chat);
            });
            if (type == 2) {
                if (chat.getChatType().equals(ChatType.PRIVATE_CHAT) &&
                        chat.getUserSender().equals(Controller.currentUser.getUserName()) && chat.getUserReceiver().equals(user.getUserName())) {
                    vBox.getChildren().add(holder);
                }
                if (chat.getChatType().equals(ChatType.PRIVATE_CHAT) &&
                        chat.getUserSender().equals(user.getUserName()) && chat.getUserReceiver().equals(Controller.currentUser.getUserName())) {
                    vBox.getChildren().add(holder);
                    VBox.setMargin(holder, new Insets(0, 0, 0, 600 - holder.getMaxWidth()));
                    holder.setStyle("-fx-background-color: rgba(91,85,60,0.3); -fx-background : rgba(91,85,60,0.3); -fx-background-radius: 10;");
                }
            }
            if (type == 0) {
                if (chat.getChatType().equals(ChatType.PUBLIC_CHAT) && chat.getUserSender().equals(Controller.currentUser.getUserName())) {
                    vBox.getChildren().add(holder);
                }
                if (chat.getChatType().equals(ChatType.PUBLIC_CHAT) && !chat.getUserSender().equals(Controller.currentUser.getUserName())) {
                    Label name = new Label(chat.getUserSender());
                    name.setFont(style.Font0(15));
                    name.setTextFill(Color.rgb(170,139,100,0.4));
                    vBox1.getChildren().add(name);
                    HBox imageAndText = new HBox();
                    imageAndText.setSpacing(5);
                    Circle circle = new Circle(30 , new ImagePattern(new Image(User.getUserByUsername(chat.getUserSender()).getAvatar())));
                    HBox.setMargin(circle, new Insets(8, 0, 0 ,0));
                    imageAndText.getChildren().addAll(holder, circle);
                    vBox.getChildren().add(imageAndText);
                    VBox.setMargin(imageAndText, new Insets(0, 0, 0, 600 - (holder.getMaxWidth() + 65)));
                    holder.setStyle("-fx-background-color: rgba(91,85,60,0.3); -fx-background : rgba(91,85,60,0.3); -fx-background-radius: 10;");
                }
            }
            if (type == 1) {
                if (chat.getChatType().equals(ChatType.GROUP) && chat.getGroupId().equals(id) && chat.getUserSender().equals(Controller.currentUser.getUserName())) {
                    vBox.getChildren().add(holder);
                }
                if (chat.getChatType().equals(ChatType.GROUP) && chat.getGroupId().equals(id) && !chat.getUserSender().equals(Controller.currentUser.getUserName())) {
                    Label name = new Label(chat.getUserSender());
                    name.setFont(style.Font0(15));
                    name.setTextFill(Color.rgb(170,139,100,0.4));
                    vBox1.getChildren().add(name);
                    HBox imageAndText = new HBox();
                    imageAndText.setSpacing(5);
                    Circle circle = new Circle(30 , new ImagePattern(new Image(User.getUserByUsername(chat.getUserSender()).getAvatar())));
                    HBox.setMargin(circle, new Insets(8, 0, 0 ,0));
                    imageAndText.getChildren().addAll(holder, circle);
                    vBox.getChildren().add(imageAndText);
                    VBox.setMargin(imageAndText, new Insets(0, 0, 0, 600 - (holder.getMaxWidth() + 65)));
                    holder.setStyle("-fx-background-color: rgba(91,85,60,0.3); -fx-background : rgba(91,85,60,0.3); -fx-background-radius: 10;");
                }
            }
        }
        chatEnvironment.getChildren().add(scrollPane);
    }

    private void setEditEnvironment(MouseEvent mouseEvent, StackPane chatEnvironment, Chat chat) {
        VBox mainHolder = new VBox();
        mainHolder.setSpacing(10);
        System.out.println(mouseEvent.getScreenX() + " " + mouseEvent.getScreenY());
        HBox emoji = new HBox();
        addEmoji(emoji);
        StackPane.setMargin(mainHolder, new Insets(mouseEvent.getScreenX() - 400,mouseEvent.getScreenY() - 200,0,0));
        emoji.setStyle("-fx-background-color: rgba(91,85,60,0.3); -fx-background : rgba(91,85,60,0.3); -fx-background-radius: 10;");
        emoji.setMaxWidth(200);
        emoji.setPrefHeight(40);
        emoji.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        VBox editBox = new VBox();
        addEditBox(editBox);

        editBox.setMaxWidth(200);
        editBox.setPrefHeight(150);
        editBox.setStyle("-fx-background-color: rgba(91,85,60,0.3); -fx-background : rgba(91,85,60,0.3); -fx-background-radius: 10;");
        editBox.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        mainHolder.getChildren().addAll(emoji, editBox);
        chatEnvironment.getChildren().add(mainHolder);
        chatEnvironment.setOnMouseDragged(mouseEvent1 -> chatEnvironment.getChildren().remove(mainHolder));
    }

    private void addEmoji(HBox emoji) {
        emoji.setAlignment(Pos.CENTER);
        Circle circle0 = new Circle(17, new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/e1.png").toExternalForm())));
        Circle circle1 = new Circle(17, new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/e2.png").toExternalForm())));
        Circle circle2 = new Circle(17, new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/e3.png").toExternalForm())));
        Circle circle3 = new Circle(17, new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/e4.png").toExternalForm())));
        emoji.setSpacing(10);
        emoji.getChildren().addAll(circle0, circle1, circle2, circle3);
    }

    private void addEditBox(VBox editBox) {
        editBox.setAlignment(Pos.CENTER);
        HBox holder0 = new HBox();
        holder0.setAlignment(Pos.CENTER_LEFT);
        holder0.setPadding(new Insets(0, 0, 0, 5));
        holder0.setSpacing(10);
        holder0.setMaxSize(200, 50);
        holder0.setPrefHeight(50);
        Label name0 = new Label("Edit Message");
        name0.setFont(style.Font0(18));
        name0.setTextFill(Color.rgb(170,139,100,0.8));
        Rectangle rectangle0 = new Rectangle(30, 30, new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/edit0.png").toExternalForm())));
        holder0.setOnMouseEntered(event -> holder0.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(128,128,128, 0.4) ; -fx-background: rgba(128,128,128, 0.4);"));
        holder0.setOnMouseExited(event -> holder0.setStyle("-fx-background-radius: 20; -fx-background-color: transparent ; -fx-background: transparent;"));

        holder0.getChildren().addAll(rectangle0,name0);

        HBox holder1 = new HBox();
        holder1.setAlignment(Pos.CENTER_LEFT);
        holder1.setPadding(new Insets(0, 0, 0, 5));
        holder1.setSpacing(10);
        holder1.setMaxSize(200, 50);
        holder1.setPrefHeight(50);
        Label name1 = new Label("Delete For Me");
        name1.setFont(style.Font0(18));
        name1.setTextFill(Color.rgb(170,139,100,0.8));
        Rectangle rectangle1 = new Rectangle(30, 30, new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/delete.png").toExternalForm())));
        holder1.setOnMouseEntered(event -> holder1.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(128,128,128, 0.4) ; -fx-background: rgba(128,128,128, 0.4);"));
        holder1.setOnMouseExited(event -> holder1.setStyle("-fx-background-radius: 20; -fx-background-color: transparent ; -fx-background: transparent;"));
        holder1.getChildren().addAll(rectangle1, name1);

        HBox holder2 = new HBox();
        holder2.setAlignment(Pos.CENTER_LEFT);
        holder2.setPadding(new Insets(0, 0, 0, 5));
        holder2.setSpacing(10);
        holder2.setMaxSize(200, 50);
        holder2.setPrefHeight(50);
        Label name2 = new Label("Delete For all");
        name2.setFont(style.Font0(18));
        name2.setTextFill(Color.rgb(170,139,100,0.8));
        Rectangle rectangle2 = new Rectangle(30, 30, new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/delete.png").toExternalForm())));
        holder2.getChildren().addAll(rectangle2, name2);
        holder2.setOnMouseEntered(event -> holder2.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(128,128,128, 0.4) ; -fx-background: rgba(128,128,128, 0.4);"));
        holder2.setOnMouseExited(event -> holder2.setStyle("-fx-background-radius: 20; -fx-background-color: transparent ; -fx-background: transparent;"));
        editBox.getChildren().addAll(holder0, holder1, holder2);
    }

    public void showRecentChat(VBox usernames, TextField searchBox, ScrollPane scrollPane, StackPane userFiled, Rectangle back, StackPane chatEnvironment) {
        back.setFill(new ImagePattern(new Image(ChatMenu.class.getResource("/images/buttons/back.png").toExternalForm())));
        usernames.getChildren().clear();
        userFiled.getChildren().clear();
        ArrayList<Chat> chats = new ArrayList<>();
        for (Chat chat : Controller.currentUser.getMyChats()) {
            int flag = 0;
            switch (chat.getChatType()) {
                case GROUP: for (Chat chat0 : chats)
                                if (chat0.getGroupId().equals(chat.getGroupId())) { flag = 1; break; }
                            if (flag == 0) chats.add(chat); break;
                case PUBLIC_CHAT: for (Chat chat0 : chats)
                                    if (chat0.getChatType().equals(ChatType.PUBLIC_CHAT)) { flag = 1; break; }
                                if (flag == 0) chats.add(chat); break;
                case PRIVATE_CHAT: for (Chat chat0 : chats)
                                        if (chat0.getChatType().equals(ChatType.PRIVATE_CHAT) &&
                                                (chat0.getUserReceiver().equals(chat.getUserReceiver())) || chat0.getUserReceiver().equals(chat.getUserSender())) { flag = 1; break; }
                                    if (flag == 0) chats.add(chat); break;
            }
        }
        for (Chat chat : chats) {
            User user;
            if (chat.getUserSender().equals(Controller.currentUser.getUserName())) user = User.getUserByUsername(chat.getUserReceiver());
            else user = User.getUserByUsername(chat.getUserSender());
            HBox recentChat = new HBox();
            recentChat.setPrefSize(360, 70);
            User finalUser = user;
            recentChat.setOnMouseClicked(mouseEvent -> {
                if (chat.getChatType().equals(ChatType.PRIVATE_CHAT)) setEnvironment(chatEnvironment,2, finalUser, "" , 2, -1, "", null);
                else if (chat.getChatType().equals(ChatType.PUBLIC_CHAT)) setEnvironment(chatEnvironment,0, null, "Public Chat" , User.users.size(), -1, "", null);
                else if (chat.getChatType().equals(ChatType.GROUP)) setEnvironment(chatEnvironment,1, null, chat.getGroupName() , 2, chat.getGroupId(), chat.getGroupName(), chat.getGroupMember());
                searchBox.setText("");
                usernames.getChildren().clear();
                userFiled.getChildren().clear();
                showRecentChat(usernames, searchBox, scrollPane, userFiled, back, chatEnvironment);
            });
            recentChat.setOnMouseEntered(event -> recentChat.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(128,128,128, 0.4) ; -fx-background: rgba(128,128,128, 0.4);"));
            recentChat.setOnMouseExited(event -> recentChat.setStyle("-fx-background-radius: 20; -fx-background-color: transparent ; -fx-background: transparent;"));
            recentChat.setPadding(new Insets(10));
            recentChat.setAlignment(Pos.CENTER_LEFT);
            Circle profile = new Circle(30);
            Label name  = new Label();
            name.setPadding(new Insets(0,0,0,20));
            name.setTextFill(Color.rgb(170,139,100,0.8));
            name.setFont(style.Font0(25));
            Label profileHolder = new Label();
            profileHolder.setPrefSize(60, 60);
            profileHolder.setAlignment(Pos.CENTER);
            profileHolder.setTextFill(Color.rgb(170,139,100,0.8));
            profileHolder.setFont(style.Font0(30));
            StackPane stackPane = new StackPane(profile, profileHolder);
            stackPane.setAlignment(Pos.CENTER);
            switch (chat.getChatType()) {
                case PRIVATE_CHAT:
                    if (chat.getUserSender().equals(Controller.currentUser.getUserName())) user = User.getUserByUsername(chat.getUserReceiver());
                    else user = User.getUserByUsername(chat.getUserSender());
                    profile.setFill(new ImagePattern(new Image(user.getAvatar())));
                    name.setText(user.getUserName());
                    recentChat.getChildren().addAll(profile, name); break;
                case PUBLIC_CHAT:
                    name.setText("Public Chat");
                    profileHolder.setText("P");
                    profile.setFill(Color.rgb(183,181,39,0.5));
                    recentChat.getChildren().addAll(stackPane, name); break;
                case GROUP:
                    name.setText(chat.getGroupName());
                    profile.setFill(Color.rgb(183,181,39,0.5));
                    profileHolder.setText(String.valueOf(chat.getGroupName().toUpperCase().charAt(0)));
                    recentChat.getChildren().addAll(stackPane, name);
            }
            usernames.getChildren().add(recentChat);
        }
        userFiled.getChildren().add(scrollPane);
    }
}
