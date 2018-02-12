package com.huluohu.ioc.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static void inject(Field field, Object obj, Object value) throws IllegalAccessException {
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, value);
        }
    }
}
