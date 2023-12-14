package com.easy.sdk.common.apmagent.advice;

import com.alibaba.fastjson.JSON;
import com.easy.sdk.common.apmagent.context.ContextManager;
import com.easy.sdk.common.apmagent.entity.Monitor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import okhttp3.Request;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Description:
 * @Version 1.0
 */
@Slf4j
public class OkHttpAdvice {

    /**
     * 向RestTemplate发送http请求的header中添加traceId，segmentId
     *
     * @param args
     * @param callable
     * @throws Exception
     */
    @RuntimeType
    public Object intercept(@This Object target, @AllArguments Object[] args, @SuperCall Callable<?> callable) throws Exception {
        Monitor monitor = new Monitor();
        long startTime = System.currentTimeMillis();
        Date startDate = new Date(startTime);
        monitor.setStartDate(startDate);

        Field field = target.getClass().getDeclaredField("originalRequest");
        field.setAccessible(true);

        Object traceId = ContextManager.get(ContextManager.TRACE_ID);
        if (ObjectUtils.isEmpty(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        monitor.setTraceId(String.valueOf(traceId));

        Object segementId = ContextManager.get(ContextManager.SEGMENT_ID);
        if (ObjectUtils.isEmpty(segementId)) {
            segementId = UUID.randomUUID().toString().replace("-", "");
        }
        String name = Thread.currentThread().getName();
        String newSegmentId = UUID.randomUUID().toString().replace("-", "");
        monitor.setParentId(String.valueOf(segementId));
        monitor.setSegmentId(newSegmentId + "-" + name);
        ContextManager.put(ContextManager.TRACE_ID, traceId);
        ContextManager.put(ContextManager.SEGMENT_ID, monitor.getSegmentId());
        try {

            Request request = (Request) field.get(target);
            monitor.setUrl(request.url().toString());
            monitor.setInParam(request.url().query());

            Request newRequest = request.newBuilder()
                    .headers(request.headers())
                    .addHeader(ContextManager.TRACE_ID, String.valueOf(traceId))
                    .addHeader(ContextManager.SEGMENT_ID, String.valueOf(segementId))
                    .build();
            field.set(target, newRequest);
            Object invoke = null;
            String response = "";
            try {
                invoke = callable.call();
            } catch (Exception e) {
                log.error("OkHttpAdvice intercept error:{}", e.getMessage());
                response = "目标方法发生报错：" + e.getMessage();
            }
            if (invoke != null) {
                try {
                    response = JSON.toJSONString(invoke);
                } catch (Exception e) {
                    log.error("OkHttpAdvice response convert error:{}", e.getMessage());
                    response = "出参转换成json出错";
                }
            }
            long endTime = System.currentTimeMillis();
            Date endDate = new Date(endTime);
            monitor.setEndDate(endDate);
            monitor.setResponseTime(endTime - startTime);
            monitor.setResponse(response);
            return invoke;
        } finally {
            try {
                log.info("监控数据:{}", monitor.toString());
                ContextManager.remove();
            } catch (Exception e) {
                log.error("OkHttpAdvice ContextManager remove error:{}", e);
            }
        }
    }



}

