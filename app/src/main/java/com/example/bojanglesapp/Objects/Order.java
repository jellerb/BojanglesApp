// Bojangles Application
// BoBerry Biscuits - Group 16
// ITCS 6112 - 051
// Stephanie Karp, Wes Wotring, Jason Ellerbeck

package com.example.bojanglesapp.Objects;

import java.io.Serializable;
import java.util.UUID;
import com.google.firebase.Timestamp;

public class Order implements Serializable {

    private String orderId;
    private ShoppingCart cart;
    private String customerName;
    private String customerEmail;
    private String customerPayment;
    private Timestamp orderedAt;
    private double pointsGained;
    private boolean favoriteStatus;


    public Order() {
        this.orderId = UUID.randomUUID().toString();
        this.favoriteStatus = false;
    }

    public Order(String orderId, ShoppingCart cart, String customerName, String customerEmail, String customerPayment, Timestamp orderedAt, double pointsGained) {
        this.orderId = orderId;
        this.cart = cart;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPayment = customerPayment;
        this.orderedAt = orderedAt;
        this.pointsGained = pointsGained;
        this.favoriteStatus = false;
    }

    public Order(ShoppingCart cart, String customerName, String customerEmail, String customerPayment, double pointsGained) {
        this.orderId = UUID.randomUUID().toString();
        this.cart = cart;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPayment = customerPayment;
        this.pointsGained = pointsGained;
        this.orderedAt = Timestamp.now();
        this.favoriteStatus = false;
    }

    public boolean favoriteStatus() {
        return favoriteStatus;
    }

    public void setFavorite(boolean status) {
        favoriteStatus = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerPayment() {
        return customerPayment;
    }

    public Timestamp getOrderedAt() {
        return orderedAt;
    }

    public double getPointsGained() {
        return pointsGained;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setCustomerPayment(String customerPayment) {
        this.customerPayment = customerPayment;
    }

    public void setOrderedAt() {
        this.orderedAt = Timestamp.now();
    }

    public void setPointsGained(double pointsGained) {
        this.pointsGained = pointsGained;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", cart=" + cart +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerPayment='" + customerPayment + '\'' +
                ", orderedAt=" + orderedAt +
                ", pointsGained=" + pointsGained +
                ", favoriteStatus=" + favoriteStatus +
                '}';
    }
}
