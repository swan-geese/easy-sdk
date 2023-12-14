package com.easy.sdk.common.apmagent.advice;

import com.easy.sdk.common.apmagent.context.ContextManager;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import java.lang.reflect.Field;

/**
 * @ClassName AsycThreadAdvice
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Description:
 * @Version 1.0
 **/
@Slf4j
public class AsyncConstructorAdvice {

    /**
     * 在主线程调用CallableWrapper、RunnableWrapper的构造函数时，将主线程的traceId，segmentId赋值给对象，
     * 以便子线程在调用run/call方法时，可从对象中获取主线程的traceId，segmentId。(就是将主线程的traceId，segmentId传给子线程)
     *
     * @param target
     * @param args
     * @throws Exception
     */
    @RuntimeType
    public void intercept(@This Object target, @AllArguments Object[] args) throws Exception {
        try {
            String traceId = (String) ContextManager.get(ContextManager.TRACE_ID);
            String segmentId = (String) ContextManager.get(ContextManager.SEGMENT_ID);
            Field staceField = target.getClass().getDeclaredField(ContextManager.TRACE_ID);
            staceField.setAccessible(true);
            staceField.set(target, traceId);
            Field segField = target.getClass().getDeclaredField(ContextManager.SEGMENT_ID);
            segField.setAccessible(true);
            segField.set(target, segmentId);
            ContextManager.put(ContextManager.TRACE_ID, traceId);
            ContextManager.put(ContextManager.SEGMENT_ID, segmentId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("异步构造方法报错：{}",e.getMessage());
        }

    }

}
