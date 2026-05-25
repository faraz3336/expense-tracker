package com.fullStack.expenseTracker.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final Logger log = Logger.getLogger(DatabaseUrlEnvironmentPostProcessor.class.getName());
    private static final String PROPERTY_SOURCE_NAME = "databaseUrlProperties";
    private static final String DEFAULT_SCHEMA = "expense_tracker";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = firstText(
                environment.getProperty("DATABASE_URL"),
                environment.getProperty("DIRECT_URL"),
                environment.getProperty("SPRING_DATASOURCE_URL")
        );

        if (!hasText(databaseUrl)) {
            return;
        }

        DatabaseConnection connection = parse(databaseUrl);
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("spring.datasource.url", connection.jdbcUrl());
        properties.put("spring.datasource.username", connection.username());
        properties.put("spring.datasource.password", connection.password());
        properties.put("spring.datasource.driver-class-name", "org.postgresql.Driver");

        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, properties));
        System.out.println("DB_USER=" + connection.username());
        System.out.println("DB_URL=" + connection.jdbcUrl());
        log.info("DB_USER=" + connection.username());
        log.info("DB_URL=" + connection.jdbcUrl());
        log.info("Datasource parsed for host '" + connection.host() + "' and user '" + connection.username() + "'");
        log.info("Datasource schema '" + DEFAULT_SCHEMA + "'");
    }

    private DatabaseConnection parse(String databaseUrl) {
        URI uri = URI.create(databaseUrl.trim());
        String userInfo = uri.getUserInfo();
        if (!hasText(userInfo)) {
            throw new IllegalArgumentException("Database URL must include username and password");
        }

        String[] creds = userInfo.split(":", 2);
        if (creds.length < 2) {
            throw new IllegalArgumentException("Database URL must include a password");
        }

        String username = creds[0];
        String password = creds[1];
        String path = uri.getPath();
        if (!hasText(path)) {
            throw new IllegalArgumentException("Database URL must include a database name");
        }
        String host = uri.getHost();
        int port = uri.getPort() == -1 ? 5432 : uri.getPort();
        String query = uri.getRawQuery();

        String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
        if (hasText(query)) {
            jdbcUrl += "?" + query;
        }

        return new DatabaseConnection(jdbcUrl, username, password, host);
    }

    private String firstText(String... values) {
        for (String value : values) {
            if (hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private record DatabaseConnection(String jdbcUrl, String username, String password, String host) {
    }
}
