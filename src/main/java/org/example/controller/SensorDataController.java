package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dao.SensorDataRepository;
import org.example.dao.SensorRepository;
import org.example.dto.basic.SensorDataDto;
import org.example.dto.basic.SensorDto;
import org.example.entity.Sensor;
import org.example.entity.SensorData;
import org.example.service.ConverterService;
import org.example.service.SensorDataService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    private final ConverterService converterService;

    @GetMapping
    public ResponseEntity<List<SensorDataDto>> getAllSensorData() {
        List<SensorData> sensorsData = sensorDataService.findAllSensorsData();
        return ResponseEntity.ok(sensorsData.stream().map(converterService::convertSensorDataToDto).collect(Collectors.toList()));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getSensorDataBySensorName(@PathVariable String name) {
        List<SensorData> sensors = sensorDataService.findSensorDataByName(name);
        return ResponseEntity.ok(sensors.stream().map(converterService::convertSensorDataToDto).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> addSensorData(@RequestBody SensorDataDto sensorDataDto) {
        Pair<Optional<SensorData>, String> add = sensorDataService.addSensorDataToSensor(sensorDataDto);
        Optional<SensorData> sensorData = add.getFirst();
        if (sensorData.isPresent()) {
            return ResponseEntity.ok(converterService.convertSensorDataToDto(sensorData.get()));
        }
        return ResponseEntity.badRequest().body(add.getSecond());
    }
}

