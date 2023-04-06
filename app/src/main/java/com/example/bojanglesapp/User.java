package com.example.bojanglesapp;

public class User {
    String user_id, name, email, password, payment;

    public User(String user_id, String name, String email, String password, String payment) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.payment = payment;
    }

    public User(String user_id, String name, String email, String payment) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.payment = payment;
    }

    public User() {
    }

    public String getU_id() {
        return user_id;
    }

    public void setU_id(String u_id) {
        this.user_id = u_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}

