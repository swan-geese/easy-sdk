package com.easy.sdk.common.apmagent.advice;

import com.easy.sdk.common.apmagent.callable.MyCallable;
import com.easy.sdk.common.apmagent.context.ContextManager;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;

/**
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Description:
 * @Version 1.0
 */
@Slf4j
public class RestTemplateAdvice {


    /**
     * 向RestTemplate发送http请求的header中添加traceId，segmentId
     *
     * @param args
     * @param callable
     * @throws Exception
     */
    @RuntimeType
    public Object intercept(@AllArguments Object[] args, @Morph MyCallable callable) throws Exception {
        try {
            for (Object arg : args) {
                if (arg instanceof ClientHttpRequest) {
                    HttpHeaders httpHeaders = ((ClientHttpRequest) arg).getHeaders();
                    Object traceId = ContextManager.get(ContextManager.TRACE_ID);
                    Object segmentId = ContextManager.get(ContextManager.SEGMENT_ID);
                    httpHeaders.add(ContextManager.TRACE_ID, traceId == null ? "" : traceId.toString());
                    httpHeaders.add(ContextManager.SEGMENT_ID, segmentId == null ? "" : segmentId.toString());
                }
            }
            return callable.call(args);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("RestTemplate发送http请求报错：{}", e.getMessage());
        }
        return callable.call(args);
    }


}

