package org.example.service;

import org.example.dto.basic.*;
import org.example.entity.Device;
import org.example.entity.Sensor;
import org.example.entity.SensorData;
import org.example.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConverterService {

    @Autowired
    private ModelMapper modelMapper;

    public UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setDevices(user.getDevices());
        return userDto;
    }

    public DeviceDto convertDeviceToDto(Device device) {
        DeviceDto deviceDto = modelMapper.map(device, DeviceDto.class);
        deviceDto.setName(device.getName());
        deviceDto.setLocation(device.getLocation());

        List<Sensor> sensors = device.getSensorList();
        if (sensors != null) {
            sensors.forEach(sensor -> deviceDto.getSensorsName().add(sensor.getName()));
        }
        return deviceDto;
    }


    public SensorDto convertSensorToDto(Sensor sensor) {
        SensorDto sensorDto = modelMapper.map(sensor, SensorDto.class);
        sensorDto.setName(sensorDto.getName());
        sensorDto.setSensorDataDto(sensor.getMeasuredValues().stream().map(this::convertSensorDataToDto).collect(Collectors.toList()));
        return sensorDto;
    }

    public SensorDataReadDto convertSensorDataToDto(SensorData sensorData) {
        SensorDataReadDto sensorDataDto = modelMapper.map(sensorData, SensorDataReadDto.class);
        sensorDataDto.setTemperature(sensorData.getTemperature());
        sensorDataDto.setUsageEnergy(sensorData.getUsageEnergy());
        sensorDataDto.setDataMeasuredTime(sensorData.getDataMeasuredTime());
        return sensorDataDto;
    }
}
