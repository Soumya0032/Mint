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
public class BankResponse {
    AccountDetails accountDetails;
   int status;

    public BankResponse(AccountDetails accountDetails, int status) {
        this.accountDetails = accountDetails;
        this.status = status;
    }
   

   
    public BankResponse() {}

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
