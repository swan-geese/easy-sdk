package com.easy.sdk.common.extra.map.annotation;

import com.easy.sdk.common.extra.map.tencent.TencentMapService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.easy.sdk.common.extra.map.MapService;

import lombok.Data;

/**
 * 地图功能
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.6
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "easy.sdk.map")
public class MapConfig {

	private String key;

	/**
	 * 腾讯地图服务
	 * @param mapConfig map 配置
	 * @return {@link MapService}
	 */
	@Bean
	public MapService mapService(MapConfig mapConfig) {
		return new TencentMapService(mapConfig.key);
	}

}
