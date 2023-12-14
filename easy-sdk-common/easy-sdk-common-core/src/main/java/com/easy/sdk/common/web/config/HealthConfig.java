package com.easy.sdk.common.web.config;

import com.easy.sdk.common.core.WebMvcConfiguration;
import com.easy.sdk.common.web.controller.HealthController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnMissingBean(WebMvcConfiguration.class)
public class HealthConfig {
	@Bean
	public HealthController healthController() {
		return new HealthController();
	}

}