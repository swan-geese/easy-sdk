package com.easy.sdk.common.config.auth.config;

import com.easy.sdk.common.config.redis.impl.RedisServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.easy.sdk.common.config.auth.core.AuthUserPerm;
import com.easy.sdk.common.config.auth.core.AuthUserProxy;
import com.easy.sdk.common.config.auth.core.AuthUserResolver;
import com.easy.sdk.common.config.redis.IRedisService;

@Configuration
public class AuthUserConfig {

	@Bean
	@ConfigurationProperties(prefix = "easy.sdk.auth")
	public AuthUserPerm authUserPerm() {
		return new AuthUserPerm();
	}

	@Bean
	@ConditionalOnMissingBean(IRedisService.class)
	public IRedisService redisService(StringRedisTemplate redisTemplate) {
		return new RedisServiceImpl(redisTemplate);
	}

	@Bean
	public AuthUserResolver authUserResolver(IRedisService redisService, AuthUserPerm authUserPerm) {
		AuthUserResolver resolver = new AuthUserResolver();
		resolver.setRedisService(redisService);
		resolver.setType(authUserPerm.getResolverType());
		return resolver;
	}

	@Bean
	public AuthUserProxy userPermProxy(AuthUserPerm authUserPerm) {
		AuthUserProxy proxy = new AuthUserProxy();
		proxy.setAuthUserPerm(authUserPerm);
		return proxy;
	}

}
