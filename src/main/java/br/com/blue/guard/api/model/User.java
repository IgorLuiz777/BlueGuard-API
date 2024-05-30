package br.com.blue.guard.api.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Table(name = "blue_guard_users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank @Column(unique = true)
    private String username;

    @NotBlank @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    // O FetchType.EAGER, as funções são carregadasautomaticamente toda vez, enquanto
    // com FetchType.LAZY, as funções serão carregadas apenas quando forem necessárias.
    @ManyToMany(fetch = FetchType.LAZY) @JoinTable(name = "blue_guard_users_roles",
                                                    joinColumns = @JoinColumn(name = "user_id"),
                                                    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
