package org.cloudfoundry.identity.migration;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix="datasource.mysql")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder
            .create()
            .build();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix="datasource.postgresql")
    public DataSource postgresqlDataSource() {
        return DataSourceBuilder
            .create()
            .build();
    }

}
