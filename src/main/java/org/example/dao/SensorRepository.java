package org.example.dao;

import org.example.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    Optional<Sensor> findById(Integer id);

    Optional<Sensor> findByName(String name);

    List<Sensor> findSensorsByDeviceId(Integer id);

}
