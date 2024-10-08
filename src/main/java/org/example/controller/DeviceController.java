package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.dto.DeviceDto;
import org.example.dto.UpdateDeviceDto;
import org.example.entity.Device;
import org.example.entity.Sensor;
import org.example.entity.User;
import org.example.service.DeviceService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v3")
public class DeviceController {

    private final DeviceService deviceService;

    private final ModelMapper modelMapper;

    public DeviceController(DeviceService deviceService, ModelMapper modelMapper) {
        this.deviceService = deviceService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/api/all-devices")
    @Operation(
            summary = "All devices",
            description = "Getting all devices")
    public ResponseEntity<?> getAllDevices() {
        List<Device> sensors = deviceService.findAllDevices();
        return ResponseEntity.ok(sensors.stream().map(this::convertDeviceToDto).collect(Collectors.toList()));
    }

    @GetMapping("/api/test/{id}")
    public String testPost(@PathVariable @NotNull Integer id) {
        return "Hello World!" + id;
    }

    @PostMapping(value = "/api/device-add")
    @Operation(
            summary = "Add device",
            description = "Adding device")
    public ResponseEntity<?> addDevice(@RequestBody DeviceDto deviceDto) {
        Pair<Optional<Device>, String> creation = deviceService.createDevice(deviceDto);
        Optional<Device> sensor = creation.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(convertDeviceToDto(sensor.get()));
        }
        return ResponseEntity.badRequest().body(creation.getSecond());
    }

    @PutMapping(value = "/api/device-update")
    public ResponseEntity<?> updateDevice(@RequestBody UpdateDeviceDto deviceDto) {
        Pair<Optional<Device>, String> update = deviceService.updateDevice(deviceDto);
        Optional<Device> device = update.getFirst();
        if (device.isPresent()) {
            return ResponseEntity.ok(convertDeviceToDto(device.get()));
        }
        return ResponseEntity.badRequest().body(update.getSecond());
    }

    @DeleteMapping(value = "/api/device-update/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable Integer id) {
        Pair<Optional<Device>, String> delete = deviceService.deleteDevice(id);
        Optional<Device> sensor = delete.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(convertDeviceToDto(sensor.get()));
        }
        return ResponseEntity.badRequest().body(delete.getSecond());
    }

    private DeviceDto convertDeviceToDto(Device device) {
        DeviceDto deviceDto = modelMapper.map(device, DeviceDto.class);
        deviceDto.setName(device.getName());
        List<User> users = device.getUsers();
//        Optional<Sensor> sensor = Optional.of(device.getSensor());
        List<Sensor> sensors = device.getSensorList();
        deviceDto.setUsers(users);
        if (sensors != null) {
            sensors.forEach(sensor-> deviceDto.getSensorId().add(sensor.getId()));
        }
//        deviceDto.setSensorId(sensor.map(Sensor::getId).orElse(null));
        return deviceDto;
    }
}
