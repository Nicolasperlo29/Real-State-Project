package org.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("lb://AUTH-SERVICE")
                )

                .route("user-service", r -> r
                        .path("/users/**")
                        .uri("lb://USER-SERVICE")
                )

                .route("properties-service", r -> r
                        .path("/properties/**")
                        .uri("lb://PROPERTIES-SERVICE")
                )

                .route("agenda-service", r -> r
                        .path("/agenda/**")
                        .uri("lb://AGENDA-SERVICE")
                )

                .route("message-service", r -> r
                        .path("/messages/**")
                        .uri("lb://MESSAGE-SERVICE")
                )

                .build();
    }
}
