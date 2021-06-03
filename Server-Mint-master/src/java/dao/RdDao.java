/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Rd;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 *
 * @author Mk's PC
 */
public class RdDao {
       private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
      public void create(Rd rd) {
 String sql = "INSERT INTO RD_SCHEME(AADHAR_NUMBER,ACCOUNT_NUMBER,DATEOFBIRTH,NOMINEE_AADHAR,"
         + "NOMINEE_NAME,NOMINEE_RELATION,AMOUNT,INSTALLMENT_AMOUNT)VALUES(?,?,?,?,?,?,?,?)";
 jdbcTemplate = new JdbcTemplate(dataSource);
 jdbcTemplate.update(sql,new Object[]{rd.getAadhar_number(), rd.getAccount_number(),rd.getDateofbirth(),rd.getNominee_aadhar(),rd.getNominee_name(),
         rd.getNominee_relation(),rd.getAmount(),rd.getInstallment_amount()
         }); 
      }
     public Rd readByAadhar_nbr(String aadhar_number) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT account_number,nominee_aadhar,nominee_name,scheme_id FROM rd_scheme WHERE aadhar_number = ?";
        Rd rd = (Rd) jdbcTemplate.queryForObject(sql, new Object[]{aadhar_number}, BeanPropertyRowMapper.newInstance(Rd.class));
        return rd;
    }
}
