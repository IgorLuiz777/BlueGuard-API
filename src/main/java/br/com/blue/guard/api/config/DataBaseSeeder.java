package br.com.blue.guard.api.config;

import java.util.List;
import java.util.Set;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.blue.guard.api.model.Role;
import br.com.blue.guard.api.model.RoleName;
import br.com.blue.guard.api.model.User;
import br.com.blue.guard.api.repository.RoleRepository;
import br.com.blue.guard.api.repository.UserRepository;

@Configuration
public class DataBaseSeeder implements CommandLineRunner {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.saveAll(
            List.of(
                Role.builder().name(RoleName.USER).build(),
                Role.builder().name(RoleName.ADMIN).build()
            )
        );

        Optional<Role> userRole = roleRepository.findByName(RoleName.USER);
        Optional<Role> adminRole = roleRepository.findByName(RoleName.ADMIN);

        if (userRole.isEmpty() || adminRole.isEmpty()) {
            throw new RuntimeException("Roles not found in database.");
        }

        userRepository.saveAll(
            List.of(
                User.builder().id(2L).username("Igor")
                .email("igor@gmail.com").password(bCryptPasswordEncoder.encode("senhaforte12")).roles(Set.of(userRole.get())).build(),
                User.builder().id(3L).username("Usuario teste02")
                .email("testuser02@gmail.com").password(bCryptPasswordEncoder.encode("senhaforte123")).roles(Set.of(userRole.get())).build(),
                User.builder().id(4L).username("Usuario teste03")
                .email("testuser03@gmail.com").password(bCryptPasswordEncoder.encode("senhaforte1234")).roles(Set.of(userRole.get())).build(),
                User.builder().id(5L).username("Usuario teste04")
                .email("testuser04@gmail.com").password(bCryptPasswordEncoder.encode("senhaforte12345")).roles(Set.of(userRole.get())).build()
            )
        );
    }
}
