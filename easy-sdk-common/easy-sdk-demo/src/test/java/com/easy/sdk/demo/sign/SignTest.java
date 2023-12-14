package com.easy.sdk.demo.sign;

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
 * @date Created in 2023/11/22 6:13 PM
 * @description 接口参数签名测试类
 * 使用说明
 *   * 在启动类增加@EnableSign
 *   * 使用方式：
 *       - 配置yaml文件中的属性
 *       - 服务端：在method-> 参数前前添加注解 @Sign
 *       - 前端：在header中添加请求头(http请求)
 *       - Sign：参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串 {k}{v}{k}{v}{k}{v}...（包含：Timestamp{时间戳毫秒数}）
 *       - Timestamp：当前时间的毫秒数
 *       - 注：可用于简单值类型的参数或Bean中 简单值类型的属性以及Map接收的简单值参数
 *       - 范围：String等简单值类型  Map<String,Object> Bean(简单值 字段)
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class SignTest {

    /**
     * 验证 @Sign 在入参 String 类型上时，接口请求为 get 方式
     */
    @Test
    public void signGetTest() {
        String url = "http://localhost:8080/demo/checkSign";
        String result = null;
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("name", "zhangsan");
            paramMap.put("pwd", "123456");

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Timestamp", "1702363907149");
            headerMap.put("Sign","eec97eabae59e807dee38a3c14e8b2ce");
            result = OkHttpUtils.sendGet(url, paramMap, headerMap);
        } catch (IOException e) {
            log.error("sendPost error:{}", e.getMessage());
        }
        log.info(result);
    }

    /**
     * 2023-12-12 16:17:23.652 []  INFO 50466 --- com.easy.sdk.demo.sign.SignTest : {"retCode":"0000","retMsg":"ok","body":{"id":123456,"name":"zhangsan","age":33,"pwd":"123456"}}
     */


    /**
     * 验证 @Sign 在入参 Bean 类型上时，接口请求为 post 方式
     */
    @Test
    public void signPostTest() {
        String url = "http://localhost:8080/demo/checkSign";
        String result = null;
        try {
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("name", "zhangsan");
            bodyMap.put("pwd", "123456");

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Timestamp", "1702367654610");
            headerMap.put("Sign","45ec83172b0ef3f676e0218278f101cf");
            result = OkHttpUtils.sendPostWithJson(url, null, bodyMap, headerMap);
        } catch (IOException e) {
            log.error("sendPost error:{}", e.getMessage());
        }
        log.info(result);
    }

    /**
     * 2023-12-12 16:17:23.652 []  INFO 50466 --- com.easy.sdk.demo.sign.SignTest : {"retCode":"0000","retMsg":"ok","body":{"id":123456,"name":"zhangsan","age":33,"pwd":"123456"}}
     */

    /**
     * 验证 @Sign 在 method 方法上时，Bean 中定义需参与验签的参数 接口请求为 post 方式
     */
    @Test
    public void signPost2Test() {
        String url = "http://localhost:8080/demo/checkSign2";
        String result = null;
        try {
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("name", "zhangsan");
            bodyMap.put("pwd", "123456");

            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Timestamp", "1702367654610");
            headerMap.put("Sign","45ec83172b0ef3f676e0218278f101cf");
            result = OkHttpUtils.sendPostWithJson(url, null, bodyMap, headerMap);
        } catch (IOException e) {
            log.error("sendPost error:{}", e.getMessage());
        }
        log.info(result);
    }

    /**
     * 2023-12-12 16:17:23.652 []  INFO 50466 --- com.easy.sdk.demo.sign.SignTest : {"retCode":"0000","retMsg":"ok","body":{"id":123456,"name":"zhangsan","age":33,"pwd":"123456"}}
     */
}
