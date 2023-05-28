package view;

import javafx.css.Size;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Style {

    public Font Font0(int size) {
        return Font.font("Comic Sans MS", FontWeight.BOLD,size);
    }
    public void textFiled0 (TextField textField, String fillText, int width, int height) {
        textField.setBackground(Background.EMPTY);
        textField.setPromptText(fillText);
        textField.setPadding(new Insets(0,30,0,30));
        textField.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(30), BorderStroke.THIN)));
        textField.setMaxWidth(width);
        textField.setPrefHeight(height);
    }
    public void button0 (Button button, String fillText, int width, int height) {
        button.setBackground(Background.fill(Color.WHITE));
        button.setPrefSize(width, height);
        button.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(30), BorderStroke.THIN)));
        button.setText(fillText);
    }
}
