package com.example.vishn.miniproject;

public class PharmNote {
    private String pharm_name;
    private String pharm_contact;
    private String pharm_address;

    public String getPharm_dp() {
        return pharm_dp;
    }

    public void setPharm_dp(String pharm_dp) {
        this.pharm_dp = pharm_dp;
    }

    private String pharm_dp;
    private int priority;

    public PharmNote()
    {

    }
    public PharmNote(String pharm_name, String pharm_contact, String pharm_address, int priority, String pharm_dp)
    {
        this.pharm_name=pharm_name;
        this.pharm_contact=pharm_contact;
        this.pharm_address=pharm_address;
        this.priority=priority;
        this.pharm_dp=pharm_dp;
    }

    public String getPharm_name() {
        return pharm_name;
    }

    public void setPharm_name(String pharm_name) {
        this.pharm_name = pharm_name;
    }

    public String getPharm_contact() {
        return pharm_contact;
    }

    public void setPharm_contact(String pharm_contact) {
        this.pharm_contact = pharm_contact;
    }

    public String getPharm_address() {
        return pharm_address;
    }

    public void setPharm_address(String pharm_address) {
        this.pharm_address = pharm_address;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


}
