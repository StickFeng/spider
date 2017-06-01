package com.study.cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author fengzhiming
 * @version 1.0.0 createTime: 2017/6/1 9:13
 * @see
 * @since 1.0
 */
@Configuration
@Controller
@ComponentScan("com.study.cn")
@EnableCaching
@EnableWebMvc
@SpringBootApplication
public class Application extends SpringBootServletInitializer{

    public static void main(String[] args) throws Exception{
        SpringApplication.run(Application.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
