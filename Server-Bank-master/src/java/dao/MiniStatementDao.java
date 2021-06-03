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
public class MiniStatementDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
//    public List<MiniStatement> getMiniStatement() {
//        String sql = "SELECT * FROM CUSTOMER_TRANSACTION FETCH FIRST 2 ROWS ONLY";
//        jdbcTemplate = new JdbcTemplate(dataSource); 
//        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Transaction.class));
//    } 
    
    public List<Transaction> getMiniStatementByAccount(String aadharNo, String accountNo) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        
       String sql1 = "SELECT * FROM ACCOUNT where AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ?";
       Account account= jdbcTemplate.queryForObject(sql1, new Object[] {aadharNo, accountNo} , BeanPropertyRowMapper.newInstance(Account.class));
        
      
       if(account!= null){
           String sql2 = "SELECT * FROM TRANSACTIONS WHERE ACCOUNT_NUMBER = ? ORDER BY RRN DESC FETCH FIRST 5 ROWS ONLY";
           return jdbcTemplate.query(sql2, new Object[] {accountNo} , BeanPropertyRowMapper.newInstance(Transaction.class));
           
           //return (List<Transaction>) transaction;
       
    }else
        return null;
    }
    
    
    // card
    
      public List<Transaction> getMiniStatementByAccount(String cardNumber,String cardHolderName,String cvv,String expireDate,String pin) {
        jdbcTemplate = new JdbcTemplate(dataSource);
       String sql = "SELECT * FROM CARD where CARD_NUMBER = ? and CARD_HOLDER_NAME = ? and CVV = ? and EXPIRY_DATE = ? and PIN = ?";
       Card customerBalance = jdbcTemplate.queryForObject(sql, new Object[] {cardNumber,cardHolderName,cvv,expireDate, pin} , BeanPropertyRowMapper.newInstance(Card.class));
       String sql1 ="SELECT * FROM TRANSACTIONS where ACCOUNT_NUMBER = ? order by RRN desc fetch first 5 rows only";
       return jdbcTemplate.query(sql1,new Object[]{customerBalance.getAccountNumber()},BeanPropertyRowMapper.newInstance(Transaction.class));
       
        
}

}

