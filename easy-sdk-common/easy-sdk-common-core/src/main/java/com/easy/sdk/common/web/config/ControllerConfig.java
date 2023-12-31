package com.easy.sdk.common.web.config;

import java.io.Serializable;

import com.easy.sdk.common.web.annotation.WebConstant;
import com.easy.sdk.common.web.controller.ControllerAutoProxyCreator;
import com.easy.sdk.common.web.controller.ControllerInterceptor;
import com.easy.sdk.common.web.controller.ResponseBodyHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.easy.sdk.common.constant.ResultCodeEnum;
import com.easy.sdk.common.entity.Response;

import cn.hutool.core.lang.Assert;
import lombok.Data;

/**
 * controller aop 配置
 * 
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.5
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "easy.sdk.controller")
public class ControllerConfig {

	private String interceptorNames = WebConstant.CONTROLLER_INTERCEPTOR;
	private boolean proxyTargetClass = true;
	private Integer order = WebConstant.CONTROLLER_ORDER;

	/**
	 * 返回值-类型
	 */
	private String responseClass = Response.class.getCanonicalName();
	private Serializable okCode = ResultCodeEnum.CODE_200.code();// 正常返回码
	private Serializable errorCode = ResultCodeEnum.CODE_500.code();// 异常返回码

	/**
	 * controller拦截器
	 * 
	 * @param config {@link ControllerConfig}
	 * @return {@link ControllerInterceptor}
	 */

	@Bean(name = WebConstant.CONTROLLER_INTERCEPTOR)
	@ConditionalOnMissingBean(ControllerInterceptor.class)
	public ControllerInterceptor controllerInterceptor(ControllerConfig config) {
		ControllerInterceptor interceptor = new ControllerInterceptor();
		interceptor.setOrder(config.getOrder());
		return interceptor;
	}

	/**
	 * 全局controller拦截
	 * 
	 * @param config {@link ControllerConfig}
	 * @return {@link ControllerAutoProxyCreator}
	 */
	@Bean
	public ControllerAutoProxyCreator controllerAutoProxyCreator(ControllerConfig config) {
		ControllerAutoProxyCreator creator = new ControllerAutoProxyCreator();
		creator.setProxyTargetClass(config.proxyTargetClass);
		Assert.notBlank(config.getInterceptorNames(), "interceptorNames 不能为空");
		creator.setInterceptorNames(config.getInterceptorNames());
		return creator;
	}

	/**
	 * 全局返回值统一处理
	 * 
	 * @return {@link ResponseBodyHandler}
	 */
	@Bean
	@ConditionalOnMissingBean(ResponseBodyHandler.class)
	public ResponseBodyHandler responseBodyHandler() {
		return new ResponseBodyHandler();
	}

}