package org.example.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewPasswordDto {

    private String username;

    private String password;

    private String code;
}
