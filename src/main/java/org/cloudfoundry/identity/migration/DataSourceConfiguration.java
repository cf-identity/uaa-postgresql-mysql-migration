package org.cloudfoundry.identity.migration;

import com.googlecode.flyway.core.Flyway;
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

    public void configureFlyway(Flyway flyway, String type) {
        flyway.setInitOnMigrate(true);
        flyway.setLocations("classpath:org/cloudfoundry/identity/uaa/db/" + type);
        flyway.setInitVersion("1.5.2");
    }

    @Bean
    public Flyway mysqlFlyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(mysqlDataSource());
        configureFlyway(flyway, "mysql");
        return flyway;
    }

    @Bean
    public Flyway postgresqlFlyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(postgresqlDataSource());
        configureFlyway(flyway, "postgresql");
        return flyway;
    }
}
