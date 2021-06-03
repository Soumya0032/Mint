/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.MiniStatementDao;
import java.util.List;
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
public class MiniStatementController {
    List<Transaction> list;
    private MiniStatementDao miniStatementDao;

    @RequestMapping(value = "/miniStatement/{aadharNo}/{accountNo}", method= RequestMethod.GET)
    public List<Transaction> getMiniStatement(@PathVariable String aadharNo, @PathVariable String accountNo){
        List<Transaction> miniStatement = null;
        try {
            miniStatement = miniStatementDao.getMiniStatementByAccount(aadharNo, accountNo);
        } catch (Exception e) {
            miniStatement = (List<Transaction>) new Transaction();
        }
        return miniStatement;
    }
    
    //card
      @RequestMapping(value = "/cardMiniStatement/{cardNumber}/{cardHolderName}/{cvv}/{expireDate}/{pin}", method= RequestMethod.GET)
    public List<Transaction> getMiniStatement(@PathVariable String cardNumber,@PathVariable String cardHolderName,@PathVariable String cvv, @PathVariable String expireDate, @PathVariable String pin){
        List<Transaction> list =  miniStatementDao.getMiniStatementByAccount(cardNumber,cardHolderName,cvv,expireDate, pin);
        return list;
    }
   
    public void setMiniStatementDao(MiniStatementDao miniStatementDao) {
        this.miniStatementDao = miniStatementDao;
    }
}