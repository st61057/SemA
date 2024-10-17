package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.dto.basic.DeviceDto;
import org.example.dto.basic.DeviceSensorDto;
import org.example.dto.updates.UpdateDeviceDto;
import org.example.entity.Device;
import org.example.entity.Sensor;
import org.example.service.DeviceService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Device", description = "Devices for users")
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class DeviceController {

    private final DeviceService deviceService;

    private final ModelMapper modelMapper;

    @GetMapping("/all-devices")
    @Operation(
            summary = "Retrieves all available devices",
            description = "Retrieves all devices available in database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Devices retrieved successfully", content = @Content(array = @ArraySchema())),
            }
    )
    public ResponseEntity<?> getAllDevices() {
        List<Device> sensors = deviceService.findAllDevices();
        return ResponseEntity.ok(sensors.stream().map(this::convertDeviceToDto).collect(Collectors.toList()));
    }

    @PostMapping(value = "/device-add")
    @Operation(
            summary = "Creating new device",
            description = "Creating new device which can be assigned to existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device created successfully", content = @Content(schema = @Schema(implementation = Device.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input")
            }
    )
    public ResponseEntity<?> addDevice(@Valid @RequestBody DeviceDto deviceDto) {
        Pair<Optional<Device>, String> creation = deviceService.createDevice(deviceDto);
        Optional<Device> sensor = creation.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(convertDeviceToDto(sensor.get()));
        }
        return ResponseEntity.badRequest().body(creation.getSecond());
    }

    @PostMapping(value = "/device-add-sensor")
    @Operation(
            summary = "Adding sensor to device",
            description = "Adding sensor to an existing device",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensor added to device successfully", content = @Content(schema = @Schema(implementation = DeviceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input")
            }
    )
    public ResponseEntity<?> addSensorToDevice(@Valid @RequestBody DeviceSensorDto deviceSensorDto) {
        Pair<Optional<Device>, String> add = deviceService.addSensorToDevice(deviceSensorDto);
        Optional<Device> device = add.getFirst();
        if (device.isPresent()) {
            return ResponseEntity.ok(convertDeviceToDto(device.get()));
        }
        return ResponseEntity.badRequest().body(add.getSecond());
    }


    @PostMapping(value = "/device-remove-sensor")
    @Operation(
            summary = "Removing sensor from device",
            description = "Removing sensor from existing device",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensor removed from device successfully", content = @Content(schema = @Schema(implementation = DeviceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input")
            }
    )
    public ResponseEntity<?> removeSensorFromDevice(@Valid @RequestBody DeviceSensorDto deviceSensorDto) {
        Pair<Optional<Device>, String> remove = deviceService.removeSensorFromDevice(deviceSensorDto);
        Optional<Device> device = remove.getFirst();
        if (device.isPresent()) {
            return ResponseEntity.ok(convertDeviceToDto(device.get()));
        }
        return ResponseEntity.badRequest().body(remove.getSecond());
    }

    @PutMapping(value = "/device-update")
    @Operation(
            summary = "Update device via name",
            description = "Updates device name or sensors",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Device or sensor not found")
            }
    )
    public ResponseEntity<?> updateDevice(@RequestBody UpdateDeviceDto deviceDto) {
        Pair<Optional<Device>, String> update = deviceService.updateDevice(deviceDto);
        Optional<Device> device = update.getFirst();
        if (device.isPresent()) {
            return ResponseEntity.ok(convertDeviceToDto(device.get()));
        }
        return ResponseEntity.badRequest().body(update.getSecond());
    }

    @DeleteMapping(value = "/device-delete/{name}")
    @Operation(
            summary = "Delete device via name",
            description = "Delete specific device by name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Device not found")
            }
    )
    public ResponseEntity<?> deleteDevice(@PathVariable String name) {
        Pair<Optional<Device>, String> delete = deviceService.deleteDevice(name);
        Optional<Device> sensor = delete.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(convertDeviceToDto(sensor.get()));
        }
        return ResponseEntity.badRequest().body(delete.getSecond());
    }

    private DeviceDto convertDeviceToDto(Device device) {
        DeviceDto deviceDto = modelMapper.map(device, DeviceDto.class);
        deviceDto.setName(device.getName());

        List<Sensor> sensors = device.getSensorList();
        if (sensors != null) {
            sensors.forEach(sensor -> deviceDto.getSensorsName().add(sensor.getName()));
        }
        return deviceDto;
    }
}
