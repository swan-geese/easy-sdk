package com.easy.sdk.demo.captcha.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/11/22 6:50 PM
 * @description 验证码controller
 */

@RestController
@RequestMapping("/captcha")
@Slf4j
public class CaptchaController {

    private LineCaptcha lineCaptcha;

    @RequestMapping("/toLogin")
    public String toLogin(String username, String password) {
        log.info("toLogin username:{}, password:{}", username, password);
        return "login";
    }

    /**
     * 登录逻辑实现
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String imgText = request.getParameter("imgText");
        log.info("username:{}", username);
        log.info("password:{}", password);
        log.info("登录验证码:{}", lineCaptcha.getCode());
        if (("user").equals(username) && ("123").equals(password) && imgText.equals(lineCaptcha.getCode())) {
            return "redirect:hello";
        } else {
            return "redirect:toLogin";
        }
    }

    /**
     * 生成验证码
     * @param response
     */
    @RequestMapping("/getCode")
    public void getCode(HttpServletResponse response) {
        // 随机生成 4 位验证码
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        // 定义图片的显示大小
        lineCaptcha = CaptchaUtil.createLineCaptcha(100, 30);
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        try {
            // 调用父类的 setGenerator() 方法，设置验证码的类型
            lineCaptcha.setGenerator(randomGenerator);
            // 输出到页面
            lineCaptcha.write(response.getOutputStream());
            // 打印日志
            log.info("生成的验证码:{}", lineCaptcha.getCode());
            // 关闭流
            response.getOutputStream().close();
        } catch (IOException e) {
            log.error("生成的验证码 error:", e);
        }
    }
}
