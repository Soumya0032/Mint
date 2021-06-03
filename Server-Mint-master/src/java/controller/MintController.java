/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.MintDao;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import model.Account;
import model.AgentAccount;
import model.AgentTransaction;

import model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ROHAN
 */
@RestController
@RequestMapping("/")
public class MintController {
    MintDao mintDao;
    private String baseuri = "http://localhost:8080/Bank";  
    
    private static final String encryptionKey = "ABCDEFGHIJKLMNOP";
    private static final String characterEncoding = "UTF-8";
    private static final String cipherTransformation = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";

    public void setMintDao(MintDao mintDao) {
        this.mintDao = mintDao;
    }
    
    
       
     @RequestMapping(value = "/miniStatement/{aadharNo}/{accountNo}", method = RequestMethod.GET)
      public List<Transaction> getMiniStatement(@PathVariable("aadharNo") String aadharNo, @PathVariable("accountNo") String accountNo) {
      RestTemplate restTemplate = new RestTemplate();      
      ResponseEntity<Transaction[]> response = restTemplate.getForEntity(baseuri + "/miniStatement/{aadharNo}/{accountNo}", Transaction[].class,new Object[] {aadharNo, accountNo} );
        List<Transaction> list = Arrays.asList(response.getBody());
        return list;
    }
    
     @RequestMapping(value = "/balance/{aadharNo}/{accountNo}", method = RequestMethod.GET)
      public Account getBalanceByAccount(@PathVariable("aadharNo") String aadharNo, @PathVariable("accountNo") String accountNo) {
      RestTemplate restTemplate = new RestTemplate();
      Account balance = restTemplate.getForObject(baseuri + "/balanceEnquiry/{aadharNo}/{accountNo}", Account.class, new Object[] {decrypt(aadharNo), decrypt(accountNo)});
      return balance;
    }
      
      
      @RequestMapping(value = "/withdraw/{customerAadharNo}/{customerAccountNo}/{amount}/{agentId}", method = RequestMethod.GET)
      public Transaction withdrawMoney(@PathVariable("customerAadharNo") String customerAadharNo, @PathVariable("customerAccountNo") String customerAccountNo, @PathVariable("amount") Double amount, @PathVariable("agentId") String agentId) {
      
      
      AgentAccount account = mintDao.getAccount(customerAadharNo, customerAccountNo, amount, agentId);
      
      String agentAccountNo = account.getAccountNumber();
      
      RestTemplate restTemplate = new RestTemplate();
     
          Transaction withdraw = null;
          try {
              withdraw = restTemplate.getForObject(baseuri + "/withdrawMoney/{customerAadharNo}/{customerAccountNo}/{amount}/{agentAccountNo}", Transaction.class, new Object[]{customerAadharNo, customerAccountNo, amount, agentAccountNo});
          } catch (Exception e) {
              withdraw = new Transaction();
          }
      AgentTransaction agentTransaction = mintDao.getAgentTransaction(customerAccountNo, agentAccountNo, amount);
      
      String remark = withdraw.getRemark();
      
          try {
              mintDao.getCustomerTransaction(customerAadharNo, withdraw.getAccountNumber(), withdraw.getAmount(), agentAccountNo);
          } catch (Exception e) {
        }
      return withdraw;
    }
      
      
    @RequestMapping(value="/deposit/{customerAadhar}/{customerAccount}/{amount}/{agentId}", method = RequestMethod.GET)
    public Transaction depositMoney( @PathVariable String customerAadhar, @PathVariable String customerAccount, @PathVariable Double amount, @PathVariable String agentId){
        
        System.out.print(customerAadhar + customerAccount);
        
      AgentAccount agentDetails = mintDao.getAccount(agentId);
      String agentAccount = agentDetails.getAccountNumber();
      
       mintDao.updateAgentTransaction(agentId, decrypt(customerAccount), amount);
      
      
        RestTemplate restTemplate = new RestTemplate();
        
        Transaction customerTransaction = restTemplate.getForObject(baseuri + "/depositMoney/{customerAadhar}/{customerAccount}/{amount}/{agentAccount}", Transaction.class, new Object[] {decrypt(customerAadhar), decrypt(customerAccount) , amount, agentAccount});     
       try{
        mintDao.updateCustomerTransaction(agentAccount, decrypt(customerAadhar), decrypt(customerAccount), amount);
       } catch(Exception e){
    
       }       
        return customerTransaction;      
    }
    
    
    @RequestMapping(value = "/getAccountDetails/{agentId}", method = RequestMethod.GET)
      public Account getBalanceByAccount(@PathVariable("agentId") String agentId) {
      RestTemplate restTemplate = new RestTemplate();
     
      
      AgentAccount agentAccount = mintDao.getAccount(agentId);
      String accountNo = agentAccount.getAccountNumber();

      Account balance = restTemplate.getForObject(baseuri + "/getAgentBalance/{accountNo}", Account.class, new Object[] {accountNo});
      return balance;
    }
    
    @RequestMapping(value = "/eod", method = RequestMethod.GET)
    public List<AgentTransaction> getDateBanks() {
//        List<AgentTransaction> list = null;
//        try {
          List<AgentTransaction>  list = mintDao.readAllByDate();
//        } catch (Exception e) {
//        }
        return list;
    }
    
    
    @RequestMapping(value = "/getByRrn/{rrn}", method = RequestMethod.GET)
    public AgentTransaction getRrnBanks(@PathVariable String rrn) {
        AgentTransaction tr = null;
        try {
            tr = mintDao.readAllByRrn(rrn);
        } catch (Exception e) {
            tr = new AgentTransaction();
        }
        return tr;
    }
        
