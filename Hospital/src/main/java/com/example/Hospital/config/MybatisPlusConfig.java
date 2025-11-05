package com.example.Hospital.config;
import org.springframework.context.annotation.Bean;
import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Configuration;


import java.util.Properties;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}
