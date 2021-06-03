/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.FundTransferDao;
import model.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ROHAN
 */
@Controller
@RequestMapping("/")
public class FundTransferController {
    FundTransferDao fundTransferDao;

    @ResponseBody
    @RequestMapping(value = "/transfer/{customerAadhar}/{amount}/{customerAccount}/{bAccount}/{bAadhar}", method = RequestMethod.GET)
    public Transaction fundTransfer( @PathVariable String customerAadhar, @PathVariable Double amount, @PathVariable String customerAccount, @PathVariable String bAccount, @PathVariable String bAadhar){
        
////        try {
        Transaction transfer = fundTransferDao.transferMoney(customerAadhar, amount, customerAccount, bAadhar, bAccount);
//            
////        } catch (Exception e) {
////            transfer = new Transaction();
//        }
        return transfer;
    }

    public void setFundTransferDao(FundTransferDao fundTransferDao) {
        this.fundTransferDao = fundTransferDao;
    }
    
    
}