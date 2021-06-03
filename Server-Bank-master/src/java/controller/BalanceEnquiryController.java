/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.BalanceEnquiryDao;
import java.util.List;
import model.Account;
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
public class BalanceEnquiryController {
    private BalanceEnquiryDao balanceEnquiryDao;
    
      @RequestMapping(value = "/balanceEnquiry/{aadharNo}/{accountNo}", method= RequestMethod.GET)
        public Account getBalance(@PathVariable String aadharNo, @PathVariable String accountNo){
        
            Account balance = null;
          try {
              balance = balanceEnquiryDao.getBalance(aadharNo, accountNo);
          } catch (Exception e) {
              balance = new Account();
          }
        return balance;
    }
        
    @RequestMapping(value = "/getAgentBalance/{accountNo}", method = RequestMethod.GET)
    public Account getBankAccount(@PathVariable("accountNo") String accountNo) {
        Account account = null;
        try {
            account = balanceEnquiryDao.readByAccountno(accountNo);
        } catch (Exception e) {
            account = new Account();
        }
        return account;
    }
    
    @RequestMapping(value = "/eod", method = RequestMethod.GET)
    public List<Transaction> getDateBanks() {
        List<Transaction> list = balanceEnquiryDao.readAllByDate();
        return list;
    }
    
    
       @RequestMapping(value = "/cardBalanceEnquiry/{cardNumber}/{cardHolderName}/{cvv}/{expireDate}/{pin}", method= RequestMethod.GET)
        public Account getBalance(@PathVariable String cardNumber,@PathVariable String cardHolderName, @PathVariable String cvv, @PathVariable String expireDate, @PathVariable String pin){
        Account balance =  balanceEnquiryDao.getBalance(cardNumber,cardHolderName, cvv, expireDate, pin);
        return balance;
    }

    public void setBalanceEnquiryDao(BalanceEnquiryDao balanceEnquiryDao) {
        this.balanceEnquiryDao = balanceEnquiryDao;
    }
}
