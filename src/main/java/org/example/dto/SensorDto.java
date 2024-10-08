package org.example.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class SensorDto {

    @Nullable
    private Integer deviceId;

    private String name;

}
