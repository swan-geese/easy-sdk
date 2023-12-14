package com.easy.sdk.demo.emoji;

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
 * @description 表情过滤测试类
 * - 说明
 *   * 在启动类增加@EnableEmojiFilter
 *   * 方法method增加@EmojiFilter（Controller）
 *       - value： 是否可用，false，将不参与拦截。
 */

@SpringBootTest(classes = EasySdkDemoApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class EmojiTest {

    /**
     * 表情过滤测试类 (貌似 hutool 的 EmojiFilter 有点问题，部分 emoji 表情无法过滤，作为一个遗憾吧)
     */
    @Test
    public void emojiTest() {
        String url = "http://localhost:8080/demo/emoji";
        String result = null;
        try {
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("name", "😃");
            bodyMap.put("pwd", "😄");
            result = OkHttpUtils.sendPostWithJson(url, new HashMap<>(), bodyMap);
        } catch (IOException e) {
            log.error("sendPost error:{}", e.getMessage());
        }
        log.info(result);
    }

    /**
     * 2023-12-13 11:08:20.305 []  INFO 52147 --- com.easy.sdk.demo.emoji.EmojiTest : {"retCode":"0500","retMsg":"[非法字符:emoji:->😃😄]"}
     */
}
