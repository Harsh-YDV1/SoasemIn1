package com.hms.api_gateway;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final String SECRET =
            "HMS-SOA-Project-Secret-Key-2026-Change-Me-Please-1234567890";

    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes(StandardCharsets.UTF_8));

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();

        if (path.startsWith("/auth/")
                || HttpMethod.OPTIONS.equals(request.getMethod())) {

            return chain.filter(exchange);
        }

        String authHeader =
                request.getHeaders().getFirst("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            return reject(
                    exchange,
                    "Missing token");
        }

        try {

            String token = authHeader.substring(7);

            Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token);

            return chain.filter(exchange);

        } catch (Exception e) {

            return reject(
                    exchange,
                    "Invalid or expired token");
        }
    }

    private Mono<Void> reject(
            ServerWebExchange exchange,
            String message) {

        ServerHttpResponse response =
                exchange.getResponse();

        response.setStatusCode(
                HttpStatus.UNAUTHORIZED);

        response.getHeaders()
                .add("Content-Type", "application/json");

        String body =
                "{\"error\":\"" + message + "\"}";

        return response.writeWith(
                Mono.just(
                        response.bufferFactory()
                                .wrap(body.getBytes(
                                        StandardCharsets.UTF_8))));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}