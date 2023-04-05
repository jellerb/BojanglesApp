package com.example.bojanglesapp;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingCart extends MenuItem implements Serializable {

    private ArrayList<MenuItem> cart;


    public ShoppingCart() {
        this.cart = new ArrayList<MenuItem>();
    }

    //quantity
    public void addItem(MenuItem item) {
        this.cart.add(item);
    }

    public int getCartSize() {
        return this.cart.size();
    }

    //I do not think this will work =)
    public ArrayList getCart() {
        return this.cart;
    }

    public void setCart(ArrayList<MenuItem> cart) {
        this.cart = cart;
    }

    //    private MenuItem menuItem;
//    private int quantity;
//
//
//
//    public ShoppingCart(MenuItem menuItem, int quantity) {
//        this.menuItem = menuItem;
//        this.quantity = quantity;
//    }
//
//    public MenuItem getMenuItem() {
//        return menuItem;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setMenuItem(MenuItem menuItem) {
//        this.menuItem = menuItem;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
}
