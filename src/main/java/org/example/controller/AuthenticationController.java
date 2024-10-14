package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.config.JwtTokenUtil;
import org.example.dto.updates.ChangePasswordDto;
import org.example.dto.basic.LoginDto;
import org.example.dto.updates.RegisterDto;
import org.example.dto.basic.UserDto;
import org.example.entity.AuthToken;
import org.example.entity.User;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Authentication", description = "Authentication")
@AllArgsConstructor
@RestController
@RequestMapping(path = "/public")
public class AuthenticationController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final JwtTokenUtil jwtTokenUtil;


    @PostMapping("/login")
    @Operation(
            summary = "Authenticate for an existing user",
            description = "Login to existing user via username and password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(schema = @Schema(implementation = LoginDto.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials"),
                    @ApiResponse(responseCode = "400", description = "User doesn't exists")
            }
    )
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        Optional<User> login = userService.findUserByUsername(loginDto.getUsername());
        if (login.isEmpty()) {
            return ResponseEntity.badRequest().body("User doesn't exists");
        }
        boolean isAuthenticated = userService.authenticated(login.get(), loginDto);
        if (isAuthenticated) {
            String token = jwtTokenUtil.generateToken(login.get().getUsername());
            return ResponseEntity.ok(new AuthToken(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

    }

    @PostMapping(value = "/register")
    @Operation(
            summary = "Register new user",
            description = "Create new user account via username, password and email",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "User is already existing")
            }
    )
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        Pair<Optional<User>, String> creation = userService.createUser(registerDto);
        Optional<User> user = creation.getFirst();
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(convertUserToDto(user.get()));
        }

        return ResponseEntity.badRequest().body(creation.getSecond());
    }

    @PostMapping(value = "/reset-request/{username}")
    @Operation(
            summary = "Sending reset code on email",
            description = "Sends to users personal ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset successfully"),
                    @ApiResponse(responseCode = "400", description = "User doesn't exists")
            }
    )
    public ResponseEntity<?> sendResetCode(@PathVariable String username) {
        Optional<User> existingUser = userService.findUserByUsername(username);
        if (existingUser.isPresent()) {
            String resetCode = UUID.randomUUID().toString();
            existingUser.get().setResetCode(resetCode);
            User updatedUser = userService.updateUser(existingUser.get());
            sendEmail(updatedUser.getEmail(), resetCode);
            return ResponseEntity.ok(convertUserToDto(updatedUser));

        }
        return ResponseEntity.badRequest().body("User doesn't exist");
    }


    @PutMapping(value = "/change-password")
    @Operation(
            summary = "Reset password via reset code",
            description = "Validating users reset code, and adding setting new password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid reset code or invalid password")
            }
    )
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        Pair<Optional<User>, String> update = userService.updatePassword(changePasswordDto);
        Optional<User> updateUser = update.getFirst();
        if (updateUser.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(updateUser.get()));
        }
        return ResponseEntity.badRequest().body(update.getSecond());
    }

    private void sendEmail(String userEmail, String resetCode) {
        Email email = EmailBuilder.startingBlank()
                .from("nnpdasem@gmail.com")
                .to(userEmail)
                .withSubject("Reset code")
                .withPlainText("Reset code: " + resetCode)
                .buildEmail();


        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "nikoroccat@gmail.com", "sirchtbavwgsepmy")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer();

        mailer.sendMail(email);

        System.out.println("Email sent successfully!");
    }
    //"nnpdasem@gmail.com", "NnpdaTest1"

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setDevices(user.getDevices());
        return userDto;
    }
}
