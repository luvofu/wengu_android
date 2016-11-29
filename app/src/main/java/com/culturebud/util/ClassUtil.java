package com.culturebud.util;

import com.culturebud.annotation.PresenterInject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtil {
    public static <T> T getClassInstance(Object obj, int i) {
        if (obj == null) {
            return null;
        }
        try {
            Type type = obj.getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                if (types != null && types.length > i) {
                    return ((Class<T>) types[i]).newInstance();
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <P> P getPresenter(Object obj) {
        PresenterInject pi = obj.getClass().getAnnotation(PresenterInject.class);
        if (pi == null) {
            return null;
        }
        try {
            return (P) pi.value().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
