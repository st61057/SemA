package org.example.controller;

import org.example.dto.ChangePasswordDto;
import org.example.dto.UpdateUserDto;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

//@CrossOrigin(origins = "*", maxAge = 3600)
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
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDto userDto) {
        Pair<Optional<User>, String> update = userService.updateUser(userDto);
        Optional<User> device = update.getFirst();
        if (device.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(device.get()));
        }
        return ResponseEntity.badRequest().body(update.getSecond());
    }

    @DeleteMapping(value = "/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        Pair<Optional<User>, String> delete = userService.deleteUser(id);
        Optional<User> sensor = delete.getFirst();
        if (sensor.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(sensor.get()));
        }
        return ResponseEntity.badRequest().body(delete.getSecond());
    }


    @PostMapping(value = "/reset-request/{username}")
    public ResponseEntity<?> sendResetCode(@RequestBody String username) {
        Optional<User> existingUser = userService.findUserByUsername(username);
        if (existingUser.isPresent()) {
            String resetCode = UUID.randomUUID().toString();
            existingUser.get().setResetCode(resetCode);
            User updatedUser = userService.updateUser(existingUser.get());
            sendEmail(updatedUser.getUsername(), updatedUser.getEmail(), resetCode);
            return ResponseEntity.ok(convertUserToDto(updatedUser));

        }
        return ResponseEntity.badRequest().body("User doesn't exist");
    }

    @PutMapping(value = "/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        Pair<Optional<User>, String> update = userService.updatePassword(changePasswordDto);
        Optional<User> updateUser = update.getFirst();
        if (updateUser.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(updateUser.get()));
        }
        return ResponseEntity.badRequest().body(update.getSecond());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout() throws AuthenticationException {
        return ResponseEntity.ok("Successfully log out");
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setDevices(user.getDevices());
        return userDto;
    }

    private void sendEmail(String username, String userEmail, String resetCode) {
        Email email = EmailBuilder.startingBlank()
                .from("Test", "test@gmail.com")
                .to(username, userEmail)
                .withSubject("Reset code")
                .withPlainText("Reset code: " + resetCode)
                .buildEmail();


        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "test@gmail.com", "your-password")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();

        mailer.sendMail(email);

        System.out.println("Email sent successfully!");
    }

}
