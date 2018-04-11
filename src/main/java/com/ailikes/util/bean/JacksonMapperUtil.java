package com.ailikes.util.bean;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 功能描述: json工具类 功能详细描述：提供bean与json之间的转换和list与json之间的转换
 * 
 * @author 徐大伟
 * date 2014-12-28
 */
public class JacksonMapperUtil {
    /** 记录日志的变量 */
    private static final Logger      logger        = LoggerFactory.getLogger(JacksonMapperUtil.class);
    /** 静态ObjectMapper */
    private ObjectMapper             mapper;

    private static JacksonMapperUtil jacksonMapper = new JacksonMapperUtil();

    private static final int         ARRAY_MAX     = 1024;

    /**
     * 私有构造函数
     */
    private JacksonMapperUtil() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 获得ObjectMapper实例
     * 
     * @return ObjectMapper
     */
    public static ObjectMapper getInstance() {
        return jacksonMapper.mapper;
    }

    /**
     * JSON对象转换为JavaBean
     * 
     * @param json JSON对象
     * @param valueType Bean类
     * @return 泛型对象
     */
    public static <T> T jsonToBean(String json,
                                   Class<T> valueType) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            return getInstance().readValue(json, valueType);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * JavaBean转换为JSON字符串
     * 
     * @param bean JavaBean对象
     * @return json字符串
     */
    public static String beanToJson(Object bean) {
        StringWriter sw = new StringWriter();
        JsonGenerator gen = null;
        try {
            gen = new JsonFactory().createJsonGenerator(sw);
            getInstance().writeValue(gen, bean);
            gen.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return sw.toString();
    }

    /**
     * 
     * 功能描述: JSON转List
     *
     * @param json
     * @param clazz
     * @return List
     * date:   2018年4月11日 下午5:39:57
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(String json,
                                         Class<T> clazz) {
        T[] t = (T[]) Array.newInstance(clazz, ARRAY_MAX);
        try {
            t = (T[]) getInstance().readValue(json, t.getClass());
            return (List<T>) Arrays.asList(t);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    /**
     * 
     * 功能描述: bean转成map
     * @param javaBean
     * @return Map
     * @version 1.0.0
     * @author 徐大伟
     */
    public static Map beanToMap(Object javaBean) {
        Map result = new HashMap();
        Method[] methods = javaBean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {

                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(javaBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 
     * 功能描述: json转成map
     * @param jsonString
     * @return Map
     * @version 1.0.0
     * @author 徐大伟
     */
    public static Map jsonToMap(String jsonString)  {
        Map result = new HashMap();
        try {
        JSONObject jsonObject = new JSONObject(jsonString);
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

    }
    /**
     * 
     * 功能描述: 
     * 
     * @param t
     * @return
     */
    public static String listToJson(List<?> t) {
        try {
            return getInstance().writeValueAsString(t);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
