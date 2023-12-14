package com.easy.sdk.common.apmagent.advice;

import com.easy.sdk.common.apmagent.context.ContextManager;
import com.easy.sdk.common.apmagent.entity.Monitor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
public class JdbcTemplateAdvice {

    /**
     * 向RestTemplate发送http请求的header中添加traceId，segmentId
     *
     * @param args
     * @param callable
     * @throws Exception
     */
    @RuntimeType
    public Object intercept(@Advice.This Object target, @Origin Method targetMethod, @AllArguments Object[] args, @SuperCall Callable<?> callable) throws Exception {


        log.info("进入jdbc拦截方法------------------------");
        Monitor monitor = new Monitor();
        String sql = "";
        long startTime = System.currentTimeMillis();
        if ("doInStatement".equals(targetMethod.getName())) {
            Method getSqlMethod = target.getClass().getDeclaredMethod("getSql");
            getSqlMethod.setAccessible(true);
            sql = (String) getSqlMethod.invoke(target);
        } else {
            Method getSqlMethod = target.getClass().getDeclaredMethod("getSql", Object.class);
            getSqlMethod.setAccessible(true);
            sql = (String) getSqlMethod.invoke(target, args[0]);
            System.out.println("sql2:" + sql);
            ArgumentPreparedStatementSetter argsPrepared = (ArgumentPreparedStatementSetter) args[1];
            Field argsField = argsPrepared.getClass().getDeclaredField("args");
            argsField.setAccessible(true);
            Object[] objectArgs = (Object[]) argsField.get(argsPrepared);
            if (objectArgs != null) {
                for (int i = 0; i < objectArgs.length; i++) {
                    Object arg = objectArgs[i];
                    String replace = sql.replace("?", arg.toString());
                    sql = replace;
                }
            }
        }


        Object call = callable.call();
        long endTime = System.currentTimeMillis();
        monitor.setInParam(sql).setStartDate(new Date(startTime)).setEndDate(new Date(endTime))
                .setTraceId(String.valueOf(ContextManager.get(ContextManager.TRACE_ID)))
                .setSegmentId(UUID.randomUUID().toString().replace("-", "") + "-" + Thread.currentThread().getName())
                .setParentId(String.valueOf(ContextManager.get(ContextManager.SEGMENT_ID)))
                .setResponse(String.valueOf(call))
                .setResponseTime(endTime - startTime);
        log.info("sql监控数据：" + monitor.toString());
        return call;
    }
//    @RuntimeType
//    public Object intercept(@This Object target, @Origin Method targetMethod, @AllArguments Object[] args, @SuperCall Callable<?> callable) throws Exception {
//        Monitor monitor = new Monitor();
//        String sql = "";
//        long startTime = System.currentTimeMillis();
//
//        if ("doInStatement".equals(targetMethod.getName())) {
//            Method getSqlMethod = target.getClass().getDeclaredMethod("getSql");
//            getSqlMethod.setAccessible(true);
//            sql = (String) getSqlMethod.invoke(target);
//        } else {
//            Method getSqlMethod = target.getClass().getDeclaredMethod("getSql");
//            getSqlMethod.setAccessible(true);
//            sql = (String) getSqlMethod.invoke(target, args[0]);
//            ArgumentPreparedStatementSetter argsPrepared = (ArgumentPreparedStatementSetter) args[1];
//            Field argsField = argsPrepared.getClass().getDeclaredField("args");
//            argsField.setAccessible(true);
//            Object[] objectArgs = (Object[]) argsField.get(argsPrepared);
//            if (objectArgs != null) {
//                for (int i = 0; i < objectArgs.length; i++) {
//                    Object arg = objectArgs[i];
//                    String replace = sql.replace("?", arg.toString());
//                    sql = replace;
//                }
//            }
//        }
//        Object call = null;
//        try {
//            call = callable.call();
//        } catch (Exception e) {
//            e.printStackTrace();
//            monitor.setResponse("目标方法发生报错：" + e.getMessage());
//        }
//        ObjectMapper objectMapper = new ObjectMapper();
//        if (call != null) {
//            monitor.setResponse(objectMapper.writeValueAsString(call));
//        }
//        long endTime = System.currentTimeMillis();
//        monitor.setInParam(sql).setStartDate(new Date(startTime)).setEndDate(new Date(endTime))
//                .setTraceId(String.valueOf(ContextManager.get(ContextManager.TRACE_ID)))
//                .setSegmentId(UUID.randomUUID().toString().replace("-", "") + "-" + Thread.currentThread().getName())
//                .setParentId(String.valueOf(ContextManager.get(ContextManager.SEGMENT_ID)))
//                .setResponseTime(startTime - endTime);
//        log.info("sql监控数据：" + monitor.toString());
//        return call;
//    }

}

