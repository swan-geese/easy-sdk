package com.easy.sdk.common.apmagent.callable;

/**
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Description: 通过byte-buddy修改所要拦截的方法的参数时会用到
 * @Version 1.0
 */
public interface MyCallable {
    Object call(Object[] args);
}
