package org.example.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDto {

    private String newPassword;

    private String resetCode;

}
