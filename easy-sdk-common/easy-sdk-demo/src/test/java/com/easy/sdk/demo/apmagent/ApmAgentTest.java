package com.easy.sdk.demo.apmagent;

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
 * @description 采用 byte-buddy框架 利用 javaagent 技术实现全链路追踪测试类
 */

@SpringBootTest(classes = EasySdkDemoApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ApmAgentTest {

    /**
     * add vm option: -javaagent:/Users/dearzhang/info/git-work/java-work/easy-sdk/easy-sdk-common/easy-sdk-common-core/target/easy-sdk-common-core-2.0.0.jar
     */
    @Test
    public void apmAgentTest() {
        String url = "http://localhost:8080/demo/demo";
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
     * 2023-12-07 15:00:07.848 [650365efce764a0ebb99a169d027b6a5]  INFO 40754 --- c.e.s.c.apmagent.advice.OkHttpAdvice : 监控数据:Monitor(id=null, url=http://localhost:8080/captcha/demo?msg=zhangsan, traceId=650365efce764a0ebb99a169d027b6a5, segmentId=26f23c12cbd242fe8059aaedb966d127-main, inParam=msg=zhangsan, response={"redirect":false,"successful":true}, responseTime=2s:528ms, parentId=1300734f875144a889846b18cd9b184a, startDate=Thu Dec 07 15:00:05 CST 2023, endDate=Thu Dec 07 15:00:07 CST 2023)
     * 2023-12-07 15:00:07.852 [650365efce764a0ebb99a169d027b6a5]  INFO 40754 --- c.e.sdk.demo.apmagent.ApmAgentTest : zhangsan
     *
     *
     * 2023-12-07 15:00:07.639 [650365efce764a0ebb99a169d027b6a5]  INFO 40032 --- c.e.s.d.c.c.CaptchaController : demo msg:zhangsan
     * 2023-12-07 15:00:07.640 [650365efce764a0ebb99a169d027b6a5]  INFO 40032 --- c.e.s.d.c.service.DemoServiceImpl : demo service :zhangsan
     * 2023-12-07 15:00:07.640 [650365efce764a0ebb99a169d027b6a5]  INFO 40032 --- c.e.s.c.a.advice.ControllerAdvice : 监控数据:Monitor(id=null, url=/captcha/demo, traceId=650365efce764a0ebb99a169d027b6a5, segmentId=472bd9a646974fcb9418c266787718c1-http-nio-8080-exec-3, inParam=[["zhangsan"]], response="zhangsan", responseTime=1ms, parentId=1300734f875144a889846b18cd9b184a, startDate=Thu Dec 07 15:00:07 CST 2023, endDate=Thu Dec 07 15:00:07 CST 2023)
     */
}
