package com.easy.sdk.common.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.easy.sdk.common.exception.ExceptionControllerAdvice;
import com.easy.sdk.common.exception.ExceptionController;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ //
		ExceptionController.class, // 全局异常处理
		ExceptionControllerAdvice.class, // 全局控制层异常处理
})
public @interface EnableGlobalException {

}
