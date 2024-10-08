package org.example.dto;

//import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSensorDto {

    private Integer id;

    private String name;

}
