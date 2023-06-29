package view.controller;

import controller.Controller;
import controller.TradeController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.*;
import view.Style;

import java.text.DecimalFormat;
import java.util.HashMap;


public class BuildingMenuController {
    public void getResourceAmount(Map gameMap, Label values, ResourceType resourceType) {
        Kingdom kingdom = gameMap.getKingdomByOwner(Controller.currentUser);
        values.setText(kingdom.getResourceAmount(resourceType).toString());
    }

    public void getFoodAmount(Map gameMap, Label value, Food food) {
        Kingdom kingdom = gameMap.getKingdomByOwner(Controller.currentUser);
        value.setText(kingdom.getFoodAmount(food).toString());
    }

    public void getWeaponAmount(Map gameMap, Label value, Weapons weapons) {
        Kingdom kingdom = gameMap.getKingdomByOwner(Controller.currentUser);
        value.setText(kingdom.getWeaponAmount(weapons).toString());
    }

    public void getSellOrBuyPrice(Rectangle rectangle, Label sell, Label buy) {
        ImagePattern pattern = (ImagePattern) rectangle.getFill();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        for (ResourceType resourceType : ResourceType.values()) {
            if (resourceType.getTexture().equals(pattern.getImage())) {
                sell.setText("SELL : " + decimalFormat.format(resourceType.getPrice() * 0.8));
                buy.setText("BUY : " + decimalFormat.format(resourceType.getPrice()));
                return;
            }
        }
        for (Food food : Food.values()) {
            if (food.getTexture().equals(pattern.getImage())) {
                sell.setText("SELL : " + decimalFormat.format(food.getPrice() * 0.8));
                buy.setText("BUY : " + decimalFormat.format(food.getPrice()));
                return;
            }
        }
        for (Weapons weapons : Weapons.values()) {
            if (weapons.getTexture().equals(pattern.getImage())) {
                sell.setText("SELL : " + decimalFormat.format(weapons.getCost() * 0.8));
                buy.setText("BUY : " + decimalFormat.format(weapons.getCost()));
                return;
            }
        }
    }
    public String getBuyOrSellType(Rectangle rectangle) {
        ImagePattern pattern = (ImagePattern) rectangle.getFill();
        for (ResourceType resourceType : ResourceType.values())
            if (resourceType.getTexture().equals(pattern.getImage())) return resourceType.name();
        for (Food food : Food.values())
            if (food.getTexture().equals(pattern.getImage())) return food.name();
        for (Weapons weapons : Weapons.values())
            if (weapons.getTexture().equals(pattern.getImage())) return weapons.name();
        return null;
    }

    public String requestAndDonateManger(Map gameMap, Rectangle flagColor, Rectangle resourceImage, TextField price, TextField value, TextField message) {
        HashMap<String, String> options = new HashMap<>();
        for (Kingdom kingdom : gameMap.getPlayers())
            if (kingdom.getFlag().getFlagColor().equals(flagColor.getFill())) {
                options.put("u", kingdom.getOwner().getUserName()); break;
            }
        for (ResourceType resourceType : ResourceType.values())
            if (resourceType.getTexture().equals(((ImagePattern)resourceImage.getFill()).getImage())) {
                options.put("t", resourceType.name());
            }
        if (message.getText().equals("")) message.setText("New Request By " + Controller.currentUser.getUserName());
        if (price.getText().equals("")) price.setText("0");
        if (value.getText().equals("")) value.setText("1");
        options.put("p", price.getText());
        options.put("a", value.getText());
        options.put("m", message.getText());
        String result = new TradeController(gameMap).newRequest(options);
        if (result.equals("done")) {
            message.setText("");
            price.setText("0");
            value.setText("1");
        }
       return result;
    }


    public void acceptRequest(Style style, Pane mainPane, Pane mapDesignMenu, VBox popUp, Map gameMap, TextField acceptMessage, Integer id) {
        HashMap<String, String> options = new HashMap<>();
        options.put("m", acceptMessage.getText());
        options.put("i", id.toString());
        String result = new TradeController(gameMap).tradeAccept(options);
        mainPane.getChildren().remove(popUp);
        mapDesignMenu.getChildren().get(0).setDisable(false);
        for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
        if (result.equals("done")) result= "Request has sent successfully!";
        VBox popUp0 = new VBox();
        Button ok = new Button();
        style.popUp0(mainPane, popUp0, ok, 20, 20, 450, 70, 180, 50, 100, 15,500, 100, result, 20);
        popUp0.setStyle("-fx-background-color: beige; -fx-background-radius: 20;");
        mapDesignMenu.getChildren().get(0).setDisable(true);
        for (int i = 0; i < mainPane.getChildren().size() - 1; i++) mainPane.getChildren().get(i).setDisable(true);
        ok.setOnMouseClicked(mouseEvent2 -> {
            mainPane.getChildren().remove(popUp0);
            mapDesignMenu.getChildren().get(0).setDisable(false);
            for (int i = 0; i < mainPane.getChildren().size(); i++) mainPane.getChildren().get(i).setDisable(false);
        });
    }
}
