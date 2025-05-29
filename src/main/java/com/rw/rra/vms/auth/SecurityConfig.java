package com.rw.rra.vms.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig {
    @Autowired
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityExceptionHandler securityExceptionHandler;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/actuator/**",
            "/api/v1/auth/**"
    };

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry ->
                        registry
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                                .requestMatchers("/api/v1/users").permitAll()
                                // Payroll Management System endpoints
                                // User/Employee endpoints
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/me").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/{id}").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/users/{id}/status").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/paged").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/role/{role}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/status/{status}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/search").hasRole("ADMIN")

                                // Employment endpoints
                                .requestMatchers(HttpMethod.GET, "/api/v1/employments").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/employments/paged").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/employments/{id}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/employments/employee/{employeeId}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/employments/employee/{employeeId}/paged").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/employments/active").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/employments/active/paged").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/employments").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/employments/{id}").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/employments/{id}").hasRole("ADMIN")

                                // Deduction endpoints
                                .requestMatchers(HttpMethod.GET, "/api/v1/deductions/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.POST, "/api/v1/deductions").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/deductions/{id}").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/deductions/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/deductions/initialize").hasRole("ADMIN")

                                // Payroll endpoints
                                .requestMatchers(HttpMethod.GET, "/api/v1/payroll/payslips").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/payroll/payslips/paged").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/payroll/payslips/{id}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/payroll/payslips/employee/{employeeId}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/payroll/payslips/employee/{employeeId}/paged").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/payroll/payslips/month/{month}/year/{year}").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/payroll/payslips/month/{month}/year/{year}/paged").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/payroll/payslips/employee/{employeeId}/month/{month}/year/{year}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.POST, "/api/v1/payroll/generate/month/{month}/year/{year}").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/payroll/approve/**").hasRole("ADMIN")

                                // Message endpoints
                                .requestMatchers(HttpMethod.GET, "/api/v1/messages").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/messages/paged").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/messages/{id}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/messages/employee/{employeeId}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/messages/employee/{employeeId}/paged").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/messages/employee/{employeeId}/month/{month}/year/{year}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/messages/employee/{employeeId}/month/{month}/year/{year}/paged").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/api/v1/messages/sent/{sent}").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/messages/employee/{employeeId}/sent/{sent}").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                                .requestMatchers(HttpMethod.POST, "/api/v1/messages").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/messages/{id}/send").hasAnyRole("ADMIN", "MANAGER")

                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(securityExceptionHandler);
                    c.accessDeniedHandler(securityExceptionHandler);
                });

        return http.build();
    }
}
