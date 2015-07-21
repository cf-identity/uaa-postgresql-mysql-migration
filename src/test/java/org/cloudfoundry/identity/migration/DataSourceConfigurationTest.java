package org.cloudfoundry.identity.migration;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.api.MigrationInfo;
import com.googlecode.flyway.core.api.MigrationInfoService;
import com.googlecode.flyway.core.api.MigrationState;
import com.googlecode.flyway.core.api.MigrationVersion;
import org.cloudfoundry.identity.uaa.db.DataSourceAccessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DataSourceConfigurationTest {

    public static final String VERSION = "2.4.0";

    @Autowired
    private DataSource mysqlDataSource;

    @Autowired
    private DataSource postgresqlDataSource;

    @Autowired
    private ApplicationContext context;

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

    @Test
    public void check_that_schema_is_at_the_right_version() throws Exception {
        Flyway mysqlflyway = context.getBean("mysqlFlyway", Flyway.class);
        new DataSourceAccessor().setDataSource(mysqlDataSource);
        mysqlflyway.migrate();
        Flyway postgresqlflyway = context.getBean("postgresqlFlyway", Flyway.class);
        postgresqlflyway.migrate();
        new DataSourceAccessor().setDataSource(postgresqlDataSource);
        //printMigrationService("mysql", mysqlflyway.info());
        //printMigrationService("postgresql", postgresqlflyway.info());
        MigrationInfo[] mysqlInfo = mysqlflyway.info().all();
        MigrationInfo[] postgresqlInfo = postgresqlflyway.info().all();
        MigrationVersion expectedVersion = MigrationVersion.fromVersion(VERSION);
        assertEquals(expectedVersion, mysqlInfo[mysqlInfo.length-1].getVersion());
        assertEquals(MigrationState.SUCCESS, mysqlInfo[mysqlInfo.length-1].getState());
        assertEquals(expectedVersion, postgresqlInfo[postgresqlInfo.length-1].getVersion());
        assertEquals(MigrationState.SUCCESS, postgresqlInfo[postgresqlInfo.length-1].getState());
    }

    public void printMigrationService(String prefix, MigrationInfoService service) {
        for (MigrationInfo info : service.all()) {
            System.out.println(prefix+" = [version:" + info.getVersion().getVersion()+ "; state:"+info.getState().getDisplayName()+"]");
        }
    }

}