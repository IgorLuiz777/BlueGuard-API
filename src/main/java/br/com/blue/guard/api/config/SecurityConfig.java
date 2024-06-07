package br.com.blue.guard.api.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.public.key.path}")
    public RSAPublicKey publicKey;

    @Value("${jwt.private.key.path}")
    public RSAPrivateKey privateKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/index.css").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/swagger-ui.css").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui.css").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/swagger-ui-bundle.js").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui-bundle.js").permitAll()
                .requestMatchers(HttpMethod.GET, "/v3/api-docs/swagger-config").permitAll()
                .requestMatchers(HttpMethod.GET, "/v3/api-docs").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/swagger-ui-standalone-preset.js").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/swagger-initializer.js").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/favicon-32x32.png").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.HEAD, "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.POST, "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.PUT, "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.TRACE, "/swagger-ui/index.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/docs").permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }


    @Bean
    public JwtDecoder JwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
