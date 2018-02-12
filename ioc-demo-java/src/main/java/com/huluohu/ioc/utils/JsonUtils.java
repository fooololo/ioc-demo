package com.huluohu.ioc.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> T readValue(InputStream is, Class<T> cls){
        try{
            return mapper.readValue(is,cls);
        }catch (Exception e){
            return null;
        }
    }

    public static <T> T readValue(InputStream is,TypeReference valueTypeRef){
        try{
            return mapper.readValue(is,valueTypeRef);
        }catch (Exception e){
            return null;
        }
    }
}
