package com.zonbeozon.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@TestConfiguration
public class TestBatchConfig {

    /**
     * 임베디드 db에서는 spring batch repo들이 자동으로 추가되지 않기 때문에 별도로 스크립트 추가
     */
    @Bean
    public DataSourceInitializer batchTablesInitializer(DataSource dataSource) {
        ClassPathResource script = new ClassPathResource("/org/springframework/batch/core/schema-h2.sql");

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(script);
        populator.setContinueOnError(false);
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
