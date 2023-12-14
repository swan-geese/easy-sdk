package com.easy.sdk.demo;

import com.easy.sdk.common.extra.access.annotation.EnableAccessLimit;
import com.easy.sdk.common.extra.emoji.annotation.EnableEmojiFilter;
import com.easy.sdk.common.extra.ftp.annotation.EnableFtp;
import com.easy.sdk.common.extra.license.config.EnableLicenseCreator;
import com.easy.sdk.common.extra.license.config.EnableLicenseVerify;
import com.easy.sdk.common.web.annotation.EnableHealth;
import com.easy.sdk.common.web.annotation.EnableCrypto;
import com.easy.sdk.common.web.annotation.EnableGlobalException;
import com.easy.sdk.common.web.annotation.EnableSign;
import com.easy.sdk.common.web.annotation.EnableWeb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCrypto           // crypto加解密
@EnableSign             // sign验签
@EnableWeb              // web相关
@EnableHealth           // 健康检查
@EnableAccessLimit      // 接口限流
@EnableEmojiFilter      // emoji过滤
@EnableGlobalException  // 全局异常
@EnableFtp              // ftp连接池
@EnableLicenseCreator   // license生成
//@EnableLicenseVerify    // license校验
@SpringBootApplication
public class EasySdkDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasySdkDemoApplication.class, args);
    }

}
