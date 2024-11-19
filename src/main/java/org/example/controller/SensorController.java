package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.dto.basic.SensorDto;
import org.example.dto.updates.UpdateSensorDto;
import org.example.entity.Sensor;
import org.example.service.ConverterService;
import org.example.service.SensorService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Sensor", description = "Sensors for devices")
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class SensorController {

    private final SensorService sensorService;

    private final ConverterService converterService;

    @GetMapping("/all-sensors")
    @Operation(
            summary = "Retrieves all available sensors",
            description = "Retrieves all sensors available in database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Devices retrieved successfully", content = @Content(array = @ArraySchema())),
            }
    )
    public ResponseEntity<?> getAllSensors() {
        List<Sensor> sensors = sensorService.findAllSensors();
        return ResponseEntity.ok(sensors.stream().map(converterService::convertSensorToDto).collect(Collectors.toList()));
    }

    @PostMapping(value = "/sensor-add")
    @Operation(
            summary = "Sensor creation for device",
            description = "Create new sensor for existing device",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sensor added successfully", content = @Content(schema = @Schema(implementation = Sensor.class))),
                    @ApiResponse(responseCode = "404", description = "User or device not found")
            }
    )
    public ResponseEntity<?> addSensor(@Valid @RequestBody SensorDto sensorDto) {
        Pair<Optional<Sensor>, String> creation = sensorService.createSensor(sensorDto);
        Optional<Sensor> sensor = creation.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(converterService.convertSensorToDto(sensor.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(creation.getSecond());
    }

    @PutMapping(value = "/sensor-update")
    @Operation(
            summary = "Sensor update for device",
            description = "Update name of sensor",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensor updated successfully"),
                    @ApiResponse(responseCode = "404", description = "User, device, or sensor not found")
            }
    )
    public ResponseEntity<?> updateSensor(@Valid @RequestBody UpdateSensorDto sensorDto) {
        Pair<Optional<Sensor>, String> creation = sensorService.updateSensor(sensorDto);
        Optional<Sensor> sensor = creation.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(converterService.convertSensorToDto(sensor.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(creation.getSecond());
    }

    @DeleteMapping(value = "/sensor-delete/{name}")
    @Operation(
            summary = "Delete sensor by sensor name",
            description = "Delete a specific sensor identified name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensor deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "User, device, or sensor not found"),
            }
    )
    public ResponseEntity<?> deleteSensor(@PathVariable String name) {
        Pair<Optional<Sensor>, String> creation = sensorService.deleteSensor(name);
        Optional<Sensor> sensor = creation.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(converterService.convertSensorToDto(sensor.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(creation.getSecond());
    }

}
