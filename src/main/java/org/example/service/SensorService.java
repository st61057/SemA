package org.example.service;


import org.apache.commons.lang3.StringUtils;
import org.example.dao.SensorRepository;
import org.example.dto.SensorDto;
import org.example.dto.UpdateSensorDto;
import org.example.entity.Device;
import org.example.entity.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private DeviceService deviceService;

    public List<Sensor> findAllSensors() {
        return sensorRepository.findAll();
    }

    public Pair<Optional<Sensor>, String> createSensor(SensorDto sensorDto) {

        if (!findSensorByName(sensorDto.getName()).isPresent()) {
            Optional<Device> device = deviceService.findDeviceById(sensorDto.getDeviceId());
            Sensor sensor = new Sensor(sensorDto.getName(), device.isPresent() ? device.get() : null);
            return Pair.of(Optional.of(sensorRepository.save(sensor)), StringUtils.EMPTY);
        }
        return Pair.of(Optional.empty(), "Sensor with this name already exists!");
    }

    public Pair<Optional<Sensor>, String> updateSensor(UpdateSensorDto updateSensorDto) {
        Optional<Sensor> existingSensor = findSensorById(updateSensorDto.getId());
        Optional<Device> existingDevice = deviceService.findDeviceById(updateSensorDto.getId());
        if (existingDevice.isEmpty()) {
            return Pair.of(Optional.empty(), "Device with this id doesn't exists");
        }

        if (existingSensor.isPresent()) {
            Sensor updatedSensor = existingSensor.get();
            updatedSensor.setName(updateSensorDto.getName());
            updatedSensor.setDevice(existingDevice.get());
            return Pair.of(Optional.of(sensorRepository.save(updatedSensor)), StringUtils.EMPTY);
        }

        return Pair.of(Optional.empty(), "Sensor with this id doesn't exists");
    }

    public Pair<Optional<Sensor>, String> deleteSensor(Integer id) {
        Optional<Sensor> existingSensor = findSensorById(id);
        if (existingSensor.isEmpty()) {
            return Pair.of(Optional.empty(), "Sensor with this id doesn't exists");
        }

        List<Device> devices = deviceService.findDevicesBySensorListContains(existingSensor.get());
        devices.forEach(device -> deviceService.deleteDevice(device));

        sensorRepository.delete(existingSensor.get());
        return Pair.of(Optional.of(existingSensor.get()), StringUtils.EMPTY);
    }

    public List<Sensor> findSensorsByDeviceId(Integer id){
        return sensorRepository.findSensorsByDeviceId(id);
    }

    public Optional<Sensor> findSensorByName(String name) {
        return sensorRepository.findByName(name);
    }

    public Optional<Sensor> findSensorById(Integer id) {
        return sensorRepository.findById(id);
    }

}
