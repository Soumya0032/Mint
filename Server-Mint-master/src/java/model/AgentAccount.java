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
public class AgentAccount {
    private String agentId;
    private String accountNumber;
    private String aadharNumber;
    private Double balance;
    private String ifscCode;

    public AgentAccount() {
    }

    public AgentAccount(String agentId, String accountNumber, String aadharNumber, Double balance, String ifscCode) {
        this.agentId = agentId;
        this.accountNumber = accountNumber;
        this.aadharNumber = aadharNumber;
        this.balance = balance;
        this.ifscCode = ifscCode;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }
    
    
}
