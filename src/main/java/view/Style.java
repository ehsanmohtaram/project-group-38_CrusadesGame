package view;

import javafx.css.Size;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Style {

    public Font Font0(int size) {
        return Font.font("Comic Sans MS", FontWeight.BOLD,size);
    }
    public void textFiled0 (TextField textField, String fillText, int width, int height) {
        textField.setBackground(Background.EMPTY);
        textField.setPromptText(fillText);
        textField.setStyle("-fx-text-fill: black; -fx-prompt-text-fill: #484646;");
        textField.setPadding(new Insets(0,30,0,30));
        textField.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        textField.setMaxWidth(width);
        textField.setPrefHeight(height);
    }
    public void button0 (Button button, String fillText, int width, int height,int fontSize) {
        button.setPrefSize(width, height);
        button.setStyle("-fx-text-fill: #484646;");
        button.setStyle("-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 10;");
        button.setBackground(Background.EMPTY);
        button.setText(fillText);
        button.setFont(this.Font0(fontSize));
        button.setOnMouseEntered(mouseEvent -> button.setStyle("-fx-background-color: rgba(185,182,182,0.5); -fx-background-radius: 10;"));
        button.setOnMouseExited(mouseEvent -> button.setStyle("-fx-background-color: rgba(255,255,255,0.5); -fx-background-radius: 10;"));
    }

    public void checkBox0 (Rectangle checkBox) {
        checkBox.setStroke(Color.WHITE);
        checkBox.setHeight(12);
        checkBox.setWidth(12);
        checkBox.setArcWidth(4);
        checkBox.setArcHeight(4);
        checkBox.setFill(Color.TRANSPARENT);
        checkBox.setOnMouseClicked(mouseEvent ->{
            if (checkBox.getFill() instanceof ImagePattern) checkBox.setFill(Color.TRANSPARENT);
            else checkBox.setFill(new ImagePattern(new Image(Style.class.getResource("/images/buttons/tick.png").toExternalForm())));
        });

    }
}
