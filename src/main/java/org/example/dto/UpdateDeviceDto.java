package org.example.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDeviceDto {

    private Integer id;

    private String name;

    @Nullable
    private List<Integer> sensorsId;
}
