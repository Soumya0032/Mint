/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author ROHAN
 */
public class Card {
    private String   cardNumber;
    private String   customerId;
    private String   cardHolderName;
    private String   accountNumber;
    private String   cvv;
    private String   pin;
    private String   expireDate;
public Card(){}
public Card(String  cardNumber,String   customerId,String cardHolderName,String   accountNumber, String  cvv, String  expireDate,String pin){
        this.cardNumber = cardNumber;
        this.customerId = customerId;
        this.cardHolderName = cardHolderName;
        this.accountNumber = accountNumber;
        this.cvv = cvv;
        this.pin = pin;
        this.expireDate = expireDate;
}
public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
    
}
