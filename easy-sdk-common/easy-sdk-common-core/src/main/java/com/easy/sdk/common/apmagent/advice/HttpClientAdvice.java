package com.easy.sdk.common.apmagent.advice;

import com.easy.sdk.common.apmagent.callable.MyCallable;
import com.easy.sdk.common.apmagent.context.ContextManager;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.apache.http.HttpRequest;

/**
 * @ClassName HttpClientAdvice
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Description:
 * @Version 1.0
 **/
@Slf4j
public class HttpClientAdvice {

    @RuntimeType
    public Object intercept(@AllArguments Object[] args, @Morph MyCallable callable) throws Exception {
        try {
            for (Object arg : args) {
                if (arg instanceof HttpRequest) {
                    HttpRequest httpRequest = ((HttpRequest) arg);
                    Object traceId = ContextManager.get(ContextManager.TRACE_ID);
                    Object segmentId = ContextManager.get(ContextManager.SEGMENT_ID);
                    httpRequest.addHeader(ContextManager.TRACE_ID, traceId == null ? "" : traceId.toString());
                    httpRequest.addHeader(ContextManager.SEGMENT_ID, segmentId == null ? "" : segmentId.toString());
                }
            }
            return callable.call(args);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("apache的HttpClient发送http请求报错：{}", e.getMessage());
            return callable.call(args);
        }

    }

}
