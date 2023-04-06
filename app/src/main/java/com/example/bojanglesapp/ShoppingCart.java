package com.example.bojanglesapp;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingCart extends MenuItem implements Serializable {

    private ArrayList<MenuItem> cart;
    private double subtotal = 0;
    private double tax = 0 ;
    private double total = 0;


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

    public double getSubtotal() {
        for (int i = 0; i < this.getCartSize(); i++) {
            this.subtotal += this.cart.get(i).getItemPrice();
        }
        return subtotal;
    }

    public double getTax() {
        //calculating off of NC DOR tax of 2%
        this.tax = this.subtotal * .02;
        return tax;
    }

    public double getTotal() {
        this.total = this.subtotal + this.tax;
        return total;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
