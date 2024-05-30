package br.com.blue.guard.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.blue.guard.api.model.Role;
import br.com.blue.guard.api.model.RoleName;

public interface RoleRepository extends JpaRepository<Role,Long>{
    
    Optional<Role> findByName(RoleName name);
}
