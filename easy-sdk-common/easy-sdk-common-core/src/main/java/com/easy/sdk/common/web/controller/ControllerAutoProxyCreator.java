package com.easy.sdk.common.web.controller;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import com.easy.sdk.common.annotation.ExcludeAnnotation;
import com.easy.sdk.common.web.annotation.WebConstant;

import cn.hutool.core.annotation.AnnotationUtil;

/**
 * controller 注入代理
 * 
 * @author 薛超
 * @since 2019年11月29日
 * @version 1.0.8
 */
@SuppressWarnings("serial")
public class ControllerAutoProxyCreator extends AbstractAutoProxyCreator {

	@Override
	@Nullable
	protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName,
			@Nullable TargetSource targetSource) {
		// 抽取cglib
		if (beanClass.getName().contains(WebConstant.CGLIB_FLAG)) {
			beanClass = beanClass.getSuperclass();
		}
		if (this.isController(beanClass)) {
			return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
		}
		return DO_NOT_PROXY;
	}

	private boolean isController(Class<?> beanClass) {
		// 获取所有controller
		Controller controller = AnnotationUtil.getAnnotation(beanClass, Controller.class);
		if (controller == null) {
			return false;
		}
		// 是否排除
		ExcludeAnnotation exclude = AnnotationUtil.getAnnotation(beanClass, ExcludeAnnotation.class);
		if (exclude != null) {
			return false;
		}
		return true;
	}

}