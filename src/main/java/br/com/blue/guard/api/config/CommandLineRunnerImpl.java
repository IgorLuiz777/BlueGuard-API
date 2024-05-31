package br.com.blue.guard.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.blue.guard.api.model.Role;
import br.com.blue.guard.api.model.RoleName;
import br.com.blue.guard.api.repository.RoleRepository;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner{

    //Classe que garante que o o roleName no DB seje iniciado com o USER e ADMIN 

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName(RoleName.USER).isEmpty()) {
            Role userRole = new Role();
            userRole.setName(RoleName.USER);
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName(RoleName.ADMIN).isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName(RoleName.ADMIN);
            roleRepository.save(adminRole);
        }
    }
    
}
