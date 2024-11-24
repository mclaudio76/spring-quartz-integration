package it.limontainformatica.schedulerspring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

    @Value("${spring.quartz.dataSource.username}")
    private String username;

    @Value("${spring.quartz.dataSource.password}")
    private String password;

    @Value("${spring.quartz.dataSource.url}")
    private String connectionUrl;

    @Value("${spring.quartz.dataSource.driver-class-name}")
    private String driverClassName;

    @QuartzDataSource
    @Bean(name = "quartzDataSource")
    public DataSource quartzDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(connectionUrl)
                .username(username)
                .password(password)
                .build();
    }


}
