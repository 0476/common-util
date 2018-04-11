package com.ailikes.util.hessian;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.remoting.caucho.HessianClientInterceptor;

import com.caucho.hessian.client.HessianProxyFactory;
/**
 * 
 * 功能描述:hessian客户端 
 * 
 * @version 1.0.0
 * @author 徐大伟
 */
public class ByHessianProxyFactoryBean extends HessianClientInterceptor implements FactoryBean<Object> {

    private HessianProxyFactory proxyFactory   = new HessianProxyFactory();

    private int                 connectTimeOut = 10000;

    private int                 readTimeOut    = 10000;

    private String              user;
    
    private String              password;

    private Object              serviceProxy;

    @Override
    public void afterPropertiesSet() {
        proxyFactory.setConnectTimeout(this.connectTimeOut);
        proxyFactory.setReadTimeout(this.readTimeOut);
        proxyFactory.setUser(this.user);
        proxyFactory.setPassword(this.password);
        ByHessianConnectionFactory hessianConnectionFactory = new ByHessianConnectionFactory();
        hessianConnectionFactory.setHessianProxyFactory(proxyFactory);
        proxyFactory.setConnectionFactory(hessianConnectionFactory);

        setProxyFactory(this.proxyFactory);
        super.afterPropertiesSet();
        this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
    }

    public Object getObject() {
        return this.serviceProxy;
    }

    public Class<?> getObjectType() {
        return getServiceInterface();
    }

    public boolean isSingleton() {
        return true;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
