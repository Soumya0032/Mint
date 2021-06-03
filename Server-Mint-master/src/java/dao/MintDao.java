/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.sql.DataSource;
import model.Account;
import model.AgentAccount;
import model.AgentTransaction;
import model.Card;
import model.Transaction;
import model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ROHAN
 */
public class MintDao {
     private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public AgentAccount getAccount(String customerAadharNo, String customerAccountNo,Double amount, String agentId){
     jdbcTemplate = new JdbcTemplate(dataSource);
     String sql = "select * from AGENT_ACCOUNT where AGENT_ID = ?";
     AgentAccount agentAccount = jdbcTemplate.queryForObject(sql, new Object[]{agentId}, BeanPropertyRowMapper.newInstance(AgentAccount.class));
     return agentAccount;
    }
    
//    Customer Transaction Code
    public Transaction getCustomerTransaction(String customerAadharNo, String customerAccountNo, Double amount, String agentAccountNo){
        String sql = "SELECT * FROM CUSTOMER_ACCOUNT where AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ?";
       Account account = jdbcTemplate.queryForObject(sql, new Object[] {customerAadharNo, customerAccountNo} , BeanPropertyRowMapper.newInstance(Account.class));
      
       Transaction transaction = new Transaction();
       
      transaction.setAccountNumber(account.getAccountNumber());
      transaction.setTransactionType("transfer");
      transaction.setAmount(amount);
      
      
      Double balance = account.getBalance();
      
      Double availableBalance = balance - amount;
      
      String sql1 = "UPDATE CUSTOMER_ACCOUNT set BALANCE= ? WHERE AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ?";
      Integer result = jdbcTemplate.update(sql1, new Object[]{availableBalance, customerAadharNo, customerAccountNo}); 
       
   String sql2 = "INSERT INTO ROOT.CUSTOMER_TRANSACTION (ACCOUNT_NUMBER, TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE ,?)";
   Integer result1 = jdbcTemplate.update(sql2, new Object[] {transaction.getAccountNumber(), transaction.getTransactionType(), transaction.getAmount(), "Transferd to " +agentAccountNo});
    
   
   if(result == 1 && result1 == 1){
       String sql7 = "SELECT * FROM CUSTOMER_TRANSACTION where ACCOUNT_NUMBER = ? ORDER BY RRN DESC FETCH FIRST 1 ROW ONLY ";
        
       Transaction transactionReport = jdbcTemplate.queryForObject(sql7, new Object[] {customerAccountNo} , BeanPropertyRowMapper.newInstance(Transaction.class));

       return transactionReport;
   }
   else{
       return transaction; 
    }
    }

//    Agent Transaction Code
    
    public AgentTransaction getAgentTransaction(String customerAccountNo,String agentAccountNo, Double amount) {
        String sql3 = "SELECT * FROM AGENT_ACCOUNT where ACCOUNT_NUMBER = ? ";
        AgentAccount account1 = jdbcTemplate.queryForObject(sql3, new Object[] {agentAccountNo}, BeanPropertyRowMapper.newInstance(AgentAccount.class));
   
    AgentTransaction agentTransaction = new AgentTransaction();
    
      agentTransaction.setAccountNumber(account1.getAccountNumber());
      agentTransaction.setTransactionType("credit");
      agentTransaction.setAmount(amount);
      agentTransaction.setRemark("Transfered From " +customerAccountNo);
      
      Double agentBalance = account1.getBalance();
      
      Double agentAvailableBalance = agentBalance + amount;
       
      String sql4 = "UPDATE AGENT_ACCOUNT set BALANCE= ? WHERE ACCOUNT_NUMBER = ?" ;
      Integer result2 = jdbcTemplate.update(sql4, new Object[]{agentAvailableBalance, agentAccountNo}); 
      
    String sql5 = "INSERT INTO ROOT.AGENT_TRANSACTION (ACCOUNT_NUMBER, AMOUNT, TRANSACTION_TYPE, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE ,?)";
    Integer result3 = jdbcTemplate.update(sql5, new Object[] {agentTransaction.getAccountNumber(), agentTransaction.getAmount(), agentTransaction.getTransactionType(), "Transferd from " + customerAccountNo});
    
       if(result2 == 1 && result3 == 1){
       String sql7 = "SELECT * FROM AGENT_TRANSACTION where ACCOUNT_NUMBER = ? ORDER BY RRN DESC FETCH FIRST 1 ROW ONLY ";
        
       AgentTransaction agentTransactionReport = jdbcTemplate.queryForObject(sql7, new Object[] {agentAccountNo} , BeanPropertyRowMapper.newInstance(AgentTransaction.class));
        
       return agentTransactionReport;
   }
   else{
       return agentTransaction; 
    }
    
    }
    
    
    
    public AgentAccount getAccount(String agentId) {
     jdbcTemplate = new JdbcTemplate(dataSource);
     String sql = "select * from AGENT_ACCOUNT where AGENT_ID = ?";
     AgentAccount agentAccount = jdbcTemplate.queryForObject(sql, new Object[]{agentId}, BeanPropertyRowMapper.newInstance(AgentAccount.class));
     return agentAccount;
    }
    
