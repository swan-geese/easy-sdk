package com.easy.sdk.common.apmagent.context;

import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ContextManager
 * @Description 存储应用上下文中的traceId和segmentId
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Version 1.0
 **/
public class ContextManager {


    public static final String TRACE_ID = "traceId";

    public static final String SEGMENT_ID = "segmentId";

    private static final ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<>();


    public static Object get(Object key) {
        Map<Object, Object> objectMap = threadLocal.get();
        if (objectMap == null) {
            return null;
        }
        return objectMap.get(key);
    }


    public static void put(Object key, Object value) {
        //将当前线程的traceId存入MDC中，若需要在日志打印traceId,自行配置logback-spring.xml中配置：%X{traceId}
        MDC.put(String.valueOf(key), String.valueOf(value));
        Map<Object, Object> objectMap = threadLocal.get();
        if (objectMap == null) {
            objectMap = new HashMap<>();
            threadLocal.set(objectMap);
        }
        objectMap.put(key, value);
    }

    public static void remove() {
        threadLocal.remove();
    }


}
