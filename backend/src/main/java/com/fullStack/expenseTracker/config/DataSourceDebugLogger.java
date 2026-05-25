package com.fullStack.expenseTracker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class DataSourceDebugLogger implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(DataSourceDebugLogger.class);

    private final Environment environment;

    public DataSourceDebugLogger(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        String datasourceUrl = environment.getProperty("spring.datasource.url", "");
        String username = environment.getProperty("spring.datasource.username", "");
        log.info("Datasource configured for host '{}' and user '{}'", extractHost(datasourceUrl), username);
        log.info("Datasource default schema '{}'", environment.getProperty("spring.jpa.properties.hibernate.default_schema", ""));
    }

    private String extractHost(String datasourceUrl) {
        try {
            return URI.create(datasourceUrl.replaceFirst("^jdbc:", "")).getHost();
        } catch (Exception ignored) {
            return "unknown";
        }
    }
}
