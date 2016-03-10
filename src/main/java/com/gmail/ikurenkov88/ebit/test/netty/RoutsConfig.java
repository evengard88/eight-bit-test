package com.gmail.ikurenkov88.ebit.test.netty;

import com.gmail.ikurenkov88.ebit.test.netty.controllers.HttpHumanEdgeWeightController;
import com.gmail.ikurenkov88.ebit.test.netty.controllers.NotFoundController;
import com.gmail.ikurenkov88.ebit.test.service.calculation.HumanPyramidCalc;
import io.netty.handler.codec.http.router.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ilia on 10.03.16.
 */
@Configuration
public class RoutsConfig {
    @Bean
    Router<Class> getRouter() {
        Router<Class> router = new Router<Class>()
                .GET("/weight", HttpHumanEdgeWeightController.class)
                .GET("/weight/:level/:index", HttpHumanEdgeWeightController.class)
                .notFound(NotFoundController.class);
        return router;
    }
}
