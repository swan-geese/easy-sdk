package com.easy.sdk.common.extra.map.tencent;

import java.util.Map;

import com.easy.sdk.common.exception.BusinessException;
import com.easy.sdk.common.extra.map.MapService;
import com.easy.sdk.common.extra.map.tencent.address.AddressResolveDTO;
import com.easy.sdk.common.extra.map.tencent.address.AddressVO;
import com.easy.sdk.common.extra.map.tencent.distance.DistanceResolveDTO;
import com.easy.sdk.common.extra.map.tencent.distance.DistanceVO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 腾讯地图
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.5
 */
@Slf4j
public class TencentMapService implements MapService {

	public TencentMapService(String key) {
		this.key = key;
	}

	private String key;

	private static final Integer SUCCESS = 0;

	private final String addressUrl = "https://apis.map.qq.com/ws/geocoder/v1/";

	private final String distanceUrl = "https://apis.map.qq.com/ws/distance/v1/";

	@Override
	public AddressResolveDTO addressToCoordinate(AddressVO vo) throws Exception {
		vo.setKey(key);
		log.info("[Tencent 请求参数：vo={}]", vo);
		Map<String, Object> map = BeanUtil.beanToMap(vo);
		String post = HttpUtil.post(addressUrl, map);
		AddressResolveDTO dto = JSONUtil.toBean(post, AddressResolveDTO.class);
		if (!SUCCESS.equals(dto.getStatus())) {
			throw new BusinessException(dto.getMessage());
		}
		log.info("[位置解析结果：]{}", post);
		// 校验地址是否合格
		AddressResolveDTO.ResultBean result = dto.getResult();
		if (result.getLevel() >= 9 && result.getReliability() >= 7) {
			return dto;
		}
		throw new BusinessException("解析精度未达到门址 或 可信度较低：[address=" + result.getTitle() + "]");
	}

	/**
	 * 距离计算
	 */
	@Override
	public DistanceResolveDTO distanceOneToMany(DistanceVO vo) throws Exception {
		vo.setKey(key);
		Map<String, Object> map = BeanUtil.beanToMap(vo);
		String get = HttpUtil.get(distanceUrl, map);
		DistanceResolveDTO dto = JSONUtil.toBean(get, DistanceResolveDTO.class);
		if (!SUCCESS.equals(dto.getStatus())) {
			throw new BusinessException(dto.getMessage());
		}
		return dto;
	}

}
