package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserDto {

    private Integer id;

    private String username;

    private String email;

    private String password;

    private List<Integer> devicesId;


}
