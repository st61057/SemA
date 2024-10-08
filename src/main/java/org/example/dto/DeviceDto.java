package org.example.dto;

import lombok.Data;
import org.example.entity.User;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeviceDto {

    private String name;

    @Nullable
    private List<User> users;

    @Nullable
    private List<Integer> sensorId = new ArrayList<>();

}
