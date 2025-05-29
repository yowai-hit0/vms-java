package com.rw.rra.vms.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing database with triggers...");
        try {
            // Load the SQL script from resources
            ClassPathResource resource = new ClassPathResource("db/trigger.sql");
            String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            
            // Execute the SQL script
            jdbcTemplate.execute(sql);
            log.info("Database triggers initialized successfully");
        } catch (IOException e) {
            log.error("Failed to initialize database triggers", e);
            throw e;
        }
    }
}