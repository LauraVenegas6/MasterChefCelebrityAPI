package com.dosw.masterchef.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI (Swagger) documentation.
 * Sets up API metadata and server information.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Servidor Local");

        Contact contact = new Contact();
        contact.setName("DOSW Company");
        contact.setEmail("soporte@doswcompany.com");

        Info info = new Info()
                .title("MasterChef Celebrity API")
                .version("1.0.0")
                .description("API REST para la gestión de recetas del programa MasterChef Celebrity. " +
                        "Permite a televidentes, participantes y chefs registrar, consultar y gestionar recetas de cocina.")
                .contact(contact)
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}