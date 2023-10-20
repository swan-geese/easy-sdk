package com.easy.sdk.common.extra.mybatis.generator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.easy.sdk.common.extra.mybatis.generator.engine.ZipVelocityTemplateEngine;

import lombok.extern.slf4j.Slf4j;

/**
 * 生成代码直接传至浏览器(zip压缩包)
 * @author 薛超
 * @since 2019年8月1日
 * @version 1.0.5
 */
@Slf4j
public class ZipAutoGenerator extends AutoGenerator {

	/**
	 * 输出到流-zip
	 * @param out 输出流
	 * @throws IOException  生成模板文件抛出的异常
	 */
	public void execute(OutputStream out) throws Exception {
		// 初始化配置
		config = new ConfigBuilder(super.getPackageInfo(), super.getDataSource(), super.getStrategy(),
				super.getTemplate(), super.getGlobalConfig());
		if (null != injectionConfig) {
			injectionConfig.setConfig(config);
		}
		if (null == super.getTemplateEngine()) {
			// 为了兼容之前逻辑，采用 Velocity 引擎 【 默认 】
			super.setTemplateEngine(new ZipVelocityTemplateEngine());
		}
		// 模板引擎初始化执行文件输出
		ZipVelocityTemplateEngine engine = (ZipVelocityTemplateEngine) super.getTemplateEngine()
				.init(this.pretreatmentConfigBuilder(config));
		// 使用zip输出流
		try (ZipOutputStream zip = new ZipOutputStream(out);) {
			// 输出
			engine.batchOutput(zip);
		}
		log.info("==========================文件生成完成！！！==========================");
	}
}