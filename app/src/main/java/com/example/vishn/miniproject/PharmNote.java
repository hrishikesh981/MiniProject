package com.example.vishn.miniproject;

import java.io.Serializable;

public class PharmNote implements Serializable{
    private String pharm_name;
    private String pharm_phone1;
    private String pharm_phone2;

    private String pharm_address;


    private String pharm_dp;

    public String getPharm_name() {
        return pharm_name;
    }

    public void setPharm_name(String pharm_name) {
        this.pharm_name = pharm_name;
    }

    public String getPharm_phone1() {
        return pharm_phone1;
    }

    public void setPharm_phone1(String pharm_phone1) {
        this.pharm_phone1 = pharm_phone1;
    }

    public String getPharm_phone2() {
        return pharm_phone2;
    }

    public void setPharm_phone2(String pharm_phone2) {
        this.pharm_phone2 = pharm_phone2;
    }

    public String getPharm_address() {
        return pharm_address;
    }

    public void setPharm_address(String pharm_address) {
        this.pharm_address = pharm_address;
    }

    public String getPharm_dp() {
        return pharm_dp;
    }

    public void setPharm_dp(String pharm_dp) {
        this.pharm_dp = pharm_dp;
    }

    public PharmNote(String pharm_name, String pharm_phone1, String pharm_phone2, String pharm_address, String pharm_dp) {

        this.pharm_name = pharm_name;
        this.pharm_phone1 = pharm_phone1;
        this.pharm_phone2 = pharm_phone2;
        this.pharm_address = pharm_address;
        this.pharm_dp = pharm_dp;
    }

    public PharmNote()
    {

    }


}
