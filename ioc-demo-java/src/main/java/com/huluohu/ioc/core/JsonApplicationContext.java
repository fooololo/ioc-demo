package com.huluohu.ioc.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.huluohu.ioc.bean.BeanDefinition;
import com.huluohu.ioc.utils.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.io.InputStream;
import java.util.List;

public class JsonApplicationContext extends BeanFactoryImpl {
    private String fileName;

    public JsonApplicationContext(String fileName) {
        this.fileName = fileName;
        loadFile();
    }

    private void loadFile() {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        List<BeanDefinition> beanDefinitions = JsonUtils.readValue(inputStream, new TypeReference<List<BeanDefinition>>() {
        });
        if (CollectionUtils.isNotEmpty(beanDefinitions)) {
            beanDefinitions.stream().forEach(beanDefinition -> {
                this.registerBean(beanDefinition.getName(), beanDefinition);
            });
        }
    }
}
