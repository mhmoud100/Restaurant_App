package com.resturant_app;

import java.util.ArrayList;

public class User {
    private Boolean isAdmin;
    private ArrayList<Cart> carts, orders;


    public User(){}
    public User(Boolean isAdmin, ArrayList<Cart> carts, ArrayList<Cart> orders) {
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

    public ArrayList<Cart> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Cart> carts) {
        this.carts = carts;
    }

    public ArrayList<Cart> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Cart> orders) {
        this.orders = orders;
    }

    public void addtoCart(Cart cart){this.carts.add(cart);}

    public void removeFromCart(Cart cart){this.carts.remove(cart);}

    public void removeAll(){this.carts.clear();}

    public void removeAllorders(){this.orders.clear();}

    public void addtoOrder(ArrayList<Cart> carts){this.orders = carts;}
}
