package nph.laboratory.template.message;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class ClickhouseConfig {

    @Bean(name = "clickhouseJdbcTemplate")
    public NamedParameterJdbcTemplate chJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.clickhouse.jdbc.Driver");
        dataSource.setUrl("jdbc:clickhouse:http://localhost:18123,localhost:28123,localhost:38123");
        dataSource.setUsername("admin");
        dataSource.setPassword("123456");
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
