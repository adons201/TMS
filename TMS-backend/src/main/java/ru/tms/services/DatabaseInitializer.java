package ru.tms.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Retryable(value = { SQLException.class }, maxAttempts = 5, backoff = @Backoff(delay = 5000))
    public void initializeDatabase() throws SQLException {
        logger.info("Attempting to connect to the database...");
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            logger.info("Successfully connected to the database.");
        } catch (SQLException e) {
            logger.error("Failed to connect to the database: {}", e.getMessage());
            throw e; // Re-throw the exception to trigger retry
        }
    }

    @Recover
    public void recover(SQLException e) {
        logger.error("Database connection failed after multiple retries. Application may not function correctly.", e);
        // Perform any necessary fallback operations here, or simply log the error
    }
}