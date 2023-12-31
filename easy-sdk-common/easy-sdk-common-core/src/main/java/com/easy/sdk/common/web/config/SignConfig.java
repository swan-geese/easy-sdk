package com.easy.sdk.common.web.config;

import com.easy.sdk.common.web.annotation.WebConstant;
import com.easy.sdk.common.web.sign.SignAutoProxyCreator;
import com.easy.sdk.common.web.sign.SignInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.hutool.core.lang.Assert;
import lombok.Data;

/**
 * 接口签名配置
 * 
 * @author 薛超
 * @since 2019年12月8日
 * @version 1.0.8
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "easy.sdk.sign")
public class SignConfig {

	private String interceptorNames = WebConstant.SIGN_INTERCEPTOR;
	private boolean proxyTargetClass = true;
	private Integer order = WebConstant.SIGN_ORDER;
	private Integer timeout = 60;// 60秒
	/**
	 * 签名算法类型 MD5|SHA1|SHA256
	 */
	private String signType = "MD5";

	/**
	 * Sign-拦截器
	 * 
	 * @param config {@link SignConfig}
	 * @return {@link SignInterceptor}
	 */
	@Bean(name = WebConstant.SIGN_INTERCEPTOR)
	@ConditionalOnMissingBean(SignInterceptor.class)
	public SignInterceptor signInterceptor(SignConfig config) {
		SignInterceptor interceptor = new SignInterceptor();
		interceptor.setOrder(config.getOrder());
		interceptor.setConfig(config);
		return interceptor;
	}

	/**
	 * 全局Sign拦截
	 * 
	 * @param config {@link SignConfig}
	 * @return {@link SignAutoProxyCreator}
	 */
	@Bean
	public SignAutoProxyCreator SignAutoProxyCreator(SignConfig config) {
		SignAutoProxyCreator creator = new SignAutoProxyCreator();
		creator.setProxyTargetClass(config.proxyTargetClass);
		Assert.notBlank(config.getInterceptorNames(), "interceptorNames 不能为空");
		creator.setInterceptorNames(config.getInterceptorNames());
		return creator;
	}

}