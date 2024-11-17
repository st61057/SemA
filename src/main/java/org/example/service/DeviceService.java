package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.example.dao.DeviceRepository;
import org.example.dao.UserRepository;
import org.example.dto.basic.DeviceDto;
import org.example.dto.basic.DeviceSensorDto;
import org.example.dto.updates.UpdateDeviceDto;
import org.example.entity.Device;
import org.example.entity.Sensor;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private UserRepository userRepository;

    public Pair<Optional<Device>, String> createDevice(DeviceDto deviceDto) {
        if (!findDeviceByName(deviceDto.getName()).isPresent()) {

            List<Sensor> sensors = new ArrayList<>();

            if (deviceDto.getSensorsName() != null) {
                for (String sensorName : deviceDto.getSensorsName()) {
                    Optional<Sensor> sensor = sensorService.findSensorByName(sensorName);
                    if (sensor.isEmpty()) {
                        return Pair.of(Optional.empty(), "Sensor with this name " + sensorName + " doesn't exists!");
                    }
                    sensors.add(sensor.get());
                }
            }
            Device device = new Device(deviceDto.getName(), deviceDto.getLocation(), sensors);
            sensors.forEach(sensor -> sensor.setDevice(device));
            return Pair.of(Optional.of(deviceRepository.save(device)), StringUtils.EMPTY);
        }
        return Pair.of(Optional.empty(), "Device with this name already exists!");
    }

    public Pair<Optional<Device>, String> addSensorToDevice(DeviceSensorDto deviceSensorDto) {

        Optional<Device> existingDevice = findDeviceByName(deviceSensorDto.getName());
        if (existingDevice.isEmpty()) {
            return Pair.of(Optional.empty(), "Device with this name doesn't exists");
        }

        Optional<Sensor> existingSensor = sensorService.findSensorByName(deviceSensorDto.getSensorName());
        if (existingSensor.isEmpty()) {
            return Pair.of(Optional.empty(), "Sensor with this name doesn't exists");
        }

        Device device = existingDevice.get();
        Sensor sensor = existingSensor.get();
        if (device.getSensorList().contains(sensor)) {
            return Pair.of(Optional.empty(), "Sensor is already assigned to this device");
        }

        device.getSensorList().add(sensor);
        sensor.setDevice(device);

        return Pair.of(Optional.of(deviceRepository.save(device)), StringUtils.EMPTY);
    }

    public Pair<Optional<Device>, String> removeSensorFromDevice(DeviceSensorDto deviceSensorDto) {

        Optional<Device> existingDevice = findDeviceByName(deviceSensorDto.getName());
        if (existingDevice.isEmpty()) {
            return Pair.of(Optional.empty(), "Device with this name doesn't exists");
        }

        Optional<Sensor> existingSensor = sensorService.findSensorByName(deviceSensorDto.getSensorName());
        if (existingSensor.isEmpty()) {
            return Pair.of(Optional.empty(), "Sensor with this name doesn't exists");
        }

        Device device = existingDevice.get();
        Sensor sensor = existingSensor.get();

        if (device.getSensorList().isEmpty() || !device.getSensorList().contains(sensor)) {
            return Pair.of(Optional.empty(), "Sensor isn't connected to this device");
        } else {
            device.getSensorList().remove(sensor);
        }
        sensor.setDevice(null);

        return Pair.of(Optional.of(deviceRepository.save(device)), StringUtils.EMPTY);
    }

    public Pair<Optional<Device>, String> updateDevice(UpdateDeviceDto updateDeviceDto) {
        Optional<Device> existingDevice = findDeviceByName(updateDeviceDto.getName());
        if (existingDevice.isEmpty()) {
            return Pair.of(Optional.empty(), "Sensor with this name doesn't exists");
        }

        List<Sensor> sensors = new ArrayList<>();
        if (updateDeviceDto.getSensorsNames() != null) {
            for (String sensorName : updateDeviceDto.getSensorsNames()) {
                Optional<Sensor> existingSensor = sensorService.findSensorByName(sensorName);
                if (existingSensor.isEmpty()) {
                    return Pair.of(Optional.empty(), "Sensor with name " + sensorName + " doesn't exists");
                }
                Sensor sensor = existingSensor.get();
                sensor.setDevice(existingDevice.get());
                sensors.add(existingSensor.get());
            }
        }

        Device updatedDevice = existingDevice.get();
        updatedDevice.setName(updateDeviceDto.getName());
        updatedDevice.setLocation(updateDeviceDto.getLocation());
        updatedDevice.setSensorList(sensors);
        return Pair.of(Optional.of(deviceRepository.save(updatedDevice)), StringUtils.EMPTY);

    }

    public Pair<Optional<Device>, String> deleteDevice(String name) {
        Optional<Device> existingDevice = findDeviceByName(name);
        if (existingDevice.isEmpty()) {
            return Pair.of(Optional.empty(), "Device with this name doesn't exists");
        }

        Set<User> users = userRepository.findUsersByDevices(existingDevice.get());
        users.forEach(user -> user.setDevices(
                user.getDevices().stream()
                        .filter(device -> !device.getId().equals(existingDevice.get().getId()))
                        .collect(Collectors.toSet())));

        Device device = existingDevice.get();
        List<Sensor> sensors = new ArrayList<>();
        for (Sensor sensor : existingDevice.get().getSensorList()) {
            sensor.setDevice(null);
            sensors.add(sensor);
        }
        device.setSensorList(sensors);

        deviceRepository.delete(device);
        return Pair.of(Optional.of(device), StringUtils.EMPTY);
    }

    public List<Device> findDevicesBySensorListContains(Sensor sensor) {
        return deviceRepository.findDevicesBySensorListContains(sensor);
    }

    public Optional<Device> findDeviceByName(String name) {
        return deviceRepository.findByName(name);
    }

    public List<Device> findAllDevices() {
        return (List<Device>) deviceRepository.findAll();
    }
}
