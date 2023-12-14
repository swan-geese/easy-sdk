package com.easy.sdk.demo.limit;

import com.easy.sdk.demo.EasySdkDemoApplication;
import com.easy.sdk.demo.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/12/06 14:13 PM
 * @description 接口访问控制/接口限流测试类
 * - 说明
 *   * 在启动类增加@EnableAccessLimit
 *   * 方法method增加@AccessLimit（Controller）
 *       - timeout： 拦截持续时间
 *       - count： 最大出错次数
 *       - enable： 是否可用，false，此注解将无效。
 */

@SpringBootTest(classes = EasySdkDemoApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class LimitTest {

    /**
     * 接口限流测试类
     */
    @Test
    public void limitTest() {
        for(int i = 0; i< 10; i++) {
            sendGet();
        }
    }

    private void sendGet() {
        String url = "http://localhost:8080/demo/limit";
        String result = null;
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("msg", "zhangsan");
            paramMap.put("pwd", "123456");
            result = OkHttpUtils.sendGet(url, paramMap, new HashMap<>());
        } catch (IOException e) {
            log.error("sendGet error:{}", e.getMessage());
        }
        log.info(result);
    }

    /**
     * 2023-12-12 19:34:33.884 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"body":{},"retCode":"0000","retMsg":"ok"}
     * 2023-12-12 19:34:33.888 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"body":{},"retCode":"0000","retMsg":"ok"}
     * 2023-12-12 19:34:33.893 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"retCode":"0500","retMsg":"HTTP请求超出设定的限制"}
     * 2023-12-12 19:34:33.902 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"retCode":"0500","retMsg":"HTTP请求超出设定的限制"}
     * 2023-12-12 19:34:33.908 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"retCode":"0500","retMsg":"HTTP请求超出设定的限制"}
     * 2023-12-12 19:34:33.915 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"retCode":"0500","retMsg":"HTTP请求超出设定的限制"}
     * 2023-12-12 19:34:33.919 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"retCode":"0500","retMsg":"HTTP请求超出设定的限制"}
     * 2023-12-12 19:34:33.923 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"retCode":"0500","retMsg":"HTTP请求超出设定的限制"}
     * 2023-12-12 19:34:33.928 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"retCode":"0500","retMsg":"HTTP请求超出设定的限制"}
     * 2023-12-12 19:34:33.932 []  INFO 22746 --- com.easy.sdk.demo.limit.LimitTest : {"retCode":"0500","retMsg":"HTTP请求超出设定的限制"}
     *
     */
}
