package com.agustin.backend.security;

import com.agustin.backend.util.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    public JwtAuthFilter(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                    throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);  // removing "Bearer " prefix

            if (jwtHelper.isTokenValid(token)) {
                String email    = jwtHelper.extractEmail(token);
                String role     = jwtHelper.extractRole(token);
                Long   userId   = jwtHelper.extractUserId(token);

                // identifying who this request is from for Spring security
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                        email,   // principal / identificator
                        userId,  // credentials for easy access
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

                SecurityContextHolder.getContext().setAuthentication(auth); // filter run, validate, set context every time
                // spring boot in controller injects automatically from the context (for any proetected controller)
            }
        }

        // for public endpoinst taht do not requerire endpoint
        filterChain.doFilter(request, response);
    }
}