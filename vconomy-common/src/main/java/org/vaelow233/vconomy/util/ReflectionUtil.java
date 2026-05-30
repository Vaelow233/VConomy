package org.vaelow233.vconomy.util;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class ReflectionUtil {
    public static URLClassLoader loadJar(Path... jarPath) throws MalformedURLException {
        URL[] urls = new URL[jarPath.length];
        for (int i = 0; i < jarPath.length; i++) {
            urls[i] = jarPath[i].toUri().toURL();
        }
        return new URLClassLoader(urls, ReflectionUtil.class.getClassLoader()) {
            @Override
            protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
                synchronized (getClassLoadingLock(name)) {
                    Class<?> c = findLoadedClass(name);
                    if (c == null) {
                        if (name.startsWith("java.")) {
                            c = super.loadClass(name, resolve);
                            return c;
                        }
                        try {
                            c = findClass(name);
                        } catch (ClassNotFoundException e) {
                            c = super.loadClass(name, resolve);
                            return c;
                        }
                    }
                    if (resolve) {
                        resolveClass(c);
                    }
                    return c;
                }
            }
        };
    }

    public static Object createNoArgInstance(URLClassLoader classLoader, String className) throws Exception {
        Class<?> clazz = classLoader.loadClass(className);
        return clazz.getConstructor().newInstance();
    }

    public static Object invokeMethod(Object object, String methodName, Object... args) throws Exception {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getMethods()) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            if (method.getParameterCount() != args.length) {
                continue;
            }
            int i = 0;
            boolean match = true;
            for (Class<?> paramType : method.getParameterTypes()) {
                Object param = args[i++];
                if (paramType.isPrimitive()) {
                    if (paramType == int.class && param instanceof Number) continue;
                    if (paramType == boolean.class && param instanceof Boolean) continue;
                    if (paramType == double.class && param instanceof Number) continue;
                    if (paramType == long.class && param instanceof Number) continue;
                    if (paramType == float.class && param instanceof Number) continue;
                    if (paramType == short.class && param instanceof Number) continue;
                    if (paramType == byte.class && param instanceof Number) continue;
                    if (paramType == char.class && param instanceof Character) continue;
                }
                if (!paramType.isInstance(param)) {
                   match = false;
                   break;
                }
            }
            if (!match) continue;
            return method.invoke(object, args);
        }
        throw new NoSuchMethodException(methodName);
    }
}
