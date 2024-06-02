package br.com.blue.guard.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.blue.guard.api.model.RoleName;
import br.com.blue.guard.api.model.User;
import br.com.blue.guard.api.model.dto.UserPostData;
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
    public ResponseEntity<Void> createUser(@RequestBody UserPostData createDto) {

        var userRole = roleRepository.findByName(RoleName.USER);
        var userRegistered = userRepository.findByUsername(createDto.username());

        if (userRegistered.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken.");
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

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserPostData userDto) {

        var userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "User not found! ID: " + id);
        }

        User user = userOptional.get();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.password()));

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {

        var userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "User not found! ID: " + id);
        }

        userRepository.deleteById(id);
    }


}
