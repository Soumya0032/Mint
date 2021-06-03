/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DepositDao;
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
public class DepositController {
    DepositDao depositDao;
    
    @RequestMapping(value="/depositMoney/{customerAadharNo}/{customerAccountNo}/{amount}/{agentAccountNo}",method=RequestMethod.GET)
     public Transaction depositMoney(@PathVariable String customerAadharNo, @PathVariable String customerAccountNo, @PathVariable Double amount, @PathVariable String agentAccountNo){
 
         
        Transaction customerTransaction = null;
        try {
            customerTransaction = depositDao.depositMoney(customerAadharNo, customerAccountNo, amount, agentAccountNo);
        } catch (Exception e) {
            customerTransaction = new Transaction();
        }
      return customerTransaction;
}
     
     
//     @RequestMapping(value="/check/{aadharNo}/{acctNo}", method = RequestMethod.GET)
//     public Account check(@PathVariable String aadharNo, @PathVariable String acctNo){
//         
//         Account act = depositDao.checkAccount(aadharNo, acctNo);
//         return act;
//     }
     
    public void setDepositDao(DepositDao depositDao) {
        this.depositDao = depositDao;
    }
    
}
