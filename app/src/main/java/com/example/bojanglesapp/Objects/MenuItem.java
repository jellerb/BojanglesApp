package com.example.bojanglesapp.Objects;

import java.io.Serializable;

public class MenuItem implements Serializable {

    public String name;
    public double price;
    public String ingredients;
    public int calories;


    public MenuItem() {}



    public MenuItem(String itemName, double itemPrice, String ingredients, int calories) {
        this.name = itemName;
        this.price = itemPrice;
        this.ingredients = ingredients;
        this.calories = calories;
    }

    public String getItemName() {
        return name;
    }

    public MenuItem setItemName(String name) {
        this.name = name;
        return this;
    }

    public double getItemPrice() {
        return price;
    }

    public MenuItem setItemPrice(double price) {
        this.price = price;
        return this;
    }

    public String getIngredients() {
        return ingredients;
    }

    public MenuItem setIngredients(String ingredients) {
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
    public String toString() {
        return "MenuItem{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", ingredients='" + ingredients + '\'' +
                ", calories=" + calories +
                '}';
    }
}
