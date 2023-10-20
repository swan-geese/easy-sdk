package com.easy.sdk.common.extra.mybatis.log;

import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;

import com.easy.sdk.common.base.BaseLogger;

/**
 * 打印sql语句
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.5
 */
public class Slf4jLogImpl implements Log, BaseLogger {

	private Logger logger = null;

	public Slf4jLogImpl(String clazz) {
		try {
			int of = clazz.lastIndexOf(".");
			char c = clazz.charAt(of + 1);
			boolean boo = c >= 'A' && c <= 'Z' ? true : false;
			if (!boo) {
				clazz = clazz.substring(0, of);
			}
			Class<?> name = Class.forName(clazz);
			logger = logger(name);
		} catch (Exception e) {
			error(e);
			logger = logger();
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isTraceEnabled() {
		return true;
	}

	@Override
	public void error(String s, Throwable e) {
		logger.error(s, e);
	}

	@Override
	public void error(String s) {
		logger.error(s);
	}

	@Override
	public void debug(String s) {
		// 打印sql的关键
		logger.info(s);
	}

	@Override
	public void trace(String s) {
		logger.trace(s);
	}

	@Override
	public void warn(String s) {
		logger.warn(s);
	}
}