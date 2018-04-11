package com.ailikes.util.ssl;

import hprose.client.HproseHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.ssl.X509HostnameVerifier;

/**
 * 
 * 功能描述: hprose SSL代理类
 * 
 * @version 1.0.0
 * @author 徐大伟
 */
public class SSLHelperUtil {
    /**
     * 功能描述:SSL代理
     * @param client
     * @param certificate void
     * @version 1.0.0
     * @author 徐大伟
     */
    public static void SSLHelper(HproseHttpClient client) {  
        X509HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;  
        SSLContext sslContext = null;  
        try {  
            MyX509TrustManager mtm = new MyX509TrustManager();  
            TrustManager[] tms = new TrustManager[] { mtm };  
            // 初始化X509TrustManager中的SSLContext  
            sslContext = SSLContext.getInstance("TLS");  
            sslContext.init(null, tms, new java.security.SecureRandom());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        // 为javax.net.ssl.HttpsURLConnection设置默认的SocketFactory和HostnameVerifier  
        if (sslContext != null) {  
            // client.setDefaultSSLSocketFactory(sslContext  
            // .getSocketFactory());  
            client.setSSLSocketFactory(sslContext.getSocketFactory());  
        }  
        // client.setDefaultHostnameVerifier(hostnameVerifier);  
        client.setHostnameVerifier(hostnameVerifier);  
    }  
    
    /**
     * 功能描述:SSL代理， 自定义证书路径
     * @param client
     * @param certificate void
     * @version 1.0.0
     * @author 徐大伟
     */
    public static void SSLHelper(HproseHttpClient client,String certificate,String password) {  
        X509HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;  
        SSLContext sslContext = null;  
        try {  
            MyX509TrustManager mtm = new MyX509TrustManager(certificate,password);  
            TrustManager[] tms = new TrustManager[] { mtm };  
            // 初始化X509TrustManager中的SSLContext  
            sslContext = SSLContext.getInstance("TLS");  
            sslContext.init(null, tms, new java.security.SecureRandom());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        // 为javax.net.ssl.HttpsURLConnection设置默认的SocketFactory和HostnameVerifier  
        if (sslContext != null) {  
            // client.setDefaultSSLSocketFactory(sslContext  
            // .getSocketFactory());  
            client.setSSLSocketFactory(sslContext.getSocketFactory());  
        }  
        // client.setDefaultHostnameVerifier(hostnameVerifier);  
        client.setHostnameVerifier(hostnameVerifier);  
    }  
}
