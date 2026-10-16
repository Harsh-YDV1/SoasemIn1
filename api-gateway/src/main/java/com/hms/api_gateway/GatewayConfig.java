package com.hms.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("http://localhost:8085"))

                .route("patient-service", r -> r
                        .path("/patients/**")
                        .uri("http://localhost:8081"))

                .route("doctor-service", r -> r
                        .path("/doctors/**")
                        .uri("http://localhost:8082"))

                .route("appointment-service", r -> r
                        .path("/appointments/**")
                        .uri("http://localhost:8083"))

                .route("billing-service", r -> r
                        .path("/bills/**")
                        .uri("http://localhost:8084"))

                .build();
    }
}