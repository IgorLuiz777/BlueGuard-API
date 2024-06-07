package br.com.blue.guard.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "BlueGuard API",
        version = "2.0",
        description = "API da aplicação BlueGuard - Sistema para Monitoramento de Praias e Observação de Vida Marinha",
        contact = @Contact(
            name = "Igor Luiz",
            email = "igorluizpereiralima@gmail.com",
            url = "https://github.com/IgorLuiz777"
        ),
        license = @License(
            name = "Repositório - GitHub",
            url = "https://github.com/IgorLuiz777/BlueGuard-API"
        )
    )
)
public class BlueGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlueGuardApplication.class, args);
	}

}
