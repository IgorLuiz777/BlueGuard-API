package br.com.blue.guard.api.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.blue.guard.api.model.RoleName;
import br.com.blue.guard.api.model.User;
import br.com.blue.guard.api.repository.RoleRepository;
import br.com.blue.guard.api.repository.UserRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByName(RoleName.ADMIN);

        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
            user -> System.out.println("admin already exists!"), 
            () -> {
                var user = new User();
                user.setUsername("admin");
                user.setEmail("admin@gmail.com");
                user.setPassword(bCryptPasswordEncoder.encode("senhaforte123"));
                roleAdmin.ifPresent(role -> user.setRoles(Set.of(role)));
                userRepository.save(user);
            });
    }

}
