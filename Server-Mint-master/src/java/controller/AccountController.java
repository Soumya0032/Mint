/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.AccountDao;
import java.util.List;
import model.AccountDetails;
import model.BankResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ROHAN
 */
@RestController
@RequestMapping("/")
public class AccountController {
    private AccountDao accountDao;
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BankResponse createAccount(@RequestBody AccountDetails newAccount) {
        
        BankResponse response=new BankResponse();
        
        if(accountDao.checkAadhar(newAccount.getAadhar())==0){
                accountDao.create(newAccount);
                response.setAccountDetails(newAccount);
                response.setStatus(200);
            }
         else
               response.setStatus(400);

        
        return response;
    }
        
        
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<AccountDetails> getAllCustomer() {
        List<AccountDetails> list = accountDao.readAll();
        return list;
    }  
    
    @RequestMapping(value = "/checkAadhar", method = RequestMethod.GET)
    public int getCheckAadhar() {
        int ac = accountDao.checkAadhar("254125635876");
        return ac;
    }
/*     @RequestMapping(value = "/create", method = RequestMethod.POST)
//    public BankResponse createAccount(@RequestBody Account newAccount) {
////*        Student s = new Student(name,age,branch,regno);
            if(accountDao.checkPan(pan)==false){
                accountDao.create(newAccount);
            }
//        throw new BankExcception()
//        return newAccount;
//    }*/
}