    public void updateAgentTransaction(String agentId,String customerAccount,Double amount){
    jdbcTemplate = new JdbcTemplate(dataSource);
    String sql = "select * from AGENT_ACCOUNT where AGENT_ID = ?";
    AgentAccount agentDetails = jdbcTemplate.queryForObject(sql, new Object[]{agentId}, BeanPropertyRowMapper.newInstance(AgentAccount.class));
    
   Double avl = agentDetails.getBalance();
   Double currentBalance = avl - amount;
   
    String sql1 = "INSERT INTO ROOT.AGENT_TRANSACTION (ACCOUNT_NUMBER, AMOUNT, TRANSACTION_TYPE, TRANSACTION_DATE, REMARK)VALUES (?, ?, ?, CURRENT_DATE, ?)";
    Integer result = jdbcTemplate.update(sql1, new Object[]{agentDetails.getAccountNumber(), amount, "transfer", "Transferred to "+customerAccount});

    String sql2 = "update AGENT_ACCOUNT set BALANCE = ? where ACCOUNT_NUMBER  = ?";
    Integer result1 = jdbcTemplate.update(sql2, new Object[]{currentBalance, agentDetails.getAccountNumber()});
    }
    
    public void updateCustomerTransaction(String agentAccount,String customerAadhar,String customerAccount,Double amount){
        jdbcTemplate = new JdbcTemplate(dataSource);
      String sql = "select * from CUSTOMER_ACCOUNT where ACCOUNT_NUMBER = ? and AADHAR_NUMBER = ?";
      Account customerDetails = jdbcTemplate.queryForObject(sql, new Object[]{customerAccount, customerAadhar}, BeanPropertyRowMapper.newInstance(Account.class));
       
      Double avl = customerDetails.getBalance();
   Double currentBalance = avl + amount;
   
    String sql1 = "INSERT INTO ROOT.CUSTOMER_TRANSACTION (ACCOUNT_NUMBER, TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK)VALUES (?, ?, ?, CURRENT_DATE, ?)";
    Integer result = jdbcTemplate.update(sql1, new Object[]{customerDetails.getAccountNumber(), "transfer", amount, "Transferred from "+agentAccount});

    String sql2 = "update CUSTOMER_ACCOUNT set BALANCE = ? where ACCOUNT_NUMBER  = ?";
    Integer result1 = jdbcTemplate.update(sql2, new Object[]{currentBalance, customerDetails.getAccountNumber()});
   }
   
    
    
public List<AgentTransaction> readAllByDate() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT * FROM AGENT_TRANSACTION where TRANSACTION_DATE = current_date";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AgentTransaction.class));
    }
    
    public AgentTransaction readAllByRrn(String rrn) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT * FROM AGENT_TRANSACTION WHERE RRN = ?";
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(AgentTransaction.class), new Object[]{rrn});
    }

    public Transaction transferMoney(String customerAadhar, Double amount, String customerAccount, String bAccount,  String bAadhar) {
     jdbcTemplate = new JdbcTemplate(dataSource);
        //agent data
        String sql = "select * from CUSTOMER_ACCOUNT where ACCOUNT_NUMBER = ? and AADHAR_NUMBER = ?";
        Account customerDetails = (Account)jdbcTemplate.queryForObject(sql, new Object[]{customerAccount, customerAadhar}, BeanPropertyRowMapper.newInstance(Account.class));
         
        // Customer data
        String sql1 = "select * from CUSTOMER_ACCOUNT where AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ?";
        Account bDetails = (Account)jdbcTemplate.queryForObject(sql1, new Object[]{bAadhar,bAccount}, BeanPropertyRowMapper.newInstance(Account.class));
        
        Double customerBalance, bBalance;
        customerBalance = customerDetails.getBalance();
        Double customerCurrentBalance = customerBalance - amount;
        bBalance = bDetails.getBalance();
        Double bCurrentBalance = bBalance + amount;
        
        String sql2 = "INSERT INTO ROOT.CUSTOMER_TRANSACTION (ACCOUNT_NUMBER,TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE, ?)";
        Integer result = jdbcTemplate.update(sql2, new Object[]{customerAccount,  "debit", amount, "transferred to"+bAccount});
        
        String sql3 = "INSERT INTO ROOT.CUSTOMER_TRANSACTION (ACCOUNT_NUMBER, TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE, ?)";
        Integer result1 = jdbcTemplate.update(sql3, new Object[]{bAccount, "credit", amount, "transferred from "+customerAccount});
        
        if(result==1 && result1==1)
        {   
            String sql5 = "update CUSTOMER_ACCOUNT set BALANCE = ? where ACCOUNT_NUMBER = ?";
            Integer result2 = jdbcTemplate.update(sql5, new Object[]{customerCurrentBalance,customerAccount});
            String sql6 = "update CUSTOMER_ACCOUNT set BALANCE=? where ACCOUNT_NUMBER = ?";
            Integer result3 = jdbcTemplate.update(sql6, new Object[]{bCurrentBalance,bAccount});
            if(result2==1 && result3==1){
            String sql4 = "select * from CUSTOMER_TRANSACTION where ACCOUNT_NUMBER = ? order by rrn desc fetch first 1 rows only";
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
         
   
    
    public int forgetpass(String agentId,String password){
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sqlPass="Update USERLOGIN set PASSWORD=? where AGENT_ID=?";
        int result=jdbcTemplate.update(sqlPass,new Object[]{password, agentId});
        return result;
    }
    
    
    
    
    
    
    // ------------------- card transaction -------------------
        
    public AgentAccount getAccountDetails(String agentId) {
     jdbcTemplate = new JdbcTemplate(dataSource);
     String sql = "select * from AGENT_ACCOUNT where AGENT_ID = ?";
     AgentAccount agentAccount = jdbcTemplate.queryForObject(sql, new Object[]{agentId}, BeanPropertyRowMapper.newInstance(AgentAccount.class));
     return agentAccount;
    }
    
    public void updateAgentTransaction(String agentId,String cardNumber,String cardHolderName,String cvv,String expireDate, String pin, Double amount){
    jdbcTemplate = new JdbcTemplate(dataSource);
    String sql = "select * from AGENT_ACCOUNT where AGENT_ID = ?";
    AgentAccount agentDetails = jdbcTemplate.queryForObject(sql, new Object[]{agentId}, BeanPropertyRowMapper.newInstance(AgentAccount.class));
    
    String sql2 = "select * from CARD where CARD_NUMBER = ?  and CARD_HOLDER_NAME = ? and CVV = ? and EXPIRY_DATE = ? and PIN = ?";
    Card cardAgent = jdbcTemplate.queryForObject(sql2, new Object[]{cardNumber,cardHolderName,cvv,expireDate, pin}, BeanPropertyRowMapper.newInstance(Card.class));    
    
    String customerAccount = cardAgent.getAccountNumber();
   
   Double avl = agentDetails.getBalance();
   Double currentBalance = avl + amount;
   
    String sql1 = "INSERT INTO AGENT_TRANSACTION (ACCOUNT_NUMBER, AMOUNT, TRANSACTION_TYPE, TRANSACTION_DATE, REMARK)VALUES (?, ?, ?, CURRENT_DATE, ?)";
    Integer result = jdbcTemplate.update(sql1, new Object[]{agentDetails.getAccountNumber(), amount, "transfer", customerAccount});

    String sql3 = "update AGENT_ACCOUNT set BALANCE = ? where ACCOUNT_NUMBER  = ?";
    Integer result1 = jdbcTemplate.update(sql3, new Object[]{currentBalance, agentDetails.getAccountNumber()});
    }
    
    public void updateCustomerTransaction(String agentAccount,String cardNumber,String cardHolderName,String cvv,String expireDate,String pin,Double amount){
    jdbcTemplate = new JdbcTemplate(dataSource);
    
    String sql2 = "select * from CARD where CARD_NUMBER = ?  and CARD_HOLDER_NAME = ? and CVV = ? and EXPIRY_DATE = ? and PIN = ?";
    Card customerDetails = jdbcTemplate.queryForObject(sql2, new Object[]{cardNumber,cardHolderName,cvv,expireDate, pin}, BeanPropertyRowMapper.newInstance(Card.class));    
    
   String customerAccount = customerDetails.getAccountNumber();  
      
      String sql4="SELECT  * FROM CUSTOMER_ACCOUNT where ACCOUNT_NUMBER = ?";
      Account customerDetail = (Account)jdbcTemplate.queryForObject(sql4, new Object[]{customerAccount}, BeanPropertyRowMapper.newInstance(Account.class));
      
    Double avl = customerDetail.getBalance();
   Double currentBalance = avl - amount;
   
    String sql1 = "INSERT INTO CUSTOMER_TRANSACTION (ACCOUNT_NUMBER, AMOUNT, TRANSACTION_TYPE, TRANSACTION_DATE, REMARK)VALUES (?, ?, ?, CURRENT_DATE, ?)";
    Integer result = jdbcTemplate.update(sql1, new Object[]{customerAccount, amount, "transfer",agentAccount});

    String sql3 = "update CUSTOMER_ACCOUNT set BALANCE = ? where ACCOUNT_NUMBER  = ?";
    Integer result1 = jdbcTemplate.update(sql3, new Object[]{currentBalance, customerDetails.getAccountNumber()});
    
     }
    
    
    //updating customer mobile number
    public Integer updateNumber(String number, String customerAadhar){
        
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sqlPass="Update CUSTOMER set MOBILE_NUMBER=? where AADHAR_NUMBER=?";
        int result=jdbcTemplate.update(sqlPass,new Object[]{number, customerAadhar});
        return result;
    }      
}
