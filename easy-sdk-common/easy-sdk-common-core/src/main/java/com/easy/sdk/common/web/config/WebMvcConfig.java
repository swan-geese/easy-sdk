package com.easy.sdk.common.web.config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.easy.sdk.common.core.ApplicationOperation;
import com.easy.sdk.common.core.WebMvcConfiguration;
import com.easy.sdk.common.web.controller.JsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import com.easy.sdk.common.annotation.ArgumentAnnotation;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;

@Slf4j
@Configuration
@ConditionalOnMissingBean(WebMvcConfiguration.class)
public class WebMvcConfig extends WebMvcConfigurationSupport {

	@Resource
	private ResourceProperties resourceProperties;

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// 获取自定义的参数解析器
		List<HandlerMethodArgumentResolver> list = ApplicationOperation
				.getInterfaceImplClass(HandlerMethodArgumentResolver.class);
		List<HandlerMethodArgumentResolver> arguments = list.stream()
				.filter(l -> l.getClass().isAnnotationPresent(ArgumentAnnotation.class)).collect(Collectors.toList());
		argumentResolvers.addAll(arguments);
		super.addArgumentResolvers(argumentResolvers);
	}

	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new JsonHttpMessageConverter());
		super.configureMessageConverters(converters);

	}

	/**
	 * 添加文件映射地址
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 注意优先级
		String[] staticLocations = ArrayUtil.addAll(resourceProperties.getStaticLocations(),
				this.getDefaultClasspathResourceLocations());
		// 去重
		List<String> list = CollUtil.toList(staticLocations).stream().filter(s -> StrUtil.isNotBlank(s)).distinct()
				.collect(Collectors.toList());
		staticLocations = ArrayUtil.toArray(list, String.class);
		//
		log.info("staticLocations={}", Arrays.toString(staticLocations));
		registry.addResourceHandler("/**").addResourceLocations(staticLocations);
		super.addResourceHandlers(registry);
	}

	/**
	 * 跨域访问
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowCredentials(true).allowedOrigins("*").allowedMethods("GET", "POST");
	}

	/**
	 * @return {@link ResourceProperties.CLASSPATH_RESOURCE_LOCATIONS}
	 */
	private String[] getDefaultClasspathResourceLocations() {
		Field field = ReflectUtil.getField(ResourceProperties.class, "CLASSPATH_RESOURCE_LOCATIONS");
		field.setAccessible(true);
		try {
			return (String[]) field.get(ResourceProperties.class);
		} catch (Exception e) {
			log.warn("[获取默认资源路径失败:{}]", e.getMessage());
		}
		return ArrayUtil.newArray(String.class, 0);
	}

}