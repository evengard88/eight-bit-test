package com.gmail.ikurenkov88.ebit.test;


import com.gmail.ikurenkov88.ebit.test.netty.server.HttpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean;

/**
 * Created by ilia on 27.02.16.
 */
@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        ctx.getBean(HttpServer.class).start();
    }

    @Bean
    public ForkJoinPoolFactoryBean getForkJoinPool() {
        ForkJoinPoolFactoryBean forkJoinPoolFactoryBean = new ForkJoinPoolFactoryBean();
        forkJoinPoolFactoryBean.setAsyncMode(true);
        forkJoinPoolFactoryBean.setParallelism(8);
        return forkJoinPoolFactoryBean;
    }
}
