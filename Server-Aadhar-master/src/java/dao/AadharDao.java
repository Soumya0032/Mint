/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controller.Aadhar;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ROHAN
 */
public class AadharDao {
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public Aadhar getDetails(String number){
        String sql = "SELECT * FROM Aadhar WHERE AADHAR_NUMBER=?";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Aadhar value = (Aadhar)jdbcTemplate.queryForObject(sql, new Object[]{number}, BeanPropertyRowMapper.newInstance(Aadhar.class));
     return value;   
    }
}
