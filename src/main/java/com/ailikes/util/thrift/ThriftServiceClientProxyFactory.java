package com.ailikes.util.thrift;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 动态代理，集群及健康检查，故障节点漂移
 */
public class ThriftServiceClientProxyFactory implements FactoryBean, InitializingBean {
    private static Logger                   logger   = LoggerFactory.getLogger(ThriftServiceClientProxy.class);
    private Class                           objectClass;                                                       // 客户端接口
    private Object                          proxyClient;                                                       // 客户端代理类
    private String                          serviceInterface;                                                  // 服务的接口
    private String                          serviceName;                                                       // 服务的名字
    private String                          host;                                                              // host1:port，例如127.0.0.1:9999
    private int                             timeout  = 3000;
    private List<ThriftServiceClientProxy>  clients  = new CopyOnWriteArrayList<ThriftServiceClientProxy>();   // 存放全部protocol
    private List<ThriftServiceClientProxy>  alive    = new CopyOnWriteArrayList<ThriftServiceClientProxy>();   // 存放存活protocol
    private List<ThriftServiceClientProxy>  death    = new CopyOnWriteArrayList<ThriftServiceClientProxy>();   // 存放死去protocol
    private AtomicInteger                   count    = new AtomicInteger(0);                                   // 计数器
    private static ScheduledExecutorService executor;                                                          // 健康检查定时器
    private int                             interval = 60000;                                                  // 健康检查周期，单位毫秒

    static {
        executor = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化
        String[] hosts = host.split(",");
        for (String address : hosts) {
            String[] tmp2 = address.split(":");
            String ip = tmp2[0];
            int port = Integer.parseInt(tmp2[1]);
            serviceName = serviceInterface.substring(serviceInterface.lastIndexOf(".")+1);
            ThriftServiceClientProxy client = new ThriftServiceClientProxy(ip, port, timeout, serviceInterface, serviceName);
            try {
                client.init();
                alive.add(client);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                death.add(client);
            }
        }
        clients.addAll(alive);
        clients.addAll(death);

        // 健康检查
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                check();
            }
        }, 0, interval, TimeUnit.MILLISECONDS);

        // 动态代理
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 加载Iface接口
        objectClass = classLoader.loadClass(serviceInterface + "$Iface");
        // 加载Client.Factory类
        proxyClient = Proxy.newProxyInstance(classLoader, new Class[] { objectClass }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy,
                                 Method method,
                                 Object[] args) throws Throwable {
                while (alive.size() > 0) {
                    // 负载均衡getThriftServiceClientProxy(),每次调用返回的client不同
                    ThriftServiceClientProxy thriftProxy = getThriftServiceClientProxy();
                    try {
                        Object o = method.invoke(thriftProxy.getClient(), args);
                        return o;
                    } catch (InvocationTargetException e1) {
                        Throwable cause = (Throwable) e1.getCause();
                        if (cause instanceof TApplicationException) {
                            //包装的应用异常
                            throw cause;
                        } else if (cause instanceof TTransportException) {
                            if (null != cause) {
                                Throwable causeRoot = (Throwable) cause.getCause();
                                if (causeRoot == null) {
                                    //运行时异常上抛，应用自行解决
                                    throw cause;
                                } else if (causeRoot instanceof SocketTimeoutException) {
                                    //访问超时上抛，应用自行解决
                                    throw cause;
                                }
                            }
                        }

                        if (thriftProxy != null) {
                            synchronized (thriftProxy) {
                                alive.remove(thriftProxy);
                                death.add(thriftProxy);
                            }
                        }
                    } catch (Throwable e) {
                        throw e;
                    }
                }
                throw new Exception("can't invode thrift server cluster.");
            }
        });
    }

    /**
     * 检查客户端存活状态
     * 
     * @author piaohailin
     * @date 2014-5-9
     */
    private void check() {
        if (clients != null && clients.size() > 0) {
            for (ThriftServiceClientProxy item : clients) {
                boolean checkResult = item.check();
                if (checkResult) {
                    // 服务活着
                    death.remove(item);
                    alive.add(item);
                } else {
                    // 服务挂了
                    alive.remove(item);
                    death.add(item);
                }

            }
        }
    }

    /**
     * 轮询获取
     * 
     * @return
     * @author piaohailin
     * @date 2014-5-9
     */
    public ThriftServiceClientProxy getThriftServiceClientProxy() {
        ThriftServiceClientProxy result = null;
        while (alive.size() > 0) {
            try {
                int index = count.getAndIncrement() % alive.size();
                result = alive.get(index);
                break;
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
        return result;
    }

    @Override
    public Object getObject() throws Exception {
        return proxyClient;
    }

    @Override
    public Class getObjectType() {
        return objectClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
