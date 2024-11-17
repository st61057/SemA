package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dao.SensorDataRepository;
import org.example.dao.SensorRepository;
import org.example.dto.basic.SensorDataDto;
import org.example.dto.basic.SensorDto;
import org.example.entity.Sensor;
import org.example.entity.SensorData;
import org.example.service.SensorDataService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class SensorDataController {

    private final SensorDataRepository sensorDataRepository;
    private final SensorRepository sensorRepository;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<SensorData>> getAllSensorData() {
        List<SensorData> data = sensorDataRepository.findAll();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSensorDataBySensorName(@PathVariable String name) {
        List<SensorData> sensors = sensorDataRepository.findBySensorName(name);
        return ResponseEntity.ok(sensors.stream().map(this::convertSensorDataToDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> addSensorData(@RequestBody SensorData sensorData) {
        try {
            if (sensorData.getSensor() != null) {
                var sensor = sensorRepository.findById(sensorData.getSensor().getId());
                if (sensor.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sensor with ID " + sensorData.getSensor().getId() + " not found.");
                }
                sensorData.setSensor(sensor.get());
            }

            SensorData savedData = sensorDataRepository.save(sensorData);
            return ResponseEntity.ok(savedData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding sensor data: " + e.getMessage());
        }
    }

    private SensorDataDto convertSensorDataToDto(SensorData sensorData) {
        SensorDataDto sensorDataDto = modelMapper.map(sensorData, SensorDataDto.class);
        sensorDataDto.setSensor(convertSensorToDto(sensorData.getSensor()));
        sensorDataDto.setTemperature(sensorData.getTemperature());
        sensorDataDto.setUsageEnergy(sensorData.getUsageEnergy());
        return sensorDataDto;
    }

    public SensorDto convertSensorToDto(Sensor sensor) {
        SensorDto sensorDto = modelMapper.map(sensor, SensorDto.class);
        sensorDto.setName(sensorDto.getName());
        return sensorDto;
    }
}

