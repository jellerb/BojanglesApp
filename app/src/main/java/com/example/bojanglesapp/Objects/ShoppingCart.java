// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp.Objects;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingCart implements Serializable {

    private ArrayList<MenuItem> cart;
    private double subtotal;
    private double tax;
    private double total;
    private double points;

    public ShoppingCart() {
        this.cart = new ArrayList<>();
        this.subtotal = 0;
        this.tax = 0;
        this.total = 0;
        this.points = 0;
    }

    public double getPoints() {
        this.points = this.total;
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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
        this.subtotal = 0;
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

    @NonNull
    @Override
    public String toString() {
        return "ShoppingCart{" +
                "cart=" + cart +
                ", subtotal=" + subtotal +
                ", tax=" + tax +
                ", total=" + total +
                ", points=" + points +
                '}';
    }
}
