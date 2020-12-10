package pl.sokolak.MyBooks.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;

//To connect 2 databases - inactive
//@Configuration
//@EnableTransactionManagement
public class PersistenceJPAConfig{

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDataSource(getDataSourceMain(),getDataSourceTest()));
        em.setPackagesToScan(new String[] { "pl.sokolak.MyBooks" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Autowired
    private Environment env;

    // Returns Routing DataSource (MyRoutingDataSource)
    //@Autowired
    @Bean(name = "dataSource")
    public DataSource getDataSource(DataSource dataSourceMain, DataSource dataSourceTest) {

        System.out.println("## Create DataSource from dataSourceMain & dataSourceTest");

        DataSourceRouter dataSourceRouter = new DataSourceRouter();

        dataSourceRouter.initDataSources(dataSourceMain, dataSourceTest);

        return dataSourceRouter;
    }

    @Bean(name = "dataSourceMain")
    public DataSource getDataSourceMain() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    @Bean(name = "dataSourceTest")
    public DataSource getDataSourceTest() {
        return
                new EmbeddedDatabaseBuilder()
                        .setType(EmbeddedDatabaseType.H2)
                        .setName("testD")
                        //.addScript("classpath:h2.schema.sql")
                        .build();
    }

    //@Autowired
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager getTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();

        txManager.setDataSource(dataSource);

        return txManager;
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

}
