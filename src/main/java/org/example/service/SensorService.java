package org.example.service;


import org.apache.commons.lang3.StringUtils;
import org.example.dao.SensorRepository;
import org.example.dto.basic.SensorDto;
import org.example.dto.updates.UpdateSensorDto;
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
            Sensor sensor = new Sensor(sensorDto.getName());
            return Pair.of(Optional.of(sensorRepository.save(sensor)), StringUtils.EMPTY);
        }
        return Pair.of(Optional.empty(), "Sensor with this name already exists!");
    }

    public Pair<Optional<Sensor>, String> updateSensor(UpdateSensorDto updateSensorDto) {
        Optional<Sensor> existingSensor = findSensorByName(updateSensorDto.getOldName());

        if (existingSensor.isPresent()) {
            Sensor updatedSensor = existingSensor.get();
            updatedSensor.setName(updateSensorDto.getNewName());
            return Pair.of(Optional.of(sensorRepository.save(updatedSensor)), StringUtils.EMPTY);
        }

        return Pair.of(Optional.empty(), "Sensor with this name doesn't exists");
    }

    public Pair<Optional<Sensor>, String> deleteSensor(String name) {
        Optional<Sensor> existingSensor = findSensorByName(name);
        if (existingSensor.isEmpty()) {
            return Pair.of(Optional.empty(), "Sensor with this name doesn't exists");
        }
        Sensor deletedSensor = existingSensor.get();

        List<Device> devices = deviceService.findDevicesBySensorListContains(deletedSensor);
        devices.forEach(device -> device.getSensorList().remove(existingSensor));

        sensorRepository.delete(deletedSensor);
        return Pair.of(Optional.of(deletedSensor), StringUtils.EMPTY);
    }

    public Optional<Sensor> findSensorByName(String name) {
        return sensorRepository.findByName(name);
    }

}
