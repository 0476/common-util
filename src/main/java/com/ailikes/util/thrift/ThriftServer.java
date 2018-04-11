package com.ailikes.util.thrift;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

public class ThriftServer implements ApplicationContextAware {
    private Logger             logger   = LoggerFactory.getLogger(ThriftServer.class);

    private int                port;
    private int                selector = 16;
    private int                worker   = 1024;
    private TServer            server;
    private ApplicationContext applicationContext;
    private String             packageScan;

    /**
     * @author piaohailin
     * @throws TTransportException
     * @throws ClassNotFoundException 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * date 2014-4-9
     */
    public void start() throws TTransportException, NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        logger.info("start thrift server");
        TMultiplexedProcessor tprocessor = new TMultiplexedProcessor();

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Service.class);
        Set<Entry<String, Object>> entries = beans.entrySet();
        //遍历所有Service
        for (Entry<String, Object> entry : entries) {
        	/*
        	 * 1.不使用代理，需要使用clazz.getName()匹配packageScan
        	 * 2.使用JDK代理，需要使用entry.getValue().toString()匹配packageScan
        	 * 3.使用CGLIB代理，需要使用clazz.getName()匹配packageScan
        	 * 4.此处使用clazz.getName();
        	 */
            Class clazz = entry.getValue().getClass();//cglib代理后的路径:class com.ync365.sso.service.impl.SsoServiceImpl$$EnhancerBySpringCGLIB$$b8286ed0
            String className = entry.getValue().toString();//com.ync365.sso.service.impl.SsoServiceImpl@3c410946
            if (className.startsWith(packageScan)) {
                Class[] ints = clazz.getInterfaces();//取得这个类的所有接口
                if (ints != null) {
                    for (int i = 0, count = ints.length; i < count; i++) {
                        Class iFace = ints[i];
                        String iNameThrift = iFace.getName();//com.xxxx.thrift.service.BlogService$Iface
                        //如果是thrift接口
                        if (iNameThrift.endsWith("Iface")) {
                            String iName = iNameThrift.substring(0, iNameThrift.length() - 6);//com.xxxx.thrift.service.BlogService
                            String name = iName.substring(iName.lastIndexOf(".") + 1, iName.length()); //BlogService
                            //tprocessor.registerProcessor("BlogService", new BlogService.Processor<BlogService.Iface>(blogService));
                            //反射注册处理器
                            Constructor cons = Class.forName(iName + "$Processor").getConstructor(iFace);
                            Object[] arguments = new Object[] { applicationContext.getBean(iFace) };
                            tprocessor.registerProcessor(name, (TProcessor) cons.newInstance(arguments));
                            logger.info("{} is ready.", name);
                        }
                    }
                }
            }
        }

        TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(port);
        TThreadedSelectorServer.Args tsArg = new TThreadedSelectorServer.Args(tnbSocketTransport);
        tsArg.selectorThreads(selector);
        tsArg.workerThreads(worker);
        tsArg.processor(tprocessor);
        tsArg.transportFactory(new TFramedTransport.Factory());
        tsArg.protocolFactory(new TCompactProtocol.Factory());
        server = new TThreadedSelectorServer(tsArg);

        final TServer s = server;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                s.serve();
            }
        });

        t.start();
        logger.info("TThreadedSelectorServer selector={},worker={}", selector, worker);
        logger.info("thrift server is listening at port: {}", port);
    }

    public void stop() {
        server.stop();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSelector() {
        return selector;
    }

    public void setSelector(int selector) {
        this.selector = selector;
    }

    public int getWorker() {
        return worker;
    }

    public void setWorker(int worker) {
        this.worker = worker;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public String getPackageScan() {
        return packageScan;
    }

    public void setPackageScan(String packageScan) {
        this.packageScan = packageScan;
    }
}
