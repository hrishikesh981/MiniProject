package com.example.vishn.miniproject;

import java.util.Date;

public class FinancingDTO {
        double amount;
        Date current;
        String medicine;
        String pharmacy;

    public FinancingDTO() {
    }

    public FinancingDTO(double amount, Date current, String medicine, String pharmacy) {
        this.amount = amount;
        this.current = current;
        this.medicine = medicine;
        this.pharmacy = pharmacy;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCurrent() {
        return current;
    }

    public void setCurrent(Date current) {
        this.current = current;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }
}
