package com.ecommerce.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "🛒 E-Ticaret API",
                version = "1.0",
                description = "Bu API, bir e-ticaret uygulaması için kullanıcı yönetimi, yetkilendirme ve admin işlemlerini sağlar.",
                contact = @Contact(
                        name = "Özlem Geliştirici",
                        email = "ozlem@example.com"
                )
        ),
        servers = @Server(url = "http://localhost:8080")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
