package org.example.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthToken {

    private String token;
    private String username;
    private Integer id;

    public AuthToken(String token, String username, Integer id) {
        this.token = token;
        this.username = username;
        this.id = id;
    }
}
