package com.easy.sdk.common.extra.voice.baidu;

import java.io.InputStream;
import java.util.HashMap;

import com.easy.sdk.common.extra.voice.SpeechRecognitionService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.easy.sdk.common.exception.BusinessException;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;

/**
 * 百度语音服务
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.5
 */

public class BaiDuSpeechRecognitionService implements SpeechRecognitionService {

	private TopspeedAipSpeech aipSpeech;

	public BaiDuSpeechRecognitionService(TopspeedAipSpeech aipSpeech) {
		this.aipSpeech = aipSpeech;
	}

	private static final String FILE_SUFFIX = "pcm";

	private static final int RATE = 16000;

	/**
	 * mp3转语音
	 */
	@Override
	public String speechToText(InputStream in, HashMap<String, Object> options) throws Exception {
		try {
			byte[] mp3Convert2pcm = MP3ConvertPCM.mp3Convert2pcm(in);
			// 调用接口
			JSONObject res = aipSpeech.asr(mp3Convert2pcm, FILE_SUFFIX, RATE, options);
			SpeechRecognitionDTO srDTO = JSONUtil.toBean(res.toString(), SpeechRecognitionDTO.class);
			BaiDuSpeechEnum code = BaiDuSpeechEnum.getSpeechByCode(srDTO.getErr_no());
			if (code == BaiDuSpeechEnum.SUCCESS) {
				return StringUtils.join(srDTO.getResult(), ",");
			}
			throw new BusinessException(code.desc);
		} catch (Exception e) {
			throw e;
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 急速版
	 */
	@Override
	public String topspeedSpeechToText(InputStream in, HashMap<String, Object> options) throws Exception {
		try {
			byte[] mp3Convert2pcm = MP3ConvertPCM.mp3Convert2pcm(in);
			// 调用接口
			JSONObject res = aipSpeech.topspeed(mp3Convert2pcm, FILE_SUFFIX, RATE, options);
			SpeechRecognitionDTO srDTO = JSONUtil.toBean(res.toString(), SpeechRecognitionDTO.class);
			BaiDuSpeechEnum code = BaiDuSpeechEnum.getSpeechByCode(srDTO.getErr_no());
			if (code == BaiDuSpeechEnum.SUCCESS) {
				return StringUtils.join(srDTO.getResult(), ",");
			}
			throw new BusinessException(code.desc);
		} catch (Exception e) {
			throw e;
		} finally {
			IoUtil.close(in);
		}
	}

	enum BaiDuSpeechEnum {
		SUCCESS("0", "成功"), //
		VIDEO_POOR("3301", "音频质量过差"), //
		AUTH_FAIL("3302", "鉴权失败"), //
		VIDEO_SERVER_ISSUE("3303", "语音服务器后端问题"), //
		QPS_OVERSTEP("3304", "用户的请求QPS超限"), //
		PV_OVERSTEP("3305", "用户的日pv（日请求量）超限"), //
		VIDEO_SERVER_RECOGNITION_ISSUE("3307", "语音服务器后端识别出错问题"), //
		VIDEO_TIME_TOO_LANG("3308", "音频过长"), //
		VIDEO_DATA_ISSUE("3309", "音频数据问题"), //
		VIDEO_FILE_TOO_LANG("3310", "输入的音频文件过大"), //
		VIDEO_RATE_NOT_FIND("3311", "采样率rate参数不在选项里"), //
		VIDEO_FORMAT_NOT_FIND("3312", "音频格式format参数不在选项里"), //
		ARG_ERROR("3300", "输入参数不正确");

		BaiDuSpeechEnum(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		String code;
		String desc;

		public static BaiDuSpeechEnum getSpeechByCode(String code) {
			for (BaiDuSpeechEnum s : BaiDuSpeechEnum.values()) {
				if (s.code.equals(code)) {
					return s;
				}
			}
			throw new BusinessException("无效的Code" + code);
		}

	}

}
