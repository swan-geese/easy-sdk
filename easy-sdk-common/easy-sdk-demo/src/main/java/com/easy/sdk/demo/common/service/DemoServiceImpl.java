package com.easy.sdk.demo.common.service;

import com.easy.sdk.demo.common.entity.DemoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zy
 * @date Created in 2023/12/6 5:23 PM
 * @description
 */
@Service
@Slf4j
public class DemoServiceImpl implements DemoService {
    @Override
    public String demo(String msg) {
        log.info("demo service :{}", msg);
        return msg;
    }

    @Override
    public DemoEntity get(Long id) {
        if (id == 123456L) {
            return DemoEntity.builder().id(123456L).name("zhangsan").age(33).pwd("123456").build();
        }
        return null;
    }

    @Override
    public DemoEntity check(String name, String pwd) {
        if ("zhangsan".equals(name) && "123456".equals(pwd)) {
            return DemoEntity.builder().id(123456L).name("zhangsan").age(33).pwd("123456").build();
        }
        return new DemoEntity();
    }

    @Override
    public DemoEntity check(DemoEntity demoEntity) {
        if ("zhangsan".equals(demoEntity.getName()) && "123456".equals(demoEntity.getPwd())) {
            return DemoEntity.builder().id(123456L).name("zhangsan").age(33).pwd("123456").build();
        }
        return new DemoEntity();
    }
}
