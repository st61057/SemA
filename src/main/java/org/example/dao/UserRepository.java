package org.example.dao;

import org.example.entity.Device;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findUserByResetCode(String resetCode);

    Set<User> findUsersByDevices(Device device);

    List<User> findUsersByResetCodeTimestampBetween(Timestamp f, Timestamp s);


}
