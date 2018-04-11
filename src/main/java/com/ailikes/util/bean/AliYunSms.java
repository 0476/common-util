package com.ailikes.util.bean;
/**
 * 
 * 功能描述: 阿里云短信
 * 
 * @version 1.0.0
 * @author 徐大伟
 */
public class AliYunSms {
    
    private String code;
    
    private String product = "i金牛座";
    
    private String item;

    public AliYunSms(){
        
    }
    
    public AliYunSms(String code){
        this.code = code;
    }
    public AliYunSms(String code,String product){
        this.code = code;
        this.product = product;
    }
    
    public AliYunSms(String code,String product,String item){
        this.code = code;
        this.product = product;
        this.item = item;
    }
    
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
    
    public String toString(){
        return "{\"code\":\""+code+"\",\"product\":\""+product+"\",\"item\":\""+item+"\"}";
    }
    
}
