package org.example.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {

        jdbcTemplate.update("INSERT INTO APP_USER (USERNAME, EMAIL, PASSWORD, RESET_CODE) VALUES ('testuser1','nnpdasem@gmail.com','{bcrypt}$2a$10$sCGmi2tZ8K8wxK8z/e4FAOOYRBtzwxtni8Qnt7rg4mBCDKWAlOhHu','code1')");
        jdbcTemplate.update("INSERT INTO APP_USER (USERNAME, EMAIL, PASSWORD, RESET_CODE) VALUES ('testuser2','testovaci_email@email.cz','{bcrypt}$2a$10$sCGmi2tZ8K8wxK8z/e4FAOOYRBtzwxtni8Qnt7rg4mBCDKWAlOhHu','code1')");
        jdbcTemplate.update("INSERT INTO APP_USER (USERNAME, EMAIL, PASSWORD, RESET_CODE) VALUES ('testuser3','testovaci_email@email.cz','{bcrypt}$2a$10$sCGmi2tZ8K8wxK8z/e4FAOOYRBtzwxtni8Qnt7rg4mBCDKWAlOhHu','code1')");

        jdbcTemplate.update("INSERT INTO SENSOR (NAME) VALUES ('testsenzor_1')");
        jdbcTemplate.update("INSERT INTO SENSOR (NAME) VALUES ('testsenzor_2')");
        jdbcTemplate.update("INSERT INTO SENSOR (NAME) VALUES ('testsenzor_3')");
        jdbcTemplate.update("INSERT INTO SENSOR (NAME) VALUES ('testsenzor_4')");

        jdbcTemplate.update("INSERT INTO DEVICE (NAME) VALUES ('testzarizeni_1')");
        jdbcTemplate.update("INSERT INTO DEVICE (NAME) VALUES ('testzarizeni_2')");
        jdbcTemplate.update("INSERT INTO DEVICE (NAME) VALUES ('testzarizeni_3')");
        jdbcTemplate.update("INSERT INTO DEVICE (NAME) VALUES ('testzarizeni_4')");
        jdbcTemplate.update("INSERT INTO DEVICE (NAME) VALUES ('testzarizeni_5')");
        jdbcTemplate.update("INSERT INTO DEVICE (NAME) VALUES ('testzarizeni_6')");


    }
}
