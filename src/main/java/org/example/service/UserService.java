package org.example.service;


import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.dao.UserRepository;
import org.example.dto.basic.LoginDto;
import org.example.dto.updates.ChangePasswordDto;
import org.example.dto.updates.RegisterDto;
import org.example.dto.updates.UpdateUserDto;
import org.example.entity.Device;
import org.example.entity.Sensor;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceService deviceService;

    private final PasswordEncoder passwordEncoder;

    public Pair<Optional<User>, String> createUser(RegisterDto registerDto) {
        if (!findUserByUsername(registerDto.getUsername()).isPresent()) {
            User user = new User(registerDto.getUsername(), registerDto.getEmail(), passwordEncoder.encode(registerDto.getPassword()));
            user.setResetCode(UUID.randomUUID().toString());
            return Pair.of(Optional.of(userRepository.save(user)), StringUtils.EMPTY);
        }
        return Pair.of(Optional.empty(), "User with this name already exists!");
    }

    public Pair<Optional<User>, String> updateUser(UpdateUserDto updateUserDto) {

        Optional<User> existingUser = findUserByUsername(updateUserDto.getUsername());

        if (existingUser.isEmpty()) {
            return Pair.of(Optional.empty(), "User with this name doesn't exists");
        }

        List<Device> listOfDevices = new ArrayList<>();

        if (!updateUserDto.getDevicesNames().isEmpty()) {
            for (String deviceId : updateUserDto.getDevicesNames()) {
                Optional<Device> existingDevice = deviceService.findDeviceByName(deviceId);
                if (existingDevice.isEmpty()) {
                    return Pair.of(Optional.empty(), "Device with id" + deviceId + " doesn't exists");
                }

                Device device = existingDevice.get();
                device.setUser(existingUser.get());
                listOfDevices.add(existingDevice.get());
            }
        }

        User updatedUser = existingUser.get();
        updatedUser.setUsername(updateUserDto.getUsername());
        updatedUser.setEmail(updatedUser.getEmail());
        updatedUser.setDevices(listOfDevices);

        return Pair.of(Optional.of(userRepository.save(updatedUser)), StringUtils.EMPTY);

    }

    public Pair<Optional<User>, String> deleteUser(String name) {
        Optional<User> existingUser = findUserByUsername(name);
        if (existingUser.isEmpty()) {
            return Pair.of(Optional.empty(), "User with this name doesn't exists");
        }

        User user = existingUser.get();

        List<Device> devices = new ArrayList<>();
        for (Device device : user.getDevices()){
            device.setUser(null);
            devices.add(device);
        }
        user.setDevices(devices);

        userRepository.delete(user);
        return Pair.of(Optional.of(user), StringUtils.EMPTY);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserByResetCode(String resetCode) {
        return userRepository.findUserByResetCode(resetCode);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Pair<Optional<User>, String> updatePassword(ChangePasswordDto changePasswordDto) {
        Optional<User> existingUser = findUserByResetCode(changePasswordDto.getResetCode());
        if (existingUser.isEmpty()) {
            return Pair.of(Optional.empty(), "Invalid reset code");
        }

        User user = existingUser.get();
        if (validatePassword(changePasswordDto, user)) {
            user.setPassword(changePasswordDto.getNewPassword());
            return Pair.of(Optional.of(userRepository.save(user)), StringUtils.EMPTY);
        }

        return Pair.of(Optional.empty(), "Invalid change of password");

    }

    private boolean validatePassword(ChangePasswordDto changePasswordDto, User user) {
        boolean isResetCodeMatching = changePasswordDto.getResetCode().equals(user.getResetCode());
        boolean isNotCurrentPasswordMatchingWithNew = !user.getPassword().equals(changePasswordDto.getNewPassword());

        return isResetCodeMatching && isNotCurrentPasswordMatchingWithNew;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> login = userRepository.findByUsername(username);
        if (login.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return login.get();
    }

    public boolean authenticated(User user, LoginDto loginDto) {
        return passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
    }

}
