package br.com.blue.guard.api.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.blue.guard.api.model.RoleName;
import br.com.blue.guard.api.model.User;
import br.com.blue.guard.api.model.dto.UserCreationData;
import br.com.blue.guard.api.repository.RoleRepository;
import br.com.blue.guard.api.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserCreationData createDto) {

        var userRole = roleRepository.findByName(RoleName.USER);
        var userRegistered = userRepository.findByUsername(createDto.username());

        if (userRegistered.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username is already taken.");
        }

        if (userRole.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User role not found.");
        }

        User user = new User();
        user.setUsername(createDto.username());
        user.setEmail(createDto.email());
        user.setPassword(bCryptPasswordEncoder.encode(createDto.password()));
        user.setRoles(Set.of(userRole.get()));

        userRepository.save(user);

        return new ResponseEntity<>(CREATED);
    }
}
