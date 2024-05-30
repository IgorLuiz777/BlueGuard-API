package br.com.blue.guard.api.model;

public enum RoleName {
    ROLE_CUSTOMER(2L),
    ROLE_ADMINISTRATOR(1L);

    Long id;

    RoleName(Long id) {
        this.id = id;
    }
}