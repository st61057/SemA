package org.example.service;

import org.example.dao.UserRepository;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UtilService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SensorDataService sensorDataService;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void validateResetCodesValidity() {
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        Timestamp validatedTime = Timestamp.valueOf(currentTime.toLocalDateTime().minusMinutes(15));
        List<User> expiredResetCodes = userRepository.findUsersByResetCodeTimestampBetween(validatedTime, currentTime);

        for (User user : expiredResetCodes) {
            user.setResetCode(null);
            userService.updateUser(user);
        }
    }

//    @Scheduled(fixedRate = 10000)
//    public void generateSensorData() {
//        try {
//            sensorDataService.generateSensorData();
//        } catch (Exception e) {
//            throw new RuntimeException("Error generating sensor data: " + e.getMessage());
//        }
//    }

}
