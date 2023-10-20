package com.easy.sdk.common.base;

import com.easy.sdk.common.constant.ExceptionConstant;
import com.easy.sdk.common.constant.ResultCodeEnum;

import cn.hutool.core.util.StrUtil;

public interface BaseConvert extends BaseWirter {
	/**
	 * 退出
	 * @param error 异常信息
	 */
	default void exit(String error) {
		if (StrUtil.isBlank(error)) {
			error = ExceptionConstant.ERROR;
		}
		wirteJsonObject(ResultCodeEnum.CODE_500.code(), error);
		exit();
	}

}
