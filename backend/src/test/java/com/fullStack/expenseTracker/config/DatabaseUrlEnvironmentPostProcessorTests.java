package com.fullStack.expenseTracker.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseUrlEnvironmentPostProcessorTests {

    @Test
    void supportsSupabasePoolerUsernameAndQueryParams() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("DATABASE_URL", "postgresql://postgres.lcqsxhuzafkqmumflntm:p%40ss@aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?pgbouncer=true&sslmode=require");

        new DatabaseUrlEnvironmentPostProcessor().postProcessEnvironment(environment, new SpringApplication());

        assertThat(environment.getProperty("spring.datasource.username"))
                .isEqualTo("postgres.lcqsxhuzafkqmumflntm");
        assertThat(environment.getProperty("spring.datasource.password")).isEqualTo("p@ss");
        assertThat(environment.getProperty("spring.datasource.hikari.username"))
                .isEqualTo("postgres.lcqsxhuzafkqmumflntm");
        assertThat(environment.getProperty("spring.datasource.hikari.data-source-properties.user"))
                .isEqualTo("postgres.lcqsxhuzafkqmumflntm");
        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?sslmode=require&currentSchema=expense_tracker&prepareThreshold=0&user=postgres.lcqsxhuzafkqmumflntm");
    }

    @Test
    void fallsBackToDirectUrlWhenDatabaseUrlIsAbsent() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("DIRECT_URL", "postgresql://postgres.lcqsxhuzafkqmumflntm:secret@aws-1-ap-southeast-1.pooler.supabase.com:5432/postgres?sslmode=require");

        new DatabaseUrlEnvironmentPostProcessor().postProcessEnvironment(environment, new SpringApplication());

        assertThat(environment.getProperty("spring.datasource.username"))
                .isEqualTo("postgres.lcqsxhuzafkqmumflntm");
        assertThat(environment.getProperty("spring.datasource.url"))
                .contains("currentSchema=expense_tracker")
                .contains("sslmode=require")
                .contains(":5432/postgres");
    }

    @Test
    void supportsAtSymbolsInsidePasswordBySplittingAtLastAtSymbol() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("DATABASE_URL", "postgresql://postgres.lcqsxhuzafkqmumflntm:p@ss@aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?sslmode=require");

        new DatabaseUrlEnvironmentPostProcessor().postProcessEnvironment(environment, new SpringApplication());

        assertThat(environment.getProperty("spring.datasource.username"))
                .isEqualTo("postgres.lcqsxhuzafkqmumflntm");
        assertThat(environment.getProperty("spring.datasource.password")).isEqualTo("p@ss");
        assertThat(environment.getProperty("spring.datasource.url"))
                .startsWith("jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?");
    }
}
