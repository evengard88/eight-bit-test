package com.gmail.ikurenkov88.ebit.test;


import com.gmail.ikurenkov88.ebit.test.service.calculation.HumanPyramidCalc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean;


import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Double.sum;

/**
 * Created by ilia on 27.02.16.
 */
@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    @Bean
    public ForkJoinPoolFactoryBean getForkJoinPool() {
        ForkJoinPoolFactoryBean forkJoinPoolFactoryBean = new ForkJoinPoolFactoryBean();
        forkJoinPoolFactoryBean.setAsyncMode(true);
        forkJoinPoolFactoryBean.setParallelism(8);
        return forkJoinPoolFactoryBean;
    }
}
