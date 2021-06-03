/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;

import javax.sql.DataSource;
import model.Account;
import model.Card;
import model.Transaction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ROHAN
 */

public class BalanceEnquiryDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public Account getBalance(String aadharNo, String accountNo) {
        String sql = "SELECT * FROM ACCOUNT where AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ? ";
        jdbcTemplate = new JdbcTemplate(dataSource);
       Account balance = jdbcTemplate.queryForObject(sql, new Object[] {aadharNo, accountNo} , BeanPropertyRowMapper.newInstance(Account.class));
      return balance;
    }
    
        public Account readByAccountno(String accountNo) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT * FROM ACCOUNT WHERE ACCOUNT_NUMBER = ?";
        Account account = jdbcTemplate.queryForObject(sql, new Object[]{accountNo}, BeanPropertyRowMapper.newInstance(Account.class));
        return account;
    }
        
        public List<Transaction> readAllByDate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT * FROM TRANSACTIONS where TRANSACTION_DATE = current_date";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Transaction.class));
    }
        
        
        // card
        
     public Account getBalance(String cardNumber,String cardHolderName,String cvv,String expireDate,String pin) {
        jdbcTemplate = new JdbcTemplate(dataSource);
       String sql = "SELECT * FROM CARD where CARD_NUMBER = ? and CARD_HOLDER_NAME = ? and CVV = ? and EXPIRY_DATE = ? and PIN = ?";
       Card customerBalance = jdbcTemplate.queryForObject(sql, new Object[] {cardNumber,cardHolderName, cvv, expireDate, pin} , BeanPropertyRowMapper.newInstance(Card.class));
       String sql1 ="SELECT * FROM ACCOUNT where ACCOUNT_NUMBER = ?";
       Account account= jdbcTemplate.queryForObject(sql1,new Object[]{customerBalance.getAccountNumber()},BeanPropertyRowMapper.newInstance(Account.class));
       return account;
    }
    
}
