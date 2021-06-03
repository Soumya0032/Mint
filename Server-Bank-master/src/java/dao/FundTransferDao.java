/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.sql.DataSource;
import model.Account;
import model.Transaction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ROHAN
 */
public class FundTransferDao {
     private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
public Transaction transferMoney( String customerAadhar, Double amount, String customerAccount, String bAadhar, String bAccount) {
     jdbcTemplate = new JdbcTemplate(dataSource);
        //agent data
        String sql = "select * from ACCOUNT where ACCOUNT_NUMBER = ? and AADHAR_NUMBER = ?";
        Account customerDetails = (Account)jdbcTemplate.queryForObject(sql, new Object[]{customerAccount, customerAadhar}, BeanPropertyRowMapper.newInstance(Account.class));
         
        // Customer data
        String sql1 = "select * from ACCOUNT where AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ?";
        Account bDetails = (Account)jdbcTemplate.queryForObject(sql1, new Object[]{bAadhar,bAccount}, BeanPropertyRowMapper.newInstance(Account.class));
        
        Double customerBalance, bBalance;
        customerBalance = customerDetails.getBalance();
        Double customerCurrentBalance = customerBalance - amount;
        bBalance = bDetails.getBalance();
        Double bCurrentBalance = bBalance + amount;
        
        String sql2 = "INSERT INTO ROOT.TRANSACTIONS (ACCOUNT_NUMBER,TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE, ?)";
        Integer result = jdbcTemplate.update(sql2, new Object[]{customerAccount,  "debit", amount, "transferred to"+bAccount});
        
        String sql3 = "INSERT INTO ROOT.TRANSACTIONS (ACCOUNT_NUMBER, TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE, ?)";
        Integer result1 = jdbcTemplate.update(sql3, new Object[]{bAccount, "credit", amount, "transferred from "+customerAccount});
        
        if(result==1 && result1==1)
        {   
            String sql5 = "update ACCOUNT set BALANCE = ? where ACCOUNT_NUMBER = ?";
            Integer result2 = jdbcTemplate.update(sql5, new Object[]{customerCurrentBalance,customerAccount});
            String sql6 = "update ACCOUNT set BALANCE=? where ACCOUNT_NUMBER = ?";
            Integer result3 = jdbcTemplate.update(sql6, new Object[]{bCurrentBalance,bAccount});
            if(result2==1 && result3==1){
            String sql4 = "select * from TRANSACTIONS where ACCOUNT_NUMBER = ? order by rrn desc fetch first 1 rows only";
            Transaction customerTransaction = jdbcTemplate.queryForObject(sql4, new Object[]{customerAccount}, BeanPropertyRowMapper.newInstance(Transaction.class));
            return customerTransaction;
            }
            else {
                Transaction customerTransaction= null;
            return customerTransaction;
            }
        }
        else
        {
            Transaction customerTransaction = null;
            return customerTransaction ;
        }    
    
    
    }
}
