package view.controller;

import controller.Controller;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.*;
import java.text.DecimalFormat;


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
}
