package model;

import java.util.ArrayList;

public class Kingdom {
    private Integer population;
    private Integer fearRate;
    private Integer popularity;
    private ArrayList<Food> foods = new ArrayList<>();
    private Integer foodRate;
    private Integer taxRate;
    public Kingdom() {

        popularity = 0;
        foodRate = -2;
    }


}
