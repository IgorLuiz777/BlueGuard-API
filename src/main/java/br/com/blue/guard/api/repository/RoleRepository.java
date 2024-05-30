package br.com.blue.guard.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.blue.guard.api.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{
    
}
