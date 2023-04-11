package model;

import model.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;

public class Kingdom {
    private Integer population;
    private Integer fearRate;
    private Integer popularity;
    private HashMap<Food , Integer> foods = new HashMap<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private Integer foodRate;
    private Integer taxRate;
    private Flages flag;
    public Kingdom() {
        popularity = 0;
        foodRate = -2;
    }

}
