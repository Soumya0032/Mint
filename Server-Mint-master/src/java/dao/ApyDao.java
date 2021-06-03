/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import model.Apy;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 *
 * @author Mk's PC
 */
public class ApyDao {
        
   private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
      public void create(Apy apy) {
 String sql = "INSERT INTO APY_SCHEME(AADHAR_NUMBER,ACCOUNT_NUMBER,NOMINEE_AADHAR,"
         + "NOMINEE_NAME,NOMINEE_RELATION,AMOUNT,PENSION_AMOUNT,DATEOFBIRTH)VALUES(?,?,?,?,?,?,?,?)";
 jdbcTemplate = new JdbcTemplate(dataSource);
 jdbcTemplate.update(sql,new Object[]{apy.getAadhar_number(), apy.getAccount_number(),apy.getNominee_aadhar(),apy.getNominee_name(),
         apy.getNominee_relation(),apy.getAmount(),apy.getPension_amount(),apy.getDateofbirth()
         }); 
 
      }
    public Apy readByAadhar_number(String aadhar_number) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT account_number,nominee_aadhar,nominee_name,scheme_id FROM apy_scheme WHERE aadhar_number = ?";
        Apy apy = (Apy) jdbcTemplate.queryForObject(sql, new Object[]{aadhar_number}, BeanPropertyRowMapper.newInstance(Apy.class));
        return apy;
    }
    
}
