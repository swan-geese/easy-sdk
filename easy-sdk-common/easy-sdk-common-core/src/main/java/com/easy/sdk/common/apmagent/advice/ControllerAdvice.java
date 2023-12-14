package com.easy.sdk.common.apmagent.advice;

import com.alibaba.fastjson.JSON;
import com.easy.sdk.common.apmagent.context.ContextManager;
import com.easy.sdk.common.apmagent.entity.Monitor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @ClassName AdviceInterceptor
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Description:
 * @Version 1.0
 **/
@Slf4j
public class ControllerAdvice {

    /**
     * 监控springmvc的请求和返回
     *
     * @param args
     * @param
     * @param callable
     * @return
     * @throws Exception
     */
    @RuntimeType
    public Object intercept(@AllArguments Object[] args, @SuperCall Callable<?> callable) throws Exception {
        Monitor monitor = new Monitor();
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            RequestContextHolder.setRequestAttributes(requestAttributes, true);
            request.getContextPath();
            String requestURL = request.getServletPath();
            List<Object> list = new ArrayList<>(Arrays.asList(args));
            Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next instanceof ServletRequest
                        || next instanceof ServletResponse
                        || next instanceof HttpServletRequest
                        || next instanceof HttpServletResponse) {
                    iterator.remove();
                }
            }
            String param = "";
            try {
                param = JSON.toJSONString(list);
            } catch (Exception e) {
                log.error("ControllerAdvice param convert error:{}", e.getMessage());
                param = "入参转换成json出错";
            }
            String name = Thread.currentThread().getName();
            monitor.setUrl(requestURL);
            monitor.setInParam(param);

            String traceId = request.getHeader(ContextManager.TRACE_ID);
            if (ObjectUtils.isEmpty(traceId)) {
                traceId = UUID.randomUUID().toString().replace("-", "");
            }
            ContextManager.put(ContextManager.TRACE_ID, traceId);
            monitor.setTraceId(traceId);

            String parentId = request.getHeader(ContextManager.SEGMENT_ID);
            monitor.setParentId(parentId);

            String newSegmentId = UUID.randomUUID().toString().replace("-", "");
            monitor.setSegmentId(newSegmentId + "-" + name);
            ContextManager.put(ContextManager.SEGMENT_ID, monitor.getSegmentId());

            long startTime = System.currentTimeMillis();
            Date startDate = new Date(startTime);
            monitor.setStartDate(startDate);
            Object invoke = null;
            String response = "";

            try {
                invoke = callable.call();
            } catch (Exception e) {
                log.error("ControllerAdvice intercept error:{}", e.getMessage());
                response = "目标方法发生报错：" + e.getMessage();
            }
            if (invoke != null) {
                try {
                    response = JSON.toJSONString(invoke);
                } catch (Exception e) {
                    log.error("ControllerAdvice response convert error:{}", e.getMessage());
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
                log.error("ControllerAdvice ContextManager remove error:{}", e);
            }
        }
    }


}
