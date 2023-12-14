package com.easy.sdk.common.extra.access.proxy;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.easy.sdk.common.extra.access.annotation.AccessLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;

import com.easy.sdk.common.base.BaseProxy;
import com.easy.sdk.common.exception.BusinessException;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 接口访问限制-ip+port-controller层拦截
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.5
 */
@Aspect
@Slf4j
public class AccessLimitProxy implements BaseProxy, InitializingBean {
	/**
	 * 定时缓存
	 */
	private final TimedCache<String, Integer> accessLimitCache = CacheUtil.newTimedCache(AccessLimit.DEFAULT_TIME);

	// 环绕拦截
	@Override
	@Around(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
			"|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
			"|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
			"|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
			"|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	public Object around(ProceedingJoinPoint pdj) throws Throwable {
		// 获取method 中的注解
		Method method = this.getMethod(pdj);
		AccessLimit access = method.getAnnotation(AccessLimit.class);
		//method 上未添加 @AccessLimit 注解，则不处理
		if (ObjectUtil.isNull(access)) {
			return pdj.proceed();
		}
		//
		HttpServletRequest request = this.getRequest();
		// 获取key=ip+port+url
		String ip = ServletUtil.getClientIP(request, null);
		String key = StrUtil.format("{}:{}@{}", ip, request.getRemotePort(), request.getRequestURI());

		Integer val = null;

		// 拦截持续时间
		long timeout = ObjectUtil.isNull(access.timeout()) ? AccessLimit.DEFAULT_TIME : access.timeout();
		// 是否可用，false，此注解将无效。
		boolean check = access.enable();
		try {
			if (check) { // 开始校验
				val = accessLimitCache.get(key, false);
				log.info("[access:key={},count={}]", key, ObjectUtil.isNull(val) ? 0 : val);
				// 最大出错次数
				int count = ObjectUtil.isNull(access.count()) ? AccessLimit.DEFAULT_COUNT : access.count();
				if (val != null && val >= count) {
					throw new BusinessException("HTTP请求超出设定的限制");
				}
			}
			// 执行
			return pdj.proceed();
		} catch (Throwable e) {
			log.error("AccessLimitProxy proceed error:", e);
			throw e;
		} finally {
			if (check) {
				// 异常记录+1
				accessLimitCache.put(key, ObjectUtil.isNull(val) ? 1 : val + 1, timeout);
			}
		}
	}

		@Override
		public void afterPropertiesSet() throws Exception {
			// 初始化设置扫描时间
			//启动定时任务，每 DEFAULT_TIME 毫秒检查一次过期
			accessLimitCache.schedulePrune(AccessLimit.DEFAULT_TIME);

		}

}