package org.example.service;


import org.apache.commons.lang3.StringUtils;
import org.example.dao.UserRepository;
import org.example.dto.*;
import org.example.entity.Device;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceService deviceService;

    public Pair<Optional<User>, String> createUser(UserDto userDto) {
        if (!findUserByUsername(userDto.getUsername()).isPresent()) {
            User user = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
            user.setResetCode(UUID.randomUUID().toString());
            return Pair.of(Optional.of(userRepository.save(user)), StringUtils.EMPTY);
        }
        return Pair.of(Optional.empty(), "User with this name already exists!");
    }

    public Pair<Optional<User>, String> updateUser(UpdateUserDto updateUserDto) {

        List<Device> listOfDevices = Collections.EMPTY_LIST;
        updateUserDto.getDevicesId().forEach(deviceId -> listOfDevices.add(deviceService.findDeviceById(deviceId).get()));

        if (listOfDevices.size() != updateUserDto.getDevicesId().size()) {
            return Pair.of(Optional.empty(), "Some sensors with this id doesn't exists");
        }

        Optional<User> existingUser = findUserById(updateUserDto.getId());

        if (existingUser.isEmpty()) {
            return Pair.of(Optional.empty(), "User with this id doesn't exists");
        }

        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(updateUserDto.getUsername());
            updatedUser.setEmail(updatedUser.getEmail());
            updatedUser.setDevices(listOfDevices);

            return Pair.of(Optional.of(userRepository.save(updatedUser)), StringUtils.EMPTY);
        }

        return Pair.of(Optional.empty(), "Sensor with this id doesn't exists");
    }

    public Pair<Optional<User>, String> deleteUser(Integer id) {
        Optional<User> existingUser = findUserById(id);
        if (existingUser.isEmpty()) {
            return Pair.of(Optional.empty(), "User with this id doesn't exists");
        }

        User user = existingUser.get();

        List<Device> devices = user.getDevices();
        devices.forEach(device -> device.setUsers(
                device.getUsers().stream()
                        .filter(u -> !u.getId().equals(user.getId()))
                        .collect(Collectors.toList())));

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

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Pair<Optional<User>, String> updatePassword(ChangePasswordDto changePasswordDto) {
        Optional<User> existingUser = findUserByResetCode(changePasswordDto.getResetCode());
        if (existingUser.isPresent()) {
            return Pair.of(Optional.empty(), "Invalid user");
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

}
