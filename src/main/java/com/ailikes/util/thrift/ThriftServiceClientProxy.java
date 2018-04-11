package com.ailikes.util.thrift;

import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射，通过字符串初始化客户端
 */
public class ThriftServiceClientProxy {
    private static Logger logger = LoggerFactory.getLogger(ThriftServiceClientProxy.class);
    private String        ip;
    private int           port;
    private int           timeout;                                                         //链接超时时间
    private TProtocol     protocol;
    private String        serviceInterface;                                                // 服务接口，用于反射
    private String        serviceName;                                                     // 服务名
    private Class         objectClass;                                                     // 客户端接口
    private Object        client;                                                          // 客户端实现

    public ThriftServiceClientProxy(String ip, int port, int timeout, String serviceInterface, String serviceName) {
        this.ip = ip;
        this.port = port;
        this.serviceInterface = serviceInterface;
        this.serviceName = serviceName;
        this.timeout = timeout;
        // 动态代理
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 加载Iface接口
        try {
            objectClass = classLoader.loadClass(serviceInterface + "$Iface");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 初始化方法
     * 
     * @author piaohailin
     * @date 2014-5-9
     */
    public void init() throws Exception {
        TSocket transport = new TSocket(ip, port);
        transport.setTimeout(timeout);
        this.protocol = new TCompactProtocol(new TFramedTransport(transport));
        try {
            transport.open();
        } catch (Exception e) {
            protocol = null;
            throw e;
        }

        // 原型
        //      TSocket transport = new TSocket("127.0.0.1", 9999);
        //      TProtocol protocol = new TCompactProtocol(new TFramedTransport(transport));
        //      WebsiteService.IFace websiteService = new WebsiteService.Client(new TMultiplexedProtocol(protocol, "WebsiteService"));
        // 反射,取得原型实例
        Class[] parameterTypes = new Class[] { TProtocol.class };
        Constructor constructor = Class.forName(serviceInterface + "$Client").getConstructor(parameterTypes);
        Object[] arguments = new Object[] { new TMultiplexedProtocol(protocol, serviceName) };
        client = constructor.newInstance(arguments);
    }

    /**
     * 是否打开
     * 
     * @return
     * @author piaohailin
     * @date 2014-5-9
     */
    public boolean isOpen() {
        boolean result = false;
        try {
            result = protocol.getTransport().isOpen();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 检查服务是否可用，如不不可用，则重新初始化
     * 超时时间为1秒钟
     * 
     * @return
     * @author piaohailin
     * @date 2014-5-9
     */
    public boolean check() {
        boolean flag = true;
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(ip, port), 1000);
            s.close();
            if (protocol == null) {
                init();
            }
        } catch (Throwable t) {
            try {
                protocol.getTransport().close();
                protocol = null;
            } finally {
                flag = false;
            }
        }
        return flag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public TProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(TProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * 取得service原型
     * 
     * @return
     * @author piaohailin
     * @date 2014-5-10
     */
    public Object getClient() {
        return client;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ThriftServiceClientProxy other = (ThriftServiceClientProxy) obj;
        if (ip == null) {
            if (other.ip != null)
                return false;
        } else if (!ip.equals(other.ip))
            return false;
        if (port != other.port)
            return false;
        return true;
    }

}
