package com.ailikes.util.hessian;

public interface ByHessianAuthorization {
    /**
     * 
     * 功能描述: hessian鉴权
     * @param user
     * @param password
     * @return boolean
     * @version 1.0.0
     * @author 徐大伟
     */
    public boolean isValid(String user,String password);
}
