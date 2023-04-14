package model;

import model.building.Building;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;

public class Kingdom {
    private Integer population;
    private Integer fearRate;
    private Integer popularity;
    private HashMap<Food , Integer> foods = new HashMap<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private Integer foodRate;
    private Integer taxRate;
    private Flags flag;
    public Kingdom() {
        population = 5;
        popularity = 0;
        foodRate = -2;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getFearRate() {
        return fearRate;
    }

    public void setFearRate(Integer fearRate) {
        this.fearRate = fearRate;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }
}
