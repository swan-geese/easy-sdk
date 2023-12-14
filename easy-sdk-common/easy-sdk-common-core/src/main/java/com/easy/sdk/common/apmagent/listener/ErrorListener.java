package com.easy.sdk.common.apmagent.listener;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

/**
 * @ClassName ErrorListener
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Description:
 * @Version 1.0
 **/
public class ErrorListener implements  AgentBuilder.Listener{
    @Override
    public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

    }

    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {

    }

    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {

    }

    @Override
    public void onError(String className, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
        System.err.println("Error transforming class " + className);
        throwable.printStackTrace(System.err);
    }

    @Override
    public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

    }
}
