package org.example.apigateway.config;

import io.jsonwebtoken.Claims;
import org.apache.http.HttpHeaders;
import org.example.apigateway.domain.PublicRoutes;
import org.example.apigateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        System.out.println("====== JWT FILTER ======");
        System.out.println("Path recibido: " + path);
        System.out.println("Es público? " + isPublic(path));

        // Si es ruta pública, dejar pasar sin validar token
        if (isPublic(path)) {
            System.out.println("Ruta pública - permitiendo acceso");
            return chain.filter(exchange);
        }

        System.out.println("Ruta protegida - validando token");

        // Obtener el header Authorization
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        System.out.println("Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No hay token o no empieza con 'Bearer '");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        System.out.println("Token extraído: " + token.substring(0, Math.min(20, token.length())) + "...");

        // Validar token UNA SOLA VEZ
        if (!jwtService.isValid(token)) {
            System.out.println("❌ TOKEN INVALIDO");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        System.out.println("✅ Token válido - extrayendo claims");

        // Extraer claims y agregar headers personalizados
        Claims claims = jwtService.getClaims(token);
        System.out.println("Subject (email): " + claims.getSubject());
        System.out.println("Role: " + claims.get("role", String.class));

        // Extraer claims
//        Claims claims = jwtService.getClaims(token);
        String role = claims.get("role", String.class);

// 🔐 REGLA: solo ADMIN puede acceder
        if (path.equals("/users/all")) {
            if (!"ROLE_ADMIN".equals(role)) {
                System.out.println("❌ Acceso denegado - no es ADMIN");
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", claims.getSubject())
                .header("X-User-Role", claims.get("role", String.class))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
//    private boolean isPublic(String path) {
//        boolean result = PublicRoutes.PUBLIC_PATHS.stream()
//                .anyMatch(publicPath -> {
//                    boolean matches = path.startsWith(publicPath);
//                    System.out.println("  Comparando '" + path + "' con '" + publicPath + "': " + matches);
//                    return matches;
//                });
//        return result;
//    }
    private boolean isPublic(String path) {
        return PublicRoutes.PUBLIC_PATHS.stream()
                .anyMatch(path::equals);
    }

}