package br.com.blue.guard.api.service;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.blue.guard.api.model.RoleName;
import br.com.blue.guard.api.model.User;
import br.com.blue.guard.api.model.dto.UserPostData;
import br.com.blue.guard.api.repository.RoleRepository;
import br.com.blue.guard.api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User createUser(UserPostData createDto) {
        var userRole = roleRepository.findByName(RoleName.USER);
        var userRegistered = userRepository.findByUsername(createDto.username());

        if (userRegistered.isPresent()) {
            throw new ResponseStatusException(CONFLICT, "Username is already taken.");
        }

        if (userRole.isEmpty()) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "User role not found.");
        }

        User user = new User();
        user.setUsername(createDto.username());
        user.setEmail(createDto.email());
        user.setPassword(bCryptPasswordEncoder.encode(createDto.password()));
        user.setRoles(Set.of(userRole.get()));

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found! ID: " + id));
    }

    public User updateUser(Long id, UserPostData userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found! ID: " + id));

        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.password()));

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "User not found! ID: " + id);
        }

        userRepository.deleteById(id);
    }
}
