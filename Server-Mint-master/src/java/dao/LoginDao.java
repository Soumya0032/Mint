
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.sql.DataSource;
import model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ROHAN
 */
public class LoginDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //login with fingerprint string
    public User validateUser(String agentId, String password, String decryptedFingerprint) {
       //System.out.print("DAO : " + fingerprint);
      jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = "SELECT * FROM USERLOGIN WHERE AGENT_ID=? AND PASSWORD=? AND FINGERPRINT = ?";
        
       User message = jdbcTemplate.queryForObject(sql, new Object[]{agentId, password, decryptedFingerprint}, BeanPropertyRowMapper.newInstance(User.class));  
        
       System.out.print(message);
        return message;
        
    }
    
    //login without fingerprint string
    public User validateUserCredential(String agentId, String password) {
       //System.out.print("DAO : " + fingerprint);
      jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = "SELECT * FROM USERLOGIN WHERE AGENT_ID=? AND PASSWORD=?";
        
       User message = jdbcTemplate.queryForObject(sql, new Object[]{agentId, password}, BeanPropertyRowMapper.newInstance(User.class));  
        
       System.out.print(message);
        return message;
        
    }
    
    
    
   public User getUserDetails(String imei){
       jdbcTemplate = new JdbcTemplate(dataSource);

        String sql = "SELECT * FROM USERLOGIN WHERE AGENT_ID=?";
        
       User message = jdbcTemplate.queryForObject(sql, new Object[]{imei}, BeanPropertyRowMapper.newInstance(User.class));  
        
       return message;
        
    }
    
}
