package com.example.bojanglesapp;

public class MenuItem implements Comparable{

    public String itemName;
    public double itemPrice;
    public String[] ingredients;
    public int calories;


    public MenuItem() {}



    public MenuItem(String itemName, double itemPrice, String[] ingredients, int calories) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.ingredients = ingredients;
        this.calories = calories;
    }

    public String getItemName() {
        return itemName;
    }

    public MenuItem setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public MenuItem setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
        return this;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public MenuItem setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public int getCalories() {
        return calories;
    }

    public MenuItem setCalories(int calories) {
        this.calories = calories;
        return this;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
