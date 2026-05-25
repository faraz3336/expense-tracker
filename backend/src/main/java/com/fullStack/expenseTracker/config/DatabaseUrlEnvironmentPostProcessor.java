package com.fullStack.expenseTracker.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        properties.put("spring.datasource.hikari.username", connection.username());
        properties.put("spring.datasource.hikari.password", connection.password());
        properties.put("spring.datasource.hikari.data-source-properties.user", connection.username());
        properties.put("spring.datasource.hikari.data-source-properties.password", connection.password());
        properties.put("spring.datasource.driver-class-name", "org.postgresql.Driver");

        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, properties));
        log.info("DB_USER=" + connection.username());
        log.info("Datasource parsed for host '" + connection.host() + "' and user '" + connection.username() + "'");
        log.info("Datasource schema '" + DEFAULT_SCHEMA + "'");
    }

    private DatabaseConnection parse(String databaseUrl) {
        String withoutScheme = removeScheme(databaseUrl.trim());
        int queryStart = withoutScheme.indexOf('?');
        String authorityAndPath = queryStart == -1 ? withoutScheme : withoutScheme.substring(0, queryStart);
        String rawQuery = queryStart == -1 ? "" : withoutScheme.substring(queryStart + 1);

        int credentialsEnd = authorityAndPath.lastIndexOf('@');
        if (credentialsEnd == -1) {
            throw new IllegalArgumentException("Database URL must include username and password");
        }

        String userInfo = authorityAndPath.substring(0, credentialsEnd);
        String hostAndPath = authorityAndPath.substring(credentialsEnd + 1);
        int userPasswordSeparator = userInfo.indexOf(':');
        if (userPasswordSeparator == -1) {
            throw new IllegalArgumentException("Database URL must include a password");
        }

        String username = decode(userInfo.substring(0, userPasswordSeparator));
        String password = decode(userInfo.substring(userPasswordSeparator + 1));

        int pathStart = hostAndPath.indexOf('/');
        String hostPort = pathStart == -1 ? hostAndPath : hostAndPath.substring(0, pathStart);
        String database = pathStart == -1 ? "postgres" : hostAndPath.substring(pathStart + 1);
        if (!hasText(database)) {
            database = "postgres";
        }

        String host;
        int port;
        int portStart = hostPort.lastIndexOf(':');
        if (portStart > -1) {
            host = hostPort.substring(0, portStart);
            port = Integer.parseInt(hostPort.substring(portStart + 1));
        } else {
            host = hostPort;
            port = 5432;
        }

        Map<String, String> params = parseQuery(rawQuery);
        params.remove("pgbouncer");
        params.putIfAbsent("sslmode", "require");
        params.put("currentSchema", DEFAULT_SCHEMA);
        params.put("prepareThreshold", "0");

        String jdbcUrl = "jdbc:postgresql://%s:%d/%s?%s".formatted(
                host,
                port,
                database,
                toQueryString(params)
        );

        return new DatabaseConnection(jdbcUrl, username, password, host);
    }

    private String removeScheme(String databaseUrl) {
        return databaseUrl
                .replaceFirst("^postgresql://", "")
                .replaceFirst("^postgres://", "")
                .replaceFirst("^jdbc:postgresql://", "");
    }

    private Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> params = new LinkedHashMap<>();
        if (!hasText(rawQuery)) {
            return params;
        }

        for (String pair : rawQuery.split("&")) {
            if (!hasText(pair)) {
                continue;
            }
            String[] parts = pair.split("=", 2);
            params.put(parts[0], parts.length > 1 ? parts[1] : "");
        }
        return params;
    }

    private String toQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((left, right) -> left + "&" + right)
                .orElse("");
    }

    private String firstText(String... values) {
        for (String value : values) {
            if (hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private record DatabaseConnection(String jdbcUrl, String username, String password, String host) {
    }
}
