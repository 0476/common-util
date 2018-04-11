package com.ailikes.util.mybatis;

import java.util.HashMap;

import com.ailikes.util.string.StringUtil;
/**
 * 
 * 功能描述:mybatis结果封装类 
 * 
 * @version 1.0.0
 * @author 徐大伟
 */
public class ResultMap<K, V> extends HashMap<K, V>{

    private static final long serialVersionUID = 1L;

    public ResultMap(){
        super();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public V put(K key, V value) {
        if(key instanceof String)
        {
            String keys = ((String) key);
            
            String reg = "^[A-Z]+$";
            if(!keys.matches(reg))
            {
                if(keys.contains("_"))
                {
                    String[] str = keys.split("_");
                    StringBuilder sb = new StringBuilder(str[0].toLowerCase());
                    
                    for(int i = 1; i < str.length; i++)
                    {
                        String s = str[i];
                        
                        sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase());
                    }
                    
                    keys = sb.toString();
                }else{
                    keys = keys.replaceFirst(keys.substring(0, 1), keys.substring(0, 1).toLowerCase());
                }
                if(keys.contains("idCard")){
                	if(StringUtil.isNotBlank((String)value)){
                		String value1 = StringUtil.getStarCenter((String)value, 4, 4);
                		 return super.put((K) keys, (V)value1);
                	}
                }
                if(keys.contains("userPhone") || keys.contains("userName")){
                	if(StringUtil.isNotBlank((String)value)){
                		String value1 = StringUtil.getStarCenter((String)value, 3, 4);
                		return super.put((K) keys, (V)value1);
                	}
                }
                if(keys.contains("cardNum")){
                	if(StringUtil.isNotBlank((String)value)){
                		String value1 = StringUtil.getStarCenter((String)value, 4, 4);
                		 return super.put((K) keys, (V)value1);
                	}
                }
                return super.put((K) keys, value);
            }
        }
        
        return null;
    }
}
