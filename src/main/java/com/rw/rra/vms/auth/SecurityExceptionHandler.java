package com.rw.rra.vms.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.*;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res,
                         org.springframework.security.core.AuthenticationException authEx) throws IOException {
        log.warn("Unauthorized: {}", authEx.getMessage());
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(res.getOutputStream(), Map.of("error", "Unauthorized", "message", authEx.getMessage()));
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res,
                       AccessDeniedException accessEx) throws IOException {
        log.warn("Forbidden: {}", accessEx.getMessage());
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(res.getOutputStream(), Map.of("error", "Forbidden", "message", accessEx.getMessage()));
    }
}
