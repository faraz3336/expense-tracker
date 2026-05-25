package com.fullStack.expenseTracker.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final String PROPERTY_SOURCE_NAME = "databaseUrlProperties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (hasText(environment.getProperty("spring.datasource.url"))) {
            return;
        }

        String databaseUrl = environment.getProperty("DATABASE_URL");
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
    }

    private DatabaseConnection parse(String databaseUrl) {
        URI uri = URI.create(databaseUrl.replaceFirst("^postgresql://", "postgres://"));
        String[] userInfo = uri.getRawUserInfo().split(":", 2);
        String username = decode(userInfo[0]);
        String password = userInfo.length > 1 ? decode(userInfo[1]) : "";
        String database = uri.getPath() == null || uri.getPath().isBlank()
                ? "postgres"
                : uri.getPath().substring(1);

        Map<String, String> params = new LinkedHashMap<>();
        params.put("sslmode", "require");
        params.put("currentSchema", "expense_tracker");
        params.put("prepareThreshold", "0");

        String jdbcUrl = "jdbc:postgresql://%s:%d/%s?%s".formatted(
                uri.getHost(),
                uri.getPort() == -1 ? 5432 : uri.getPort(),
                database,
                toQueryString(params)
        );

        return new DatabaseConnection(jdbcUrl, username, password);
    }

    private String toQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((left, right) -> left + "&" + right)
                .orElse("");
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private record DatabaseConnection(String jdbcUrl, String username, String password) {
    }
}
