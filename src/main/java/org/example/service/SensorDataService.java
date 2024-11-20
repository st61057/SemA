package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.example.dao.SensorDataRepository;
import org.example.dao.SensorRepository;
import org.example.dto.basic.SensorDataDto;
import org.example.dto.basic.SensorDto;
import org.example.entity.Device;
import org.example.entity.Sensor;
import org.example.entity.SensorData;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private SensorService sensorService;

    private final Random random = new Random();

    public List<SensorData> findAllSensorsData() {
        return sensorDataRepository.findAll();
    }

    public List<SensorData> findSensorDataByName(String name) {
        return sensorDataRepository.findBySensorName(name);
    }

    public Pair<Optional<SensorData>, String> addSensorDataToSensor(@RequestBody SensorDataDto sensorDataDto) {

        if (sensorDataDto.getSensor() == null) {
            return Pair.of(Optional.empty(), "Sensor is empty");
        }
        Optional<Sensor> sensor = sensorService.findSensorByName(sensorDataDto.getSensor().getName());
        if (sensor.isEmpty()) {
            return Pair.of(Optional.empty(), "Sensor with this name doesn't exists");
        }

        SensorData sensorData = new SensorData(sensorDataDto.getDataMeasuredTime(), sensorDataDto.getTemperature(),
                sensorDataDto.getUsageEnergy(), sensor.get());
        return Pair.of(Optional.of(sensorDataRepository.save(sensorData)), StringUtils.EMPTY);
    }

    public void generateSensorData() {
        Sensor sensor = getRandomSensor();

        if (sensor == null) {
            throw new IllegalStateException("No sensors available to generate data.");
        }

        SensorData sensorData = new SensorData();
        sensorData.setDataMeasuredTime(Instant.now());
        sensorData.setTemperature(generateRandomBigDecimal(15, 35));
        sensorData.setUsageEnergy(generateRandomBigDecimal(0, 100));
        sensorData.setSensor(sensor);

        sensorDataRepository.save(sensorData);
    }

    private BigDecimal generateRandomBigDecimal(int min, int max) {
        return BigDecimal.valueOf(min + (max - min) * random.nextDouble());
    }

    private Sensor getRandomSensor() {
        List<Sensor> sensors = sensorService.findAllSensors();
        if (sensors.isEmpty()) {
            return null;
        }
        return sensors.get(random.nextInt(sensors.size()));
    }
}
