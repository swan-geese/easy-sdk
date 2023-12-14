package com.easy.sdk.common.apmagent.agent;

import com.easy.sdk.common.apmagent.advice.AsyncConstructorAdvice;
import com.easy.sdk.common.apmagent.advice.AsyncExecuteAdvice;
import com.easy.sdk.common.apmagent.advice.ControllerAdvice;
import com.easy.sdk.common.apmagent.advice.HttpClientAdvice;
import com.easy.sdk.common.apmagent.advice.JdbcTemplateAdvice;
import com.easy.sdk.common.apmagent.advice.OkHttpAdvice;
import com.easy.sdk.common.apmagent.advice.RestTemplateAdvice;
import com.easy.sdk.common.apmagent.callable.MyCallable;
import com.easy.sdk.common.apmagent.listener.ErrorListener;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatchers;
import java.lang.instrument.Instrumentation;

/**
 * @ClassName AgentDemo
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Description:
 * @Version 1.0
 * <p>
 * -javaagent:D:\workspace\apm-agment\target\apm-agent-0.0.1-SNAPSHOT.jar
 **/
public class ApmAgent {


    public static void premain(String agrs, Instrumentation inst) throws Exception {
        System.out.println("进入premain");
        System.out.println("isRedefineClassesSupported: " + inst.isRedefineClassesSupported());

        AgentClassLoader.initDefaultLoader();


        /**
         * 拦截springmvc的请求
         */
        new AgentBuilder.Default()
                .type(ElementMatchers.named("org.springframework.web.method.support.InvocableHandlerMethod"))
                .transform((builder, type, loader, module) -> builder
                        .method(ElementMatchers.named("doInvoke"))
                        .intercept(MethodDelegation.to(new ControllerAdvice())))
                .with(new ErrorListener())
                .installOn(inst);


        /**
         * 拦截RestTemplate的http请求，向header中添加traceId,segmentId
         */
        new AgentBuilder.Default()
                .type(ElementMatchers.nameContains("RestTemplate$HttpEntityRequestCallback"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("doWithRequest"))
                        .intercept(MethodDelegation.withDefaultConfiguration().withBinders(
                                Morph.Binder.install(MyCallable.class)
                                ).to(new RestTemplateAdvice())

                        ))
                .with(new ErrorListener())
                .installOn(inst);

        /**
         * 拦截自定义实现Runnable接口的类，其中的run方法
         */
        new AgentBuilder.Default()
                .type(ElementMatchers.named("com.easy.sdk.common.apmagent.trace.RunnableWrapper"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("run"))
                        .intercept(MethodDelegation.to(new AsyncExecuteAdvice())))
                .with(new ErrorListener())
                .installOn(inst);


        /**
         * 拦截自定义实现Runnable接口的类，其中的构造方法
         */
        new AgentBuilder.Default()
                .type(ElementMatchers.named("com.easy.sdk.common.apmagent.trace.RunnableWrapper"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .constructor(ElementMatchers.any())
                        .intercept(SuperMethodCall.INSTANCE
                                .andThen(MethodDelegation.to(new AsyncConstructorAdvice()))))
                .with(new ErrorListener())
                .installOn(inst);


        /**
         * 拦截自定义实现Callable接口的类，其中的call方法
         */
        new AgentBuilder.Default()
                .type(ElementMatchers.named("com.easy.sdk.common.apmagent.trace.CallableWrapper"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("call"))
                        .intercept(MethodDelegation.to(new AsyncExecuteAdvice())))
                .with(new ErrorListener())
                .installOn(inst);

        /**
         * 拦截自定义实现Callable接口的类，其中的构造方法
         */
        new AgentBuilder.Default()
                .type(ElementMatchers.named("com.easy.sdk.common.apmagent.trace.CallableWrapper"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .constructor(ElementMatchers.any())
                        .intercept(SuperMethodCall.INSTANCE
                                .andThen(MethodDelegation.to(new AsyncConstructorAdvice()))))
                .installOn(inst);

        /**
         *  拦截okhttp的http请求，向header中添加traceId,segmentId
         *  execute(同步http),enqueue(异步http)
         */
        new AgentBuilder.Default()
                .type(ElementMatchers.named("okhttp3.internal.connection.RealCall"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("execute").or(ElementMatchers.named("enqueue")))
                        .intercept(MethodDelegation.to(new OkHttpAdvice())
                        ))
                .with(new ErrorListener())
                .installOn(inst);

        /**
         *  拦截apache的httpClient请求，向header中添加traceId,segmentId
         */
        new AgentBuilder.Default()
                .type(ElementMatchers.hasSuperType(ElementMatchers.named("org.apache.http.client.HttpClient")))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("execute"))
                        .intercept(MethodDelegation.withDefaultConfiguration().withBinders(
                                Morph.Binder.install(MyCallable.class)
                                ).to(new HttpClientAdvice())
                        ))
//                .with(new ErrorListener())
                .installOn(inst);

        /**
         * 拦截jdbcTemplate
         *
         */

        new AgentBuilder.Default()
                .type(ElementMatchers.hasSuperType(ElementMatchers.named("org.springframework.jdbc.core.StatementCallback")))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("doInStatement"))
                        .intercept(MethodDelegation.to(new JdbcTemplateAdvice())
                        ))
//                .with(new ErrorListener())
                .installOn(inst);

//
        new AgentBuilder.Default()
                .type(ElementMatchers.named("org.springframework.jdbc.core.JdbcTemplate"))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.named("query")
                                .and(ElementMatchers.takesArgument(0, ElementMatchers.hasSuperType(ElementMatchers.named("org.springframework.jdbc.core.PreparedStatementCreator"))))
                                .and(ElementMatchers.takesArgument(1, ElementMatchers.hasSuperType(ElementMatchers.named("org.springframework.jdbc.core.PreparedStatementSetter"))))
                                .and(ElementMatchers.takesArgument(2, ElementMatchers.hasSuperType(ElementMatchers.named("org.springframework.jdbc.core.ResultSetExtractor")))))
                        .intercept(MethodDelegation.to(new JdbcTemplateAdvice())
                        ))
                .with(new ErrorListener())
                .installOn(inst);


    }


}



