/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.sql.DataSource;
import model.Account;
import model.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ROHAN
 */
public class DepositDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public Transaction depositMoney(String customerAadharNo, String customerAccountNo, Double amount, String agentAccountNo){
    
        jdbcTemplate = new JdbcTemplate(dataSource);
        //agent data
        String sql = "select * from ACCOUNT where ACCOUNT_NUMBER = ?";
        Account agentDetails = jdbcTemplate.queryForObject(sql, new Object[]{agentAccountNo}, BeanPropertyRowMapper.newInstance(Account.class));
         
        // Customer data
        String sql1 = "select * from ACCOUNT where AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ?";
        Account customerDetails = jdbcTemplate.queryForObject(sql1, new Object[]{customerAadharNo,customerAccountNo}, BeanPropertyRowMapper.newInstance(Account.class));
        
        Double agentBalance, customerBalance;
        agentBalance = agentDetails.getBalance();
        Double agentCurrentBalance = agentBalance - amount;
        customerBalance = customerDetails.getBalance();
        Double customerCurrentBalance = customerBalance + amount;
        
        String sql2 = "INSERT INTO ROOT.TRANSACTIONS (ACCOUNT_NUMBER, TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE, ?)";
        Integer result = jdbcTemplate.update(sql2, new Object[]{agentAccountNo, "debit", amount, "Transferred to "+customerAccountNo});
        
        String sql3 = "INSERT INTO ROOT.TRANSACTIONS (ACCOUNT_NUMBER, TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE, ?)";
        Integer result1 = jdbcTemplate.update(sql3, new Object[]{customerAccountNo, "credit", amount, "Transfered from "+agentAccountNo});
        
        if(result==1 && result1==1)
        {   
            String sql5 = "update ACCOUNT set BALANCE = ? where ACCOUNT_NUMBER = ?";
            Integer result2 = jdbcTemplate.update(sql5, new Object[]{agentCurrentBalance,agentAccountNo});
            String sql6 = "update ACCOUNT set BALANCE=? where ACCOUNT_NUMBER = ?";
            Integer result3 = jdbcTemplate.update(sql6, new Object[]{customerCurrentBalance,customerAccountNo});
            
            if(result2==1 && result3==1){
            String sql4 = "select * from TRANSACTIONS where ACCOUNT_NUMBER = ? order by rrn desc fetch first 1 rows only";
            Transaction customerTransaction = jdbcTemplate.queryForObject(sql4, new Object[]{customerAccountNo}, BeanPropertyRowMapper.newInstance(Transaction.class));
            return customerTransaction;
            }
            else {
            return new Transaction() ;
            }
        }
        else
        {
            return new Transaction();
        }       
    }
    
//     public Account checkAccount(String customerAadharNo, String customerAccountNo){
//         Account customerDetails1;
//         jdbcTemplate = new JdbcTemplate(dataSource);
//            
//        String sql1 = "select * from ACCOUNT where AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ?";
//         
//         try {
//             customerDetails1 = jdbcTemplate.queryForObject(sql1, new Object[]{customerAadharNo, customerAccountNo}, BeanPropertyRowMapper.newInstance(Account.class));
//         } catch (DataAccessException dataAccessException) {
//             customerDetails1 = new Account();
//         }
//        return customerDetails1;
//        
//       
//        
//        }
    
}
