package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserDto {

    private Integer id;

    private String username;

    private String email;

    private String password;

    private List<Integer> devicesId;


}
