package com.resturant_app;

import java.util.ArrayList;

public class User {
    private Boolean isAdmin;
    private ArrayList<String> carts, orders;

    public User(Boolean isAdmin, ArrayList<String> carts, ArrayList<String> orders) {
        this.isAdmin = isAdmin;
        this.carts = carts;
        this.orders = orders;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public ArrayList<String> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<String> carts) {
        this.carts = carts;
    }

    public ArrayList<String> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<String> orders) {
        this.orders = orders;
    }
}
