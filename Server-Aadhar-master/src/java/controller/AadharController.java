package controller;


import dao.AadharDao;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ROHAN
 */
@RestController
@RequestMapping("/")
public class AadharController {
    private AadharDao aadharDao;
  
    @RequestMapping(value = "/aadhar/{number}", method = RequestMethod.GET)
        public Aadhar getAadharDetails(@PathVariable String number){
          
            Aadhar aAadhar = null;
        try {
            aAadhar = aadharDao.getDetails(number);
        } catch (Exception e) {
            new Aadhar();
        }
          return aAadhar;
        }
    
    public void setAadharDao(AadharDao aadharDao) {
        this.aadharDao = aadharDao;
    }
    
    
    // Test GIT Message
}
