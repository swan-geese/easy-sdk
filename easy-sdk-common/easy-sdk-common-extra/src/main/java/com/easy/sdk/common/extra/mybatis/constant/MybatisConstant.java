package com.easy.sdk.common.extra.mybatis.constant;

/**
 * mybatis 相关常量
 * 
 * @author 薛超
 * @since 2020年7月10日
 * @version 1.0.9
 */
public interface MybatisConstant {
	/**
	 * 只查询一条 limit 1
	 */
	String LIMIT_1 = "LIMIT 1";
	String LIMIT_1_MYSQL = "LIMIT 1";
	String LIMIT_1_ORACLE = "ROWNUM = 1";
	/**
	 * 只查询一条
	 */
	String LIMIT_TEMPLATE = "LIMIT {},{}";
	/**
	 * 偏移量0
	 */
	int OFFSET_0 = 0;
	/**
	 * 第一页
	 */
	int CURRENT_PAGE = 1;
	/**
	 * 默认size 为1000
	 */
	Integer SIZE = 1000;

	String ASK = "?";
}
