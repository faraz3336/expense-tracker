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
        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?pgbouncer=true&sslmode=require");
    }

    @Test
    void fallsBackToDirectUrlWhenDatabaseUrlIsAbsent() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("DIRECT_URL", "postgresql://postgres.lcqsxhuzafkqmumflntm:secret@aws-1-ap-southeast-1.pooler.supabase.com:5432/postgres?sslmode=require");

        new DatabaseUrlEnvironmentPostProcessor().postProcessEnvironment(environment, new SpringApplication());

        assertThat(environment.getProperty("spring.datasource.username"))
                .isEqualTo("postgres.lcqsxhuzafkqmumflntm");
        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:5432/postgres?sslmode=require")
                .contains(":5432/postgres");
    }

    @Test
    void decodesEncodedAtSymbolsInsidePassword() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("DATABASE_URL", "postgresql://postgres.lcqsxhuzafkqmumflntm:Osama%4010@aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?sslmode=require");

        new DatabaseUrlEnvironmentPostProcessor().postProcessEnvironment(environment, new SpringApplication());

        assertThat(environment.getProperty("spring.datasource.username"))
                .isEqualTo("postgres.lcqsxhuzafkqmumflntm");
        assertThat(environment.getProperty("spring.datasource.password")).isEqualTo("Osama@10");
        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://aws-1-ap-southeast-1.pooler.supabase.com:6543/postgres?sslmode=require");
    }
}
