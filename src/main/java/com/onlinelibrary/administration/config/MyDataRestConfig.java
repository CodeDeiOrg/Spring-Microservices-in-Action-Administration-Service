package com.onlinelibrary.administration.config;

import com.onlinelibrary.administration.entity.Checkout;
import com.onlinelibrary.administration.entity.History;
import com.onlinelibrary.administration.entity.Payment;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

//TODO: Check if this class is necessary
@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {
        config.exposeIdsFor(Payment.class);
        config.exposeIdsFor(Checkout.class);
        config.exposeIdsFor(History.class);
    }
}
