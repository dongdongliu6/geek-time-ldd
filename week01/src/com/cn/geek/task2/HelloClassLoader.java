package com.cn.geek.task2;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * HelloClassLoader 加载Hello.xlass二进制文件，按照x=255-x规则解密，加载类，执行hello方法
 * @Date 2020/10/20
 */
public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        HelloClassLoader helloClassLoader = new HelloClassLoader();
        String prePath = System.getProperty("user.dir");
        String filePath = prePath + "\\week01\\src\\com\\cn\\geek\\task2\\Hello.xlass";
        Class hello = helloClassLoader.loadClass(filePath);
        Method[] methods = hello.getDeclaredMethods();
        Object obj = hello.newInstance();

        for (Method method : methods) {
            String name = method.getName();
            System.out.println("name ===>" + name);
            method.invoke(obj, null);
        }
    }

    @Override
    protected Class<?> findClass(String filePath) {

        File file = new File(filePath);
        byte[] dataBytes = readFile(file);
        System.out.println("dataBytes.length===>" + dataBytes.length);
        return defineClass("Hello", dataBytes, 0, dataBytes.length);
    }

    public static byte[] readFile(File source) {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new ByteArrayOutputStream();
            int byteValue = 0;
            while ((byteValue = in.read()) != -1) {
                out.write(255 - byteValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }

}
