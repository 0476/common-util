package com.ailikes.util;
import java.util.Set;  
  
import javax.validation.ConstraintViolation;  
import javax.validation.Validation;  
//import javax.validation.ValidationException;  
import javax.validation.Validator;  
import javax.validation.ValidatorFactory;  
import javax.xml.bind.ValidationException;  
  
/**
 * 
 * 功能描述: 校验工具类  
 * 
 * @version 1.0.0
 * @author 徐大伟
 */
public class ValidationUtil {  
  
    private static Validator validator;  
      
    static {  
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();  
        validator = vf.getValidator();  
    }  
  
    /**
     * 
     * 功能描述: 校验方法 
     * 
     * @param t 将要校验的对象 
     * @throws ValidationException void
     * @version 1.0.0
     * @author 徐大伟
     */
    public static <T> void validate(T t) throws ValidationException{  
        Set<ConstraintViolation<T>> set =  validator.validate(t);  
        if(set.size()>0){  
            StringBuilder validateError = new StringBuilder();  
            for(ConstraintViolation<T> val : set){  
                validateError.append(val.getMessage() + " ;<br>");  
            }  
            throw new ValidationException(validateError.toString());              
        }  
    }  
      
}  