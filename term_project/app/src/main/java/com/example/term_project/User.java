package com.example.term_project;

public class User {
    private String name;
    private String color;
    private int howManyUnits=0;
    private int howManyTiles=0;

    public User(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getHowManyUnits() {
        return howManyUnits;
    }

    public void addHowManyUnits(int howManyUnits) {
        this.howManyUnits += howManyUnits;
    }

    public int getHowManyTiles() {
        return howManyTiles;
    }

    public void addHowManyTiles(int howManyTiles) {
        this.howManyTiles += howManyTiles;
    }
}
