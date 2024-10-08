package org.example.dto;

//import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class UpdateSensorDto {

//    @NotNull
    private Integer id;

//    @NotNull
    private String name;

    @Nullable
    private Integer deviceId;
}
