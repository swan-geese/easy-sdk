package com.easy.sdk.common.extra.license.config;

import com.easy.sdk.common.util.EntityUtil;

import cn.hutool.crypto.asymmetric.SignAlgorithm;
import de.schlichtherle.license.LicenseNotary;

/**
 * license一些操作
 * 
 * @author 薛超
 * @since 2020年12月15日
 * @version 1.0.0
 */
public interface LicenseInit {

	/**
	 * 修改签名算法
	 * 
	 * @param algorithm 签名算法 {@link SignAlgorithm}
	 * @throws Exception 签名异常
	 */
	default void setLicenseNotaryAlgorithm(SignAlgorithm algorithm) throws Exception {
		EntityUtil.setPrivateFinalField(LicenseNotary.class, "SHA1_WITH_DSA", algorithm.getValue());
	}

}
