/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;


import java.util.List;
import javax.sql.DataSource;
import model.Pmsby;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 *
 * @author Mk's PC
 */
public class PmsbyDao {
    
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
        public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
      public void create(Pmsby pmsby) {
 String sql = "INSERT INTO PMSBY_SCHEME(AADHAR_NUMBER,ACCOUNT_NUMBER,NOMINEE_AADHAR,"
         + "NOMINEE_NAME,NOMINEE_RELATION,DATEOFBIRTH)VALUES(?,?,?,?,?,?)";
 jdbcTemplate = new JdbcTemplate(dataSource);
 jdbcTemplate.update(sql,new Object[]{pmsby.getAadhar_number(), pmsby.getAccount_number(),pmsby.getNominee_aadhar(),pmsby.getNominee_name(),
         pmsby.getNominee_relation(),pmsby.getDateofbirth()}); 
      }
      
        public Pmsby readByAadharnbr(String aadhar_number) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT account_number,nominee_aadhar,nominee_name,scheme_id FROM pmsby_scheme WHERE aadhar_number = ?";
        Pmsby pmsby = (Pmsby) jdbcTemplate.queryForObject(sql, new Object[]{aadhar_number}, BeanPropertyRowMapper.newInstance(Pmsby.class));
        return pmsby;
    }
}