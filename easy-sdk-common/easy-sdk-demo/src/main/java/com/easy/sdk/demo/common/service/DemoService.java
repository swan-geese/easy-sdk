package com.easy.sdk.demo.common.service;

import com.easy.sdk.demo.common.entity.DemoEntity;

/**
 * @author zy
 * @date Created in 2023/12/6 5:22 PM
 * @description
 */
public interface DemoService {
    String demo(String msg);

    DemoEntity get(Long id);

    DemoEntity check(String name, String pwd);

    DemoEntity check(DemoEntity demoEntity);
}
