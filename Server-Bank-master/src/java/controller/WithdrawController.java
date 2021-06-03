/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.WithdrawDao;
import model.Transaction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ROHAN
 */
@RestController
@RequestMapping("/")
public class WithdrawController {
    private WithdrawDao withdrawDao;
    
    @RequestMapping(value = "/withdrawMoney/{customerAadharNo}/{customerAccountNo}/{amount}/{agentAccountNo}", method = RequestMethod.GET)
    public Transaction withdrawMoney(@PathVariable String customerAadharNo, @PathVariable String customerAccountNo, @PathVariable Double amount, @PathVariable String agentAccountNo ){
//     String result = withdrawDao.validateBalanceDayWise(customerAccountNo, amount);
//        Account balance = withdrawDao.validateBalance(customerAccountNo);
        
//           if(balance.getBalance() < amount){
//              
//            } else if(result.equals("valid balance")){
//                
//            }
    
        Transaction withdraw = null ;
       
          try {
//              String result = withdrawDao.validateBalanceDayWise(customerAccountNo, amount);
//              
//              if(result.equals("valid")){
//                  
              withdraw = withdrawDao.transfer(customerAadharNo, customerAccountNo, amount, agentAccountNo);
              
//              } else{
//                  withdraw = new Transaction();
//              }
        } catch (Exception e) {
            withdraw = new Transaction();
        }
           
        return withdraw;
}  
    
    
    // card 
     @RequestMapping(value="/rupaywithdraw/{agentAccount}/{cardNumber}/{cardHolderName}/{cvv}/{expireDate}/{pin}/{amount}",method=RequestMethod.GET)
 public Transaction rupaywithdraw(@PathVariable String agentAccount, @PathVariable String cardNumber,@PathVariable String cardHolderName,@PathVariable String cvv, @PathVariable String expireDate, @PathVariable String pin,  @PathVariable double amount){
     Transaction transaction = null;
    try{
    transaction = withdrawDao.rwithdraw(agentAccount,cardNumber,cardHolderName,cvv,expireDate,pin,amount);
    }catch(Exception e){
    transaction = new Transaction();
    }
    return transaction;
}

    public void setWithdrawDao(WithdrawDao withdrawDao) {
        this.withdrawDao = withdrawDao;
    }
}
