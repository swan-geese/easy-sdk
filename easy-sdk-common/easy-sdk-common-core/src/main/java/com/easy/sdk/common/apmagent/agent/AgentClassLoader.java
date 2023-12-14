package com.easy.sdk.common.apmagent.agent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @ClassName MyClassLoader
 * @Author swan-geese
 * @Date 2023/12/05 17:39
 * @Description:
 * @Version 1.0
 **/
public class AgentClassLoader extends ClassLoader{

    private static AgentClassLoader DEFAULT_LOADER;

    public static AgentClassLoader getDefault() {
        return DEFAULT_LOADER;
    }

    public static void initDefaultLoader() throws Exception {
        if (DEFAULT_LOADER == null) {
            synchronized (AgentClassLoader.class) {
                if (DEFAULT_LOADER == null) {
                    DEFAULT_LOADER = new AgentClassLoader(ApmAgent.class.getClassLoader());
                }
            }
        }
    }

    private String classLoaderName;

    private String path="/opt/";

    private String fileExtension = ".class";

    public AgentClassLoader() {
        super();
        this.classLoaderName = classLoaderName;
    }

    public AgentClassLoader(ClassLoader parent) {
        super(parent);
        this.classLoaderName = classLoaderName;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        System.out.println("findClass invoked : " + className);
        System.out.println("class loader name : " + this.classLoaderName);
        byte[] data = this.loadClassData(className);

        return this.defineClass(className, data, 0, data.length);
    }

    private byte[] loadClassData(String className) {
        byte[] data = null;
        className = className.replace(".", "/");
        try(InputStream is = new FileInputStream(new File(this.path + className + this.fileExtension));
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int ch;
            while(-1 != (ch = is.read())) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
