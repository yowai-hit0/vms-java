package com.rw.rra.vms.auth;

import com.rw.rra.vms.auth.exceptions.InvalidJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] SKIP_PATHS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        for (String skipPath : SKIP_PATHS) {
            if (path.startsWith(skipPath.replace("/**", ""))) {
                return true; // Skip the filter for these paths
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        // Validate Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            Jwt claims = jwtService.parseToken(token);

            if (claims.getUserId() == null || claims.getRole() == null) {
                throw new RuntimeException("Missing token claims");
            }


            // Set authentication
            var auth = new UsernamePasswordAuthenticationToken(
                    claims.getUserId(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority(claims.getRole()))
            );

            auth.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            log.debug("Authentication failed: {}", ex.getMessage());
            authenticationEntryPoint.commence(
                    request,
                    response,
                    new InvalidJwtException(ex.getMessage())
            );
        }
    }
}
