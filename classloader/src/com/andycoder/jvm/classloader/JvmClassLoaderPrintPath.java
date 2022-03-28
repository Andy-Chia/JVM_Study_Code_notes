package com.andycoder.jvm.classloader;

import sun.misc.Launcher;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class JvmClassLoaderPrintPath {
    public static void main(String[] args) {
        // 启动类加载器
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        System.out.println("启动类加载器");
        for (URL url : urLs) {
            System.out.println("===>" + url.toExternalForm());
        }
        printClassLoader("扩展加载器", JvmClassLoaderPrintPath.class.getClassLoader().getParent());
        printClassLoader("应用加载器", JvmClassLoaderPrintPath.class.getClassLoader());
    }

    public static void printClassLoader(String name, ClassLoader classLoader) {
        if (classLoader != null) {
            System.out.println(name + " classloader =>" + classLoader.toString());
            printUrlForClassLoader(classLoader);
        } else {
            System.out.println(name + " classLoader => null");
        }
    }

    public static void printUrlForClassLoader(ClassLoader classLoader) {
        Object ucp = insightField(classLoader, "ucp");
        Object path = insightField(ucp, "path");
        ArrayList ps = (ArrayList) path;
        for (Object p : ps) {
            System.out.println(" ==>" + p.toString());
        }
    }


    private static Object insightField(Object obj, String fName) {
        try {
            Field f = null;
            if (obj instanceof URLClassLoader) {
                f = URLClassLoader.class.getDeclaredField(fName);
            } else {
                f = obj.getClass().getDeclaredField(fName);
            }
            f.setAccessible(true);
            return f.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
