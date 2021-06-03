/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.sql.DataSource;
import model.AccountDetails;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ROHAN
 */
public class AccountDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
        public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
        
    
    public void create(AccountDetails account) {
        String sql = "INSERT INTO NEW_ACCOUNT (NAME,AGE,DOB,IFSC,AADHAR,"
                + "EMAIL,MOBILENO,FATHERNAME,ANNUALINCOME,ADDRESS,NOMINEENAME,RELATIONSHIP,NOMINEEADDRESS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate = new JdbcTemplate(dataSource);
        int i=jdbcTemplate.update(sql, new Object[]{account.getName(), account.getAge(),account.getDob()
                ,account.getIfsc(),account.getAadhar(),account.getEmail(),account.getMobileNo(),account.getFatherName(),
                account.getAnnualIncome(),account.getAddress(),account.getNomineeName(),account.getRelationship(),account.getNomineeAddress()});
    }
            
     
    public List<AccountDetails> readAll() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT * FROM NEW_ACCOUNT";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AccountDetails.class));
    }
    
     public int checkAadhar(String aadharNumber) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT count(*) FROM NEW_ACCOUNT WHERE AADHAR=?";
//        Account ac= jdbcTemplate.queryForObject(sql,new Object[]{aadharNumber},BeanPropertyRowMapper.newInstance(Account.class));
            int count = jdbcTemplate.queryForObject(
                        sql, new Object[] { aadharNumber }, Integer.class);
        return count;
        
    }
    
}
