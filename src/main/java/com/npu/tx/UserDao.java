package com.npu.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insert(){
        String insert="insert into `user` (id,username) values(?,?)";
        jdbcTemplate.update(insert,2,"19");
    }
}
