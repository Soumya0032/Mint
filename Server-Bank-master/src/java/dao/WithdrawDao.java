/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static java.lang.Double.parseDouble;
import javax.sql.DataSource;
import model.Account;
import model.Card;
import model.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ROHAN
 */
public class WithdrawDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public Transaction transfer(String customerAadharNo, String customerAccountNo, Double amount, String agentAccountNo) {
        
        jdbcTemplate = new JdbcTemplate(dataSource);
        
//        Customer Transaction code

       String sql = "SELECT * FROM ACCOUNT where AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ?";
       Account account = jdbcTemplate.queryForObject(sql, new Object[] {customerAadharNo, customerAccountNo} , BeanPropertyRowMapper.newInstance(Account.class));
      
       Transaction transaction = new Transaction();
       
      transaction.setAccountNumber(account.getAccountNumber());
      transaction.setTransactionType("debit");
      transaction.setAmount(amount);
      transaction.setRemark("Transfered To " +agentAccountNo);
      
      Double balance = account.getBalance();
      
      Double availableBalance = balance - amount;
      
      String sql1 = "UPDATE ACCOUNT set BALANCE= ? WHERE AADHAR_NUMBER = ? and ACCOUNT_NUMBER = ?";
      Integer result = jdbcTemplate.update(sql1, new Object[]{availableBalance, customerAadharNo, customerAccountNo}); 
       
   String sql2 = "INSERT INTO ROOT.TRANSACTIONS (ACCOUNT_NUMBER, TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE ,?)";
   Integer result1 = jdbcTemplate.update(sql2, new Object[] {transaction.getAccountNumber(), transaction.getTransactionType(), transaction.getAmount(), transaction.getRemark()});
      
   
//       Agent Transaction code

   String sql3 = "SELECT * FROM ACCOUNT where ACCOUNT_NUMBER = ? ";
   Account account1 = jdbcTemplate.queryForObject(sql3, new Object[] {agentAccountNo}, BeanPropertyRowMapper.newInstance(Account.class));
   
    Transaction transaction1 = new Transaction();
    
      transaction1.setAccountNumber(account1.getAccountNumber());
      transaction1.setTransactionType("credit");
      transaction1.setAmount(amount);
      transaction1.setRemark("Transfered From " +customerAccountNo);
      
      Double agentBalance = account1.getBalance();
      
      Double agentAvailableBalance = agentBalance + amount;
       
      String sql4 = "UPDATE ACCOUNT set BALANCE= ? WHERE ACCOUNT_NUMBER = ?" ;
      Integer result2 = jdbcTemplate.update(sql4, new Object[]{agentAvailableBalance, agentAccountNo}); 
      
   String sql5 = "INSERT INTO ROOT.TRANSACTIONS (ACCOUNT_NUMBER, TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE ,?)";
   Integer result3 = jdbcTemplate.update(sql5, new Object[] {transaction1.getAccountNumber(), transaction1.getTransactionType(), transaction1.getAmount(), transaction1.getRemark()});
      
   
//      Retrieving Transaction Report of customer 

