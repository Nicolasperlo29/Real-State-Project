//package org.example.usuarios.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.example.usuarios.service.implementacion.JwtServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtServiceImpl jwtService;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        String path = request.getServletPath();
//
//        // Rutas públicas
//        if (path.startsWith("/auth/login") ||
//                path.startsWith("/auth/register") ||
//                path.startsWith("/auth/refresh") ||
//                path.startsWith("/v3/api-docs") ||
//                path.startsWith("/swagger-ui")) {
//
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String header = request.getHeader("Authorization");
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = header.substring(7);
//
//        try {
//            String email = jwtService.getEmailFromToken(token);
//            String role = jwtService.getRoleFromToken(token);
//
//            if (email != null && role != null) {
//                String authority = role.startsWith("ROLE_")
//                        ? role
//                        : "ROLE_" + role;
//
//                UserDetails userDetails = new User(
//                        email,
//                        "",
//                        Collections.singletonList(new SimpleGrantedAuthority(authority))
//                );
//
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails, null, userDetails.getAuthorities()
//                        );
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Token inválido o expirado");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String getJwtFromRequest(HttpServletRequest request) {
//        String header = request.getHeader("Authorization");
//        if (header != null && header.startsWith("Bearer ")) {
//            return header.substring(7);
//        }
//        return null;
//    }
//}
