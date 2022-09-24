package hu.ponte.hr.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.util.Properties;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@AllArgsConstructor
public abstract class DatabaseConfig {

    public static final String PROFILE_H2 = "h2";

    protected final Environment environment;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("test.datasource.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("test.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("test.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("test.datasource.password"));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    public abstract Properties additionalProperties();
}
