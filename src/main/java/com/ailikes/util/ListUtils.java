package com.ailikes.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ListUtils {  
  
    /**
     * 
     * 功能描述: 分组依据接口
     * 
     * date:   2018年4月11日 下午5:06:37
     * @author: ailikes
     * @version: 1.0.0
     * @since: 1.0.0
     */
    public interface GroupBy<T> {  
        T groupby(Object obj);  
    }  
  
    /**
     * 
     * 功能描述: 分组
     *
     * @param colls
     * @param gb
     * @return Map
     * date:   2018年4月11日 下午5:07:06
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static final <T extends Comparable<T>, D> Map<T, List<D>> group(Collection<D> colls, GroupBy<T> gb) {  
        if (colls == null || colls.isEmpty()) {  
            return null;  
        }  
        if (gb == null) {  
            return null;  
        }  
        Iterator<D> iter = colls.iterator();  
        Map<T, List<D>> map = new HashMap<T, List<D>>();  
        while (iter.hasNext()) {  
            D d = iter.next();  
            T t = gb.groupby(d);  
            if (map.containsKey(t)) {  
                map.get(t).add(d);  
            } else {  
                List<D> list = new ArrayList<D>();  
                list.add(d);  
                map.put(t, list);  
            }  
        }  
        return map;  
    }  
    /**
     * 
     * 功能描述: 将List按照V的methodName方法返回值（返回值必须为K类型）分组，合入到Map中
     *
     * @param list 待分组的列表 
     * @param map  存放分组后的map 
     * @param clazz  泛型V的类型 
     * @param methodName  方法名 
     * date:   2018年4月11日 下午5:07:27
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static <K, V> void listGroup2Map(List<V> list, Map<K, List<V>> map, Class<V> clazz, String methodName) {  
        // 入参非法行校验  
        if (null == list || null == map || null == clazz || null == methodName) {  
            System.out.print("CommonUtils.listGroup2Map 入参错误，list：" + list + " ；map：" + map + " ；clazz：" + clazz + " ；methodName：" + methodName);  
            return;  
        }  
  
        // 获取方法  
        Method method = getMethodByName(clazz, methodName);  
        // 非空判断  
        if (null == method) {  
            return;  
        }  
  
        // 正式分组  
        listGroup2Map(list, map, method);  
    }  
    /**
     * 
     * 功能描述: 根据类和方法名，获取方法对象 
     *
     * @param clazz
     * @param methodName
     * @return Method
     * date:   2018年4月11日 下午5:07:57
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static Method getMethodByName(Class<?> clazz, String methodName) {  
        Method method = null;  
        // 入参不能为空  
        if (null == clazz || null == methodName) {  
            System.out.print("CommonUtils.getMethodByName 入参错误，clazz：" + clazz + " ；methodName：" + methodName);  
            return method;  
        }  
  
        try {  
            method = clazz.getDeclaredMethod(methodName);  
        } catch (Exception e) {  
            System.out.print("类获取方法失败！");  
        }  
  
        return method;  
    }  
    /**
     * 
     * 功能描述: 将List按照V的某个方法返回值（返回值必须为K类型）分组，合入到Map中 
     *
     * @param list
     * @param map
     * @param method void
     * date:   2018年4月11日 下午5:08:05
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    @SuppressWarnings("unchecked")  
    public static <K, V> void listGroup2Map(List<V> list, Map<K, List<V>> map, Method method) {  
        // 入参非法行校验  
        if (null == list || null == map || null == method) {  
            System.out.print("CommonUtils.listGroup2Map 入参错误，list：" + list + " ；map：" + map + " ；method：" + method);  
            return;  
        }  
  
        try {  
            // 开始分组  
            Object key;  
            List<V> listTmp;  
            for (V val : list) {  
                key = method.invoke(val);  
                listTmp = map.get(key);  
                if (null == listTmp) {  
                    listTmp = new ArrayList<V>();  
                    map.put((K) key, listTmp);  
                }  
                listTmp.add(val);  
            }  
        } catch (Exception e) {  
            System.out.print("分组失败！");  
        }  
    }  
  
}