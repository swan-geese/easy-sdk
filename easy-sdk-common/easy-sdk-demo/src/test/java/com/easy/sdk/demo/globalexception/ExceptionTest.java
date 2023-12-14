package com.easy.sdk.demo.globalexception;

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
 * @description 验证全局异常处理
 * 启动类需新增 @EnableGlobalException 注解
 */

@SpringBootTest(classes = EasySdkDemoApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ExceptionTest {

    /**
     * 验证全局异常处理
     */
    @Test
    public void exceptionTest() {
        String url = "http://localhost:8080/demo/exception";
        String result = null;
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("msg", "zhangsan");
            result = OkHttpUtils.sendGet(url, paramMap, new HashMap<>());
        } catch (IOException e) {
            log.error("sendGet error:{}", e.getMessage());
        }
        log.info(result);
    }

    /**
     *  启动类新增 @EnableGlobalException 注解时， 返回404页面
     *  启动类未新增该注解时，返回错误信息
     *  {
     *     "timestamp": "2023-12-07T11:29:55.442+00:00",
     *     "status": 500,
     *     "error": "Internal Server Error",
     *     "message": "",
     *     "path": "/demo/exception"
     * }
     */
}
