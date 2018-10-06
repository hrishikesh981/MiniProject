package com.example.vishn.miniproject;

public class Medicine {
    private String name;
    private int stock;
    private double cost;
    private String pharmacy;

    public Medicine(String name, int stock, double cost, String pharmacy) {
        this.name = name;
        this.stock = stock;
        this.cost = cost;
        this.pharmacy = pharmacy;
    }

    public Medicine() {
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
