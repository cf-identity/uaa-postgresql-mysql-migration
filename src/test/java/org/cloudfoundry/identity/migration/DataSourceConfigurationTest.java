package org.cloudfoundry.identity.migration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DataSourceConfigurationTest {

    @Autowired
    private DataSource mysqlDataSource;

    @Autowired
    private DataSource postgresqlDataSource;

    @Test
    public void testDataSourceCreation() {
        assertNotNull(mysqlDataSource);
        assertNotNull(postgresqlDataSource);
    }


    @Test
    public void testDataSourceWorking() {
        assertEquals(1, new JdbcTemplate(mysqlDataSource).queryForInt("SELECT 1"));
        assertEquals(1, new JdbcTemplate(postgresqlDataSource).queryForInt("SELECT 1"));
    }

}