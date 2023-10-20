package com.easy.sdk.common.extra.map.tencent.address;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class AddressVO {
	/**
	 * address=北京市海淀区彩和坊路海淀西大街74号
	 */
	@NotEmpty(message = "地址不能为空")
	private String address;
	/**
	 * region=北京
	 */
	private String region;
	/**
	 * key=百度申请
	 */
	private String key;
	/**
	 * output=json
	 */
	private String output;
	/**
	 * callback=function1
	 */
	private String callback;

}
