package com.huluohu.ioc.utils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

public class BeanUtils {
    public static <T> T instance(Class<T> clazz, Constructor constructor, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(NoOp.INSTANCE);

        if (constructor == null) {
            return (T) enhancer.create();
        }
        return (T) enhancer.create(constructor.getParameterTypes(), args);
    }
}
