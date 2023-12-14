package com.easy.sdk.common.web.annotation;

import com.easy.sdk.common.web.config.HealthConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口参数签名
 * 
 * @author swan-geese
 * @since 2023年12月12日
 * @version 1.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HealthConfig.class)
@Documented
public @interface EnableHealth {

}