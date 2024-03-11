package com.example.dy.Config;

import com.example.dy.Util.LinkFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {

    @Bean
    public LinkFormatter linkFormatter() {
        return new LinkFormatter();
    }
}