   if(result == 1 && result1 == 1){
       String sql7 = "SELECT * FROM TRANSACTIONS where ACCOUNT_NUMBER = ? ORDER BY RRN DESC FETCH FIRST 1 ROW ONLY ";
        //jdbcTemplate = new JdbcTemplate(dataSource);
       Transaction transactionReport = jdbcTemplate.queryForObject(sql7, new Object[] {customerAccountNo} , BeanPropertyRowMapper.newInstance(Transaction.class));
        
       return transactionReport;
   }
   else{
       return transaction; 
    }
    
}
    
    public String validateBalanceDayWise(String accountNo, Double amount){ 
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sum = "select sum(AMOUNT) from TRANSACTIONS where ACCOUNT_NUMBER = ? and TRANSACTION_DATE = CURRENT_DATE and TRANSACTION_TYPE = 'debit' ";
        
        System.out.print(sum);
        
        String resultSet = jdbcTemplate.queryForObject(sum, new Object[]{accountNo},String.class); 
        
        Double checkAmount = parseDouble(resultSet);
        Double checkCurrentAmount = checkAmount + amount;
        
        String message = "valid";
        
        if(checkCurrentAmount > 30000){
            return message = "invalid";
        }else if(checkCurrentAmount == 30000){
            return message;
        } else
            return message;   
    }
    
    public Account validateBalance(String customerAccountNo){
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sum = "select  * from ACCOUNT where ACCOUNT_NUMBER = ?";
        Account resultSet = null;
        try {
            resultSet = jdbcTemplate.queryForObject(sum, new Object[]{customerAccountNo}, Account.class);
        } catch (DataAccessException dataAccessException) {
            return new Account();
        }
 
          return resultSet;
    }
    
    
    
    // card
     public Transaction rwithdraw(String agentAccount, String cardNumber,String cardHolderName,String cvv,String expireDate, String pin,  double amount){
    jdbcTemplate = new JdbcTemplate( dataSource);
        
      //agent data
      String sql="SELECT * FROM ACCOUNT WHERE ACCOUNT_NUMBER = ?";
      Account agentDetails=(Account)jdbcTemplate.queryForObject(sql,new Object[]{agentAccount},BeanPropertyRowMapper.newInstance(Account.class));
      
      //customer Data
      String sql2="SELECT * FROM CARD WHERE CARD_NUMBER = ? AND CARD_HOLDER_NAME = ? AND CVV = ? AND EXPIRY_DATE = ? AND PIN = ?";
      Card cardCustomer=(Card) jdbcTemplate.queryForObject(sql2, new Object[]{cardNumber,cardHolderName,cvv,expireDate,pin}, BeanPropertyRowMapper.newInstance(Card.class));
      
      String customerAccount = cardCustomer.getAccountNumber();
      
      String sql1="SELECT  * FROM ACCOUNT where ACCOUNT_NUMBER = ?";
      Account customerDetails = (Account)jdbcTemplate.queryForObject(sql1, new Object[]{customerAccount}, BeanPropertyRowMapper.newInstance(Account.class));
      
      double agentBalance, customerBalance;
      
      customerBalance = customerDetails.getBalance();
      double customerCurrentBalance = customerBalance - amount;
      
      agentBalance = agentDetails.getBalance();
      double agentCurrentBalance = agentBalance + amount;
      
      String sql3="INSERT INTO TRANSACTIONS(ACCOUNT_NUMBER, TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES(?, ?, ?, CURRENT_DATE, ?)";
      Integer result = jdbcTemplate.update(sql3, new Object[]{agentAccount, "debit", amount, "transferred from"+customerAccount});
      
      String sql4 = "INSERT INTO TRANSACTIONS(ACCOUNT_NUMBER,  TRANSACTION_TYPE, AMOUNT, TRANSACTION_DATE, REMARK) VALUES (?, ?, ?, CURRENT_DATE, ?)";
      Integer result1 = jdbcTemplate.update(sql4, new Object[]{customerAccount,  "debit", amount, "debited successfully"});
        
       String sql5 = "update ACCOUNT set BALANCE = ? where ACCOUNT_NUMBER = ?";
       Integer result2 = jdbcTemplate.update(sql5, new Object[]{agentCurrentBalance,agentAccount});
       
       String sql6 = "update ACCOUNT set BALANCE=? where ACCOUNT_NUMBER = ?";
       Integer result3 = jdbcTemplate.update(sql6, new Object[]{customerCurrentBalance,customerAccount});
            
       String sql7 = "select * from TRANSACTIONS where ACCOUNT_NUMBER = ? order by RRN_NUMBER desc fetch first 1 rows only";
            
       Transaction transaction = jdbcTemplate.queryForObject(sql7, new Object[]{customerAccount}, BeanPropertyRowMapper.newInstance(Transaction.class));
       return transaction;     
            
    }
}
