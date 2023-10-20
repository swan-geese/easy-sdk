package com.easy.sdk.common.config.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * redis-spring
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.5
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisConfig.class)
public @interface EnableRedis {

}
