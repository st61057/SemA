package org.example.controller;

import org.example.dto.SensorDto;
import org.example.dto.UpdateSensorDto;
import org.example.entity.Device;
import org.example.entity.Sensor;
import org.example.service.SensorService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v3")
public class SensorController {

    private final SensorService sensorService;

    private final ModelMapper modelMapper;

    public SensorController(SensorService sensorService, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/api/all-sensors")
    public ResponseEntity<?> getAllSensors() {
        List<Sensor> sensors = sensorService.findAllSensors();
        return ResponseEntity.ok(sensors.stream().map(this::convertSensorToDto).collect(Collectors.toList()));
    }

    @PostMapping(value = "/api/sensor-add")
    public ResponseEntity<?> addSensor(@RequestBody SensorDto sensorDto) {
        Pair<Optional<Sensor>, String> creation = sensorService.createSensor(sensorDto);
        Optional<Sensor> sensor = creation.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(convertSensorToDto(sensor.get()));
        }
        return ResponseEntity.badRequest().body(creation.getSecond());
    }

    @PutMapping(value = "/api/sensor-update")
    public ResponseEntity<?> updateSensor(@RequestBody UpdateSensorDto sensorDto) {
        Pair<Optional<Sensor>, String> creation = sensorService.updateSensor(sensorDto);
        Optional<Sensor> sensor = creation.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(convertSensorToDto(sensor.get()));
        }
        return ResponseEntity.badRequest().body(creation.getSecond());
    }

    @DeleteMapping(value = "/api/sensor-update/{id}")
    public ResponseEntity<?> deleteSensor(@PathVariable Integer id) {
        Pair<Optional<Sensor>, String> creation = sensorService.deleteSensor(id);
        Optional<Sensor> sensor = creation.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(convertSensorToDto(sensor.get()));
        }
        return ResponseEntity.badRequest().body(creation.getSecond());
    }

    private SensorDto convertSensorToDto(Sensor sensor) {
        SensorDto sensorDto = modelMapper.map(sensor, SensorDto.class);
        sensorDto.setName(sensorDto.getName());
        Optional<Device> device = Optional.of(sensor.getDevice());
        sensorDto.setDeviceId(device.map(Device::getId).orElse(null));
        return sensorDto;
    }

}
