package view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class Style {

    public Font Font0(int size) {
        return Font.font("Comic Sans MS", FontWeight.BOLD,size);
    }
    public void textFiled0 (TextField textField, String fillText, int width, int height) {
        textField.setBackground(Background.EMPTY);
        textField.setPromptText(fillText);
        textField.setStyle("-fx-text-fill: rgba(170,139,100,0.8); -fx-prompt-text-fill: rgba(86,73,57,0.8);");
        textField.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        textField.setMaxWidth(width);
        textField.setMinWidth(width);
        textField.setPrefHeight(height);
    }
    public void button0 (Button button, String fillText, int width, int height) {
        button.setPrefSize(width, height);
        button.setStyle("-fx-text-fill: rgba(170,139,100,1);");
        button.setStyle("-fx-background-color: rgba(170,139,100,0.8); -fx-background-radius: 10;");
        button.setBackground(Background.EMPTY);
        button.setText(fillText);
        button.setOnMouseEntered(mouseEvent -> button.setStyle("-fx-background-color: rgba(185,182,182,0.5); -fx-background-radius: 10;"));
        button.setOnMouseExited(mouseEvent -> button.setStyle("-fx-background-color: rgba(170,139,100,0.8); -fx-background-radius: 10;"));
    }

    public void checkBox0 (Rectangle checkBox) {
        checkBox.setStroke(Color.rgb(170,139,100,0.8));
        checkBox.setHeight(15);
        checkBox.setWidth(15);
        checkBox.setArcWidth(4);
        checkBox.setArcHeight(4);
        checkBox.setFill(Color.TRANSPARENT);
        checkBox.setOnMouseClicked(mouseEvent ->{
            if (checkBox.getFill() instanceof ImagePattern) checkBox.setFill(Color.TRANSPARENT);
            else checkBox.setFill(new ImagePattern(new Image(Style.class.getResource("/images/buttons/tick.png").toExternalForm())));
        });

    }

    public void popUp0(Pane pane, VBox popUp, Button ok,double spacing, double padding, double popUpWidth, double popUpHeight, double textFlowWidth, double textFlowHeight, int buttonWidth, int buttonHeight, double positionX, double positionY, String result, int fontSize) {
        popUp.setSpacing(spacing);
        popUp.setPadding(new Insets(padding));
        popUp.setAlignment(Pos.TOP_CENTER);
        popUp.setStyle("-fx-background-color: transparent; -fx-background-radius: 20;");
        popUp.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(20), BorderStroke.THIN)));
        popUp.setPrefSize(popUpWidth, popUpHeight);
        popUp.setLayoutX(positionX);
        popUp.setLayoutY(positionY);
        TextFlow error = new TextFlow();
        error.setLineSpacing(20);
        error.setPrefSize(textFlowWidth, textFlowHeight);
        Text text = new Text(result);
        text.setFill(Color.rgb(170,139,100,0.6));
        text.setFont(Font0(fontSize));
        error.getChildren().add(text);
        ok.setFont(Font0(fontSize));
        button0(ok, "OK", buttonWidth, buttonHeight);
        popUp.getChildren().addAll(error,ok);
        pane.getChildren().add(popUp);
    }



    public void inputTransition(Pane pane) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setNode(pane);
        fadeTransition.setCycleCount(1);
    }
}
