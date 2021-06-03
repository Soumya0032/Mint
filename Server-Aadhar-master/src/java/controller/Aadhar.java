package controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ROHAN
 */
public class Aadhar {
    private String aadharNumber;
    private String name;
    private String dob;
    private String fingerprint;

    public Aadhar() {
    }

    public Aadhar(String aadharNumber, String name, String dob, String fingerprint) {
        this.aadharNumber = aadharNumber;
        this.name = name;
        this.dob = dob;
        this.fingerprint = fingerprint;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    
}
