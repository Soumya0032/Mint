/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ApyDao;
import dao.PmjjbyDao;
import dao.PmsbyDao;
import dao.RdDao;


import model.Apy;
import model.Pmjjby;
import model.Pmsby;
import model.Rd;

/**
 *
 * @author Mk's PC
 */
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class SchemeController {
    private ApyDao apydao;
    private PmjjbyDao pmjjbydao;
    private PmsbyDao pmsbydao;
    private RdDao rddao;
    

    public void setApyDao(ApyDao apydao) {
        this.apydao = apydao;
    }
        @RequestMapping(value="/apyInsert", method=RequestMethod.POST)
    public Apy createApy(@RequestBody Apy apy){
//        Apy apy = new Apy(aadharNumber,accountNumber,nomineeAadhar,nomineeName,nomineeRelation,Amount,pensionAmount,dateofbirth);
        apydao.create(apy);
    return apy;
    }
                @RequestMapping(value = "/getApy/{aadhar_number}", method = RequestMethod.GET)
    public Apy getAadhar_number(@PathVariable("aadhar_number") String aadhar_number) {
        Apy apy = apydao.readByAadhar_number(aadhar_number);
        return apy;
    }
    
    public void setPmjjbyDao(PmjjbyDao pmjjbydao){
    this.pmjjbydao=pmjjbydao;}
    
    @RequestMapping(value="/pmjjbyInsert", method=RequestMethod.POST)
    public Pmjjby createPmjjby(@RequestBody Pmjjby pmjjby){
//        Apy apy = new Apy(aadharNumber,accountNumber,nomineeAadhar,nomineeName,nomineeRelation,Amount,pensionAmount,dateofbirth);
        pmjjbydao.create(pmjjby);
    return pmjjby;
    }
            @RequestMapping(value = "/getPmjjby/{aadhar_number}", method = RequestMethod.GET)
    public Pmjjby getAadharnumber(@PathVariable("aadhar_number") String aadhar_number) {
        Pmjjby pmjjby = pmjjbydao.readByAadharnumber(aadhar_number);
        return pmjjby;
    }
    
    
    
    public void setPmsbyDao(PmsbyDao pmsbydao){
    this.pmsbydao=pmsbydao;}
    
            @RequestMapping(value="/pmsbyInsert", method=RequestMethod.POST)
    public Pmsby createPmsby(@RequestBody Pmsby pmsby){
//        Apy apy = new Apy(aadharNumber,accountNumber,nomineeAadhar,nomineeName,nomineeRelation,Amount,pensionAmount,dateofbirth);
        pmsbydao.create(pmsby);
    return pmsby;
    }
            @RequestMapping(value = "/getPmsby/{aadhar_number}", method = RequestMethod.GET)
    public Pmsby getAadharnbr(@PathVariable("aadhar_number") String aadhar_number) {
        Pmsby pmsby = pmsbydao.readByAadharnbr(aadhar_number);
        return pmsby;
    }
    
    
    
    public void setRdDao(RdDao rddao){
    this.rddao=rddao;}
    
            @RequestMapping(value="/rdInsert", method=RequestMethod.POST)
    public Rd createRd(@RequestBody Rd rd){
//        Apy apy = new Apy(aadharNumber,accountNumber,nomineeAadhar,nomineeName,nomineeRelation,Amount,pensionAmount,dateofbirth);
        rddao.create(rd);
    return rd;
    }
            @RequestMapping(value = "/getRd/{aadhar_number}", method = RequestMethod.GET)
    public Rd getAadhar_nbr(@PathVariable("aadhar_number") String aadhar_number) {
        Rd rd = rddao.readByAadhar_nbr(aadhar_number);
        return rd;
    }
}
