package br.com.blue.guard.api.config;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.blue.guard.api.model.BeachReport;
import br.com.blue.guard.api.model.Role;
import br.com.blue.guard.api.model.RoleName;
import br.com.blue.guard.api.model.User;
import br.com.blue.guard.api.repository.BeachReportRepository;
import br.com.blue.guard.api.repository.RoleRepository;
import br.com.blue.guard.api.repository.UserRepository;

@Configuration
public class DataBaseSeeder implements CommandLineRunner {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BeachReportRepository beachReportRepository;

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

        Optional<User> user1 = userRepository.findByUsername("Igor");
        Optional<User> user2 = userRepository.findByUsername("Usuario teste02");
        Optional<User> user3 = userRepository.findByUsername("Usuario teste03");

        User user1Object = user1.orElse(null);
        User user2Object = user2.orElse(null);
        User user3Object = user3.orElse(null);

        beachReportRepository.saveAll(
                List.of(
                        BeachReport.builder().user(user1Object)
                                .location("Pria de Ponta Negra")
                                .condition("BOA")
                                .description("Água clara e pria limpa.")
                                .imageUrl("https://feriasemnatal.com.br/animacoes/passeios/banner/").build(),
                        BeachReport.builder().user(user2Object)
                                .location("Pria Grande")
                                .condition("RUIM")
                                .description("Pria está bastante suja após o feriado.")
                                .imageUrl("https://pensamentoverde.com.br/wp-content/uploads/2014/03/img177.jpg").build(),
                        BeachReport.builder().user(user3Object)
                                .location("Costa dos Corais")
                                .condition("HORRÍVEL")
                                .description("Água muito suja com vazamento de óleo.")
                                .imageUrl("https://projetocolabora.com.br/wp-content/uploads/2020/12/desde-1.jpg").build()
                )
        );
    }
}
