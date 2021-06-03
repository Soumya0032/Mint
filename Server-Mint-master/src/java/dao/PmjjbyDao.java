/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;



import java.util.List;
import javax.sql.DataSource;
import model.Pmjjby;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 *
 * @author Mk's PC
 */
public class PmjjbyDao {
    
       private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
      public void create(Pmjjby pmjjby) {
 String sql = "INSERT INTO PMJJBY_SCHEME(AADHAR_NUMBER,ACCOUNT_NUMBER,NOMINEE_AADHAR,"
         + "NOMINEE_NAME,NOMINEE_RELATION,DATEOFBIRTH ) VALUES(?,?,?,?,?,?)";
 jdbcTemplate = new JdbcTemplate(dataSource);
 jdbcTemplate.update(sql,new Object[]{pmjjby.getAadhar_number(), pmjjby.getAccount_number(),pmjjby.getNominee_aadhar(),
            pmjjby.getNominee_name(),
            pmjjby.getNominee_relation(),pmjjby.getDateofbirth()
         }); 
}
        public Pmjjby readByAadharnumber(String aadhar_number) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT account_number,nominee_aadhar,nominee_name,scheme_id FROM pmjjby_scheme WHERE aadhar_number = ?";
        Pmjjby pmjjby = (Pmjjby) jdbcTemplate.queryForObject(sql, new Object[]{aadhar_number}, BeanPropertyRowMapper.newInstance(Pmjjby.class));
        return pmjjby;
    }
}