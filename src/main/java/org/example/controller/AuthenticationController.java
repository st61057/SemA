package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.dto.UserDto;
import org.example.entity.AuthToken;
import org.example.entity.User;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

//@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@RequestMapping(path = "/public")
public class AuthenticationController {

    private final UserService userService;

    private final ModelMapper modelMapper;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        Optional<User> login = userService.findUserByUsername(loginDto.getUsername());
        if (login.isEmpty()) {
            return ResponseEntity.badRequest().body("User doesn't exists");
        }
        boolean isAuthenticated = userService.authenticated(login.get(), loginDto);
        if (isAuthenticated) {
            return ResponseEntity.ok(new AuthToken("token"));
        }
        return ResponseEntity.badRequest().body("Autentikace v piči");

//            String token = jwtService.createToken(login);
//            return ResponseEntity.ok(new AuthToken(token));

    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        Pair<Optional<User>, String> creation = userService.createUser(registerDto);
        Optional<User> user = creation.getFirst();
        if (user.isPresent()) {
            return ResponseEntity.ok(convertUserToDto(user.get()));
        }

        return ResponseEntity.badRequest().body(creation.getSecond());
    }

    private UserDto convertUserToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setDevices(user.getDevices());
        return userDto;
    }
}
