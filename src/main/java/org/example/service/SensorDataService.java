package org.example.service;

import org.example.dao.SensorDataRepository;
import org.example.dao.SensorRepository;
import org.example.entity.Sensor;
import org.example.entity.SensorData;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private SensorRepository sensorRepository;
    private final Random random = new Random();

    public void generateSensorData() {
        Sensor sensor = getRandomSensor();

        if (sensor == null) {
            throw new IllegalStateException("No sensors available to generate data.");
        }

        // Vytvoříme nový záznam SensorData
        SensorData sensorData = new SensorData();
        sensorData.setDataMeasuredTime(Instant.now());
        sensorData.setTemperature(generateRandomBigDecimal(20, 30));
        sensorData.setUsageEnergy(generateRandomBigDecimal(0, 100));
        sensorData.setSensor(sensor);

        sensorDataRepository.save(sensorData);
    }

    private BigDecimal generateRandomBigDecimal(int min, int max) {
        return BigDecimal.valueOf(min + (max - min) * random.nextDouble());
    }

    private Sensor getRandomSensor() {
        List<Sensor> sensors = sensorRepository.findAll();
        if (sensors.isEmpty()) {
            return null;
        }
        return sensors.get(random.nextInt(sensors.size()));
    }
}
