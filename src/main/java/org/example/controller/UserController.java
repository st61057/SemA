package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.dto.basic.UserDeviceDto;
import org.example.dto.updates.NewPasswordDto;
import org.example.dto.updates.UpdateUserDto;
import org.example.dto.basic.UserDto;
import org.example.entity.User;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Tag(name = "User", description = "Users")
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api")
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/user-info")
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        Optional<User> existingUser = userService.findUserByUsername(username);
        if (existingUser.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(existingUser.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not exists");
    }


    @PostMapping(value = "/user-add-device")
    @Operation(
            summary = "Add device to user",
            description = "Adding new device to existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device added to user successfully", content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input")
            }
    )
    public ResponseEntity<?> addSensorToDevice(@RequestBody UserDeviceDto userDeviceDto) {
        Pair<Optional<User>, String> add = userService.addDeviceToUser(userDeviceDto);
        Optional<User> user = add.getFirst();
        if (user.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(user.get()));
        }
        return ResponseEntity.badRequest().body(add.getSecond());
    }


    @PostMapping(value = "/user-remove-device")
    @Operation(
            summary = "Removing device from user",
            description = "Removing device from existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device removed from device successfully", content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input")
            }
    )
    public ResponseEntity<?> removeSensorFromDevice(@Valid @RequestBody UserDeviceDto userDeviceDto) {
        Pair<Optional<User>, String> remove = userService.removeDeviceFromUser(userDeviceDto);
        Optional<User> user = remove.getFirst();
        if (user.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(user.get()));
        }
        return ResponseEntity.badRequest().body(remove.getSecond());
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
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserDto userDto) {
        Pair<Optional<User>, String> update = userService.updateUser(userDto);
        Optional<User> user = update.getFirst();
        if (user.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(user.get()));
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
        Optional<User> user = delete.getFirst();
        if (user.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(user.get()));
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
    public ResponseEntity<?> changePasswordOnExistingUser(@Valid @RequestBody NewPasswordDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        boolean isCurrentPasswordMatching = userService.authenticated(user, userDto.getOldPassword());
        boolean isNewPasswordDifferent = !userDto.getNewPassword().equals(userDto.getOldPassword());

        if (isCurrentPasswordMatching && isNewPasswordDifferent) {
            user.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
            return ResponseEntity.ok(convertUserToDto(userService.updateUser(user)));
        }

        return ResponseEntity.badRequest().body("Problem with password doesn't match or new is similar as current");
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setDevices(user.getDevices());
        return userDto;
    }

}
