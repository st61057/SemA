package org.example.dto;


//import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class UpdateDeviceDto {

//    @NotNull
    private Integer id;

//    @NotNull
    private String name;

    @Nullable
    private Integer userId;

    @Nullable
    private Integer sensorId;
}
