package com.huluohu.ioc.core;

import com.huluohu.ioc.bean.BeanDefinition;
import com.huluohu.ioc.bean.ConstructorArg;
import com.huluohu.ioc.utils.BeanUtils;
import com.huluohu.ioc.utils.ClassUtils;
import com.huluohu.ioc.utils.ReflectionUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BeanFactoryImpl implements BeanFactory {
    private static final ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, BeanDefinition> beanDefineMap = new ConcurrentHashMap<>();

    private static final Set<String> beanNameSet = Collections.synchronizedSet(new HashSet<>());

    @Override
    public Object getBean(String name) throws Exception {
        Object bean = beanMap.get(name);
        if (bean != null) {
            return bean;
        }

        bean = createBean(beanDefineMap.get(name));
        if (bean != null) {
            populateBean(bean);
            beanMap.put(name, bean);
        }
        return bean;
    }


    protected void registerBean(String name, BeanDefinition bd) {
        beanDefineMap.put(name, bd);
        beanNameSet.add(name);
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        String className = beanDefinition.getClassName();
        Class clazz = ClassUtils.loadClass(className);
        if (clazz == null) {
            throw new Exception("can not find bean by beanName");
        }

        List<ConstructorArg> constructorArgs = beanDefinition.getConstructorArgs();
        if (CollectionUtils.isNotEmpty(constructorArgs)) {
            List<Object> objects = new ArrayList<>();
            for (ConstructorArg constructorArg : constructorArgs) {
                if (constructorArg.getValue() != null) {
                    objects.add(constructorArg.getValue());
                } else {
                    objects.add(getBean(constructorArg.getRef()));
                }
            }
            Class[] constructorArgTypes = objects.stream().map(Object::getClass).collect(Collectors.toList()).toArray(new Class[]{});
            Constructor constructor = clazz.getConstructor(constructorArgTypes);
            return BeanUtils.instance(clazz, constructor, objects.toArray());
        }
        return BeanUtils.instance(clazz, null, null);
    }

    private void populateBean(Object bean) throws Exception {
        Field[] declaredFields = bean.getClass().getSuperclass().getDeclaredFields();
        if (ArrayUtils.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                String beanName = StringUtils.uncapitalize(field.getName());
                if (beanNameSet.contains(beanName)) {
                    Object fieldBean = getBean(beanName);
                    if (fieldBean != null) {
                        ReflectionUtils.inject(field, bean, fieldBean);
                    }
                }
            }
        }
    }
}