    @RequestMapping(value = "/fundTransfer/{customerAadhar}/{amount}/{customerAccount}/{bAccount}/{bAadhar}", method = RequestMethod.GET)
    public Transaction fundTransfer(@PathVariable String customerAadhar,@PathVariable Double amount, @PathVariable String customerAccount,@PathVariable String bAccount, @PathVariable String bAadhar ){
    
        Transaction customerTransaction;

            mintDao.transferMoney(customerAadhar,amount,customerAccount, bAccount, bAadhar);

            RestTemplate restTemplate = new RestTemplate();
    Transaction tr = restTemplate.getForObject(baseuri + "/transfer/{customerAadhar}/{amount}/{customerAccount}/{bAccount}/{bAadhar}", Transaction.class, new Object[]{customerAadhar,amount,customerAccount, bAccount, bAadhar});
    
    return tr;
    }
    
    //updating cutomer phone number
    @RequestMapping(value = "/mobileSeeding/{customerAadhar}/{number}", method = RequestMethod.GET)
    public Integer updateMobileNumber(@PathVariable String customerAadhar, @PathVariable String number){
        Integer result = mintDao.updateNumber(number, customerAadhar); 
        return result;
}
 
    @RequestMapping(value="/resetPassword/{agentId}/{password}",method=RequestMethod.GET)
    public int changePass(@PathVariable String agentId, @PathVariable String password){
        int result = mintDao.forgetpass(agentId, password);
        return result;
    }   
    
    
    
    // -------------------------- card transaction --------------------------------


    @RequestMapping(value = "/cBalanceEnquiry/{cardNumber}/{cardHolderName}/{cvv}/{expireDate}/{pin}" , method = RequestMethod.GET)
public Account getBalanceByAccount(@PathVariable String cardNumber,@PathVariable String cardHolderName, @PathVariable String cvv, @PathVariable String expireDate, @PathVariable String pin) {
RestTemplate restTemplate = new RestTemplate();
Account account = restTemplate.getForObject(baseuri + "/cardBalanceEnquiry/{cardNumber}/{cardHolderName}/{cvv}/{expireDate}/{pin}", Account.class,new Object[] {cardNumber,cardHolderName,cvv,expireDate,pin});
return account;
    }

@RequestMapping(value = "/cMiniStatement/{cardNumber}/{cardHolderName}/{cvv}/{expireDate}/{pin}", method= RequestMethod.GET)
public List<Transaction> getminiStatement(@PathVariable String cardNumber,@PathVariable String cardHolderName,@PathVariable String cvv, @PathVariable String expireDate, @PathVariable String pin) {
RestTemplate restTemplate = new RestTemplate();
ResponseEntity<Transaction[]> response = restTemplate.getForEntity(baseuri+"/cardMiniStatement/{cardNumber}/{cardHolderName}/{cvv}/{expireDate}/{pin}",Transaction[].class, new Object[] {cardNumber,cardHolderName,cvv,expireDate,pin});
 List<Transaction> list = Arrays.asList(response.getBody());
return list;
}

 @RequestMapping(value="/rupaywithdraw/{agentId}/{cardNumber}/{cardHolderName}/{cvv}/{expireDate}/{pin}/{amount}", method = RequestMethod.GET)
    public Transaction withdrawMoney(@PathVariable String agentId, @PathVariable String cardNumber,@PathVariable String cardHolderName,@PathVariable String cvv, @PathVariable String expireDate, @PathVariable String pin, @PathVariable Double amount){
        
        AgentAccount agentDetails = mintDao.getAccountDetails(agentId);
      String agentAccount = agentDetails.getAccountNumber();
       mintDao.updateAgentTransaction(agentId, cardNumber,cardHolderName,cvv,expireDate, pin, amount);
      RestTemplate restTemplate = new RestTemplate();
        Transaction customerTransaction = restTemplate.getForObject(baseuri + "/rupaywithdraw/{agentAccount}/{cardNumber}/{cardHolderName}/{cvv}/{expireDate}/{pin}/{amount}", Transaction.class, new Object[] {agentAccount, cardNumber,cardHolderName,cvv,expireDate, pin, amount});     
        mintDao.updateCustomerTransaction(agentAccount, cardNumber,cardHolderName,cvv,expireDate, pin, amount);
        return customerTransaction;      
    }

    
    //    Encryption & Decryption Logic
    
    public static String encrypt(String plainText) {
        String encryptedText = "";
        try {
            Cipher cipher   = Cipher.getInstance(cipherTransformation);
            byte[] key      = encryptionKey.getBytes(characterEncoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedText = encoder.encodeToString(cipherText);

        } catch (Exception E) {
            System.err.println("Encrypt Exception : "+E.getMessage());
        }
        return encryptedText;
    }


    public static String decrypt(String encryptedText) {
        String decryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(characterEncoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(encryptedText.getBytes("UTF8"));
            decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

        } catch (Exception E) {
            System.err.println("decrypt Exception : "+E.getMessage());
        }
        return decryptedText;
    }

}
