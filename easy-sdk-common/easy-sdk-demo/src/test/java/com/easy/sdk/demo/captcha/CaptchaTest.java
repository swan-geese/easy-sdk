package com.easy.sdk.demo.captcha;

import com.easy.sdk.common.util.HyalineCaptchaUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.Image;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/11/22 6:13 PM
 * @description 验证码测试类
 */

@SpringBootTest
@Slf4j
public class CaptchaTest {

    @Test
    public void testCaptch() {
        HyalineCaptchaUtil.HyalineCircleCaptcha captcha = HyalineCaptchaUtil.createCircleCaptcha(100, 42, 4, 3);
        String code = captcha.getCode();
        log.info("[验证码: {}]", code);
        captcha.write("resources/static/captcha.png");
    }
}
