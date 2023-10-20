package com.easy.sdk.common.extra.token.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import com.easy.sdk.common.extra.token.proxy.SessionFormTokenProxy;

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class FormTokenConfig {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SessionFormTokenProxy tokenResetProxy() {
        return new SessionFormTokenProxy();
    }

}