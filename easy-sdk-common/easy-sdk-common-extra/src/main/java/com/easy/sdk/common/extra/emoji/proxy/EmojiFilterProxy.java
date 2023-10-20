package com.easy.sdk.common.extra.emoji.proxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.easy.sdk.common.extra.emoji.annotation.EmojiFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.easy.sdk.common.base.BaseProxy;
import com.easy.sdk.common.exception.BusinessException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.emoji.EmojiUtil;
import cn.hutool.json.JSONUtil;

/**
 * emoji 过滤-controller层拦截
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.5
 */
@Aspect
public class EmojiFilterProxy implements BaseProxy {

	// 环绕拦截
	@Around(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object around(ProceedingJoinPoint pdj) throws Throwable {
		Method method = this.getMethod(pdj);
		// 获取method 中的注解
		EmojiFilter emojiFilter = method.getAnnotation(EmojiFilter.class);
		// 获取参数
		List<Object> args = Stream.of(pdj.getArgs()).filter(o -> !isInclude(o)).collect(Collectors.toList());
		// 开始过滤
		if ((emojiFilter == null || emojiFilter.value()) && !CollUtil.isEmpty(args)) {
			// 参数json化
			String jsonStr = JSONUtil.toJsonStr(args);
			// 提取emoji
			List<String> list = EmojiUtil.extractEmojis(jsonStr);
			if (!CollUtil.isEmpty(list)) {
				String emoji = list.stream().collect(Collectors.joining());
				// 抛出异常
				throw new BusinessException(StrUtil.format("[非法字符:emoji:->{}]", emoji));
			}
		}
		return pdj.proceed();
	}

}