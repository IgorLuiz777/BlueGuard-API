package br.com.blue.guard.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.blue.guard.api.model.User;
import br.com.blue.guard.api.repository.UserRepository;

@Configuration
public class DataBaseSeeder implements CommandLineRunner {
    
    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        
        userRepository.saveAll(
            List.of(
                User.builder().id(2L).username("Igor")
                .email("igor@gmail.com").password("senhaforte123").roles(null).build(),
                User.builder().id(3L).username("Usuario teste02")
                .email("testuser02@gmail.com").password("senhaforte123").build(),
                User.builder().id(4L).username("Usuario teste03")
                .email("testuser03@gmail.com").password("senhaforte1234").build(),
                User.builder().id(5L).username("Usuario teste04")
                .email("testuser04@gmail.com").password("senhaforte1245").build()
            )
        );

    }

}
