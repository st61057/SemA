package org.example.dao;

import org.example.entity.Device;
import org.example.entity.Sensor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DeviceRepository extends ElasticsearchRepository<Device, String> {

    Optional<Device> findById(String id);

    Optional<Device> findByName(String name);

    List<Device> findDevicesBySensorListContains(Sensor sensor);

}
