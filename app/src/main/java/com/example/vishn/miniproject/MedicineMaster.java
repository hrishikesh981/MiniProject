package com.example.vishn.miniproject;

public class MedicineMaster {
    String name,after_effect;
    int dosage;
    double strength,weight_mg;

    public MedicineMaster(String name, String after_effect, int dosage, double strength, double weight_mg) {
        this.name = name;
        this.after_effect = after_effect;
        this.dosage = dosage;
        this.strength = strength;
        this.weight_mg = weight_mg;
    }

    public MedicineMaster() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAfter_effect() {
        return after_effect;
    }

    public void setAfter_effect(String after_effect) {
        this.after_effect = after_effect;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getWeight_mg() {
        return weight_mg;
    }

    public void setWeight_mg(double weight_mg) {
        this.weight_mg = weight_mg;
    }
}
