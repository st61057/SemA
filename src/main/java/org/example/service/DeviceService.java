package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.example.dao.DeviceRepository;
import org.example.dao.SensorRepository;
import org.example.dao.UserRepository;
import org.example.dto.DeviceDto;
import org.example.dto.UpdateDeviceDto;
import org.example.entity.Device;
import org.example.entity.Sensor;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private UserRepository userRepository;

    public Pair<Optional<Device>, String> createDevice(DeviceDto deviceDto) {
        if (!findDeviceByName(deviceDto.getName()).isPresent()) {

            List<Sensor> sensors = Collections.EMPTY_LIST;

            for (Integer sensorId : deviceDto.getSensorsId()) {
                Optional<Sensor> sensor = sensorRepository.findById(sensorId);
                if (sensor.isEmpty()) {
                    return Pair.of(Optional.empty(), "Sensor with this id " + sensorId + " doesn't exists!");
                }
                sensors.add(sensor.get());
            }
            Device device = new Device(deviceDto.getName(), sensors);
            return Pair.of(Optional.of(deviceRepository.save(device)), StringUtils.EMPTY);
        }
        return Pair.of(Optional.empty(), "Device with this name already exists!");
    }

    public Pair<Optional<Device>, String> updateDevice(UpdateDeviceDto updateDeviceDto) {
        Optional<Device> existingDevice = findDeviceById(updateDeviceDto.getId());
        if (existingDevice.isEmpty()) {
            return Pair.of(Optional.empty(), "Sensor with this id doesn't exists");
        }

        List<Sensor> sensors = new ArrayList<>();
        for (Integer sensorId : updateDeviceDto.getSensorsId()) {
            Optional<Sensor> existingSensor = sensorRepository.findById(sensorId);
            if (existingSensor.isEmpty()) {
                return Pair.of(Optional.empty(), "Sensor with id " + sensorId + " doesn't exists");
            }
            sensors.add(existingSensor.get());
        }

        Device updatedDevice = existingDevice.get();
        updatedDevice.setName(updateDeviceDto.getName());
        updatedDevice.setSensorList(sensors);
        return Pair.of(Optional.of(deviceRepository.save(updatedDevice)), StringUtils.EMPTY);

    }

    public Pair<Optional<Device>, String> deleteDevice(Integer id) {
        Optional<Device> existingDevice = findDeviceById(id);
        if (existingDevice.isEmpty()) {
            return Pair.of(Optional.empty(), "Device with this id doesn't exists");
        }

        List<User> users = userRepository.findUsersByDevices(existingDevice.get());
        users.forEach(user -> user.setDevices(
                user.getDevices().stream()
                        .filter(device -> !device.getId().equals(existingDevice.get().getId()))
                        .collect(Collectors.toList())));

        deviceRepository.delete(existingDevice.get());
        return Pair.of(Optional.of(existingDevice.get()), StringUtils.EMPTY);
    }


    public Optional<Device> findDeviceById(Integer id) {
        return deviceRepository.findById(id);
    }

    public List<Device> findDevicesBySensorListContains(Sensor sensor) {
        return deviceRepository.findDevicesBySensorListContains(sensor);
    }

    public Optional<Device> findDeviceByName(String name) {
        return deviceRepository.findByName(name);
    }

    public void deleteDevice(Device device) {
        deviceRepository.delete(device);
    }


    public List<Device> findAllDevices() {
        return deviceRepository.findAll();
    }
}
