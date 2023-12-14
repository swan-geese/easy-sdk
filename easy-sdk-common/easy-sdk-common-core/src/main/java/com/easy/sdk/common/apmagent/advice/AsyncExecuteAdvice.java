package com.easy.sdk.common.apmagent.advice;

import com.alibaba.fastjson.JSON;
import com.easy.sdk.common.apmagent.context.ContextManager;
import com.easy.sdk.common.apmagent.entity.Monitor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @ClassName AsycThreadAdvice
 * @Description
 * @Author tianxiaojun
 * @Date 2023-04-12 9:59
 **/
@Slf4j
public class AsyncExecuteAdvice {

    /**
     * 子线程调用call/run方法时，从对象中拿到主线程的traceId，segmentId,保证主线程和子线程的链路连接
     *
     * @param target
     * @param callable
     * @return
     * @throws Exception
     */
    @RuntimeType
    public Object intercept(@This Object target, @SuperCall Callable<?> callable) throws Exception {
        try {
            long startTime = System.currentTimeMillis();
            Field staceField = target.getClass().getDeclaredField(ContextManager.TRACE_ID);
            staceField.setAccessible(true);
            String traceId = staceField.get(target) == null ? "" : staceField.get(target).toString();
            Field segField = target.getClass().getDeclaredField(ContextManager.SEGMENT_ID);
            segField.setAccessible(true);
            String segmentId = segField.get(target) == null ? "" : segField.get(target).toString();
            ContextManager.put(ContextManager.TRACE_ID, traceId);
            ContextManager.put(ContextManager.SEGMENT_ID, segmentId);
            Monitor monitor = new Monitor();
            Object call = null;
            try {
                call = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
                monitor.setResponse("目标方法发生报错：" + e.getMessage());
            }
            if (call != null) {
                monitor.setResponse(JSON.toJSONString(call));
            }
            long endTime = System.currentTimeMillis();
            String newSegmentId = UUID.randomUUID().toString().replace("-", "");
            monitor.setParentId(segmentId)
                    .setStartDate(new Date(startTime))
                    .setEndDate(new Date(endTime))
                    .setTraceId(traceId)
                    .setSegmentId(newSegmentId + "-" + Thread.currentThread().getName())
                    .setResponseTime(endTime - startTime);
            log.info("异步线程监控数据:{}", monitor.toString());
            return call;
        } catch (Exception e) {
            log.error("异步线程run/call方法报错：{}", e.getMessage());
            e.printStackTrace();
        }
        return callable.call();
    }

}
