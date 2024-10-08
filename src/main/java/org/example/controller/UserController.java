package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.updates.NewPasswordDto;
import org.example.dto.updates.UpdateUserDto;
import org.example.dto.basic.UserDto;
import org.example.entity.User;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "User", description = "Users")
@RestController
@RequestMapping(path = "/api")
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;


    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @PutMapping(value = "/update-user")
    @Operation(
            summary = "Update user information",
            description = "Enables update users username, email, assigned devices",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error - Failed to update user"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
            }
    )
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDto userDto) {
        Pair<Optional<User>, String> update = userService.updateUser(userDto);
        Optional<User> device = update.getFirst();
        if (device.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(device.get()));
        }
        return ResponseEntity.badRequest().body(update.getSecond());
    }

    @DeleteMapping(value = "/delete-user/{name}")
    @Operation(
            summary = "Delete user ",
            description = "Deletes users account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Invalid users name")
            }
    )
    public ResponseEntity<?> deleteUser(@PathVariable String name) {
        Pair<Optional<User>, String> delete = userService.deleteUser(name);
        Optional<User> sensor = delete.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(sensor.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(delete.getSecond());
    }

    @PutMapping(value = "/change-password")
    @Operation(
            summary = "Change password",
            description = "Change the current user's password by providing the old password and a new password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid old password or other error")
            }
    )
    public ResponseEntity<?> changePasswordOnExistingUser(@RequestBody NewPasswordDto userDto) {
        Optional<User> existingUser = userService.findUserByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            boolean isCurrentPasswordMatching = user.getPassword().equals(userDto.getOldPassword());
            boolean isNewPasswordDifferent = userDto.getNewPassword().equals(userDto.getOldPassword());

            if (isCurrentPasswordMatching && isNewPasswordDifferent) {
                return ResponseEntity.ok(convertUserToDto(existingUser.get()));
            }

            return ResponseEntity.badRequest().body("Problem with password doesn't match or new is similar as current");
        }
        return ResponseEntity.badRequest().body("User doesn't exist");
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setDevices(user.getDevices());
        return userDto;
    }

}
