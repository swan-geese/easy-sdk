package com.easy.sdk.common.extra.emoji.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.easy.sdk.common.extra.emoji.proxy.EmojiFilterProxy;

@Configuration
public class EmojiFilterConfig {

    @Bean
    public EmojiFilterProxy emojiFilterProxy() {
        return new EmojiFilterProxy();
    }

}
