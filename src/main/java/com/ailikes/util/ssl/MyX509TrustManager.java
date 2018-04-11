package com.ailikes.util.ssl;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
/**
 * 
 * 功能描述: 加载SSL证书
 * 
 * @version 1.0.0
 * @author 徐大伟
 */
public class MyX509TrustManager implements X509TrustManager {  
        
        X509TrustManager myJSSEX509TrustManager = null;  
        /**
         * @throws Exception
         */
        public MyX509TrustManager() throws Exception {  
            KeyStore ks = KeyStore.getInstance("PKCS12");  
            ks.load(MyX509TrustManager.class.getResourceAsStream("certificate/mykey.p12"), "jinniuzuo".toCharArray()); 
            // 这是加载自己的数字签名证书文件和密码，在这里这里没有，所以不需要  
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");  
            tmf.init(ks);  
            TrustManager tms[] = tmf.getTrustManagers();  
            for (int i = 0; i < tms.length; i++) {  
                if (tms[i] instanceof X509TrustManager) {  
                    myJSSEX509TrustManager = (X509TrustManager) tms[i];  
                    return;  
                }  
            }  
        }  
        /**
         * @param certificate 证书地址
         * @throws Exception
         */
        public MyX509TrustManager(String certificate,String password) throws Exception {  
            KeyStore ks = KeyStore.getInstance("PKCS12");  
            ks.load(MyX509TrustManager.class.getResourceAsStream(certificate), password.toCharArray()); 
            // 这是加载自己的数字签名证书文件和密码，在这里这里没有，所以不需要  
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");  
            tmf.init(ks);  
            TrustManager tms[] = tmf.getTrustManagers();  
            for (int i = 0; i < tms.length; i++) {  
                if (tms[i] instanceof X509TrustManager) {  
                    myJSSEX509TrustManager = (X509TrustManager) tms[i];  
                    return;  
                }  
            }  
        }  
  
        @Override  
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)  
                throws CertificateException {  
            // sunJSSEX509TrustManager.checkClientTrusted(arg0, arg1);  
        }  
  
        @Override  
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)  
                throws CertificateException {  
            // sunJSSEX509TrustManager.checkServerTrusted(arg0, arg1);  
        }  
  
        @Override  
        public X509Certificate[] getAcceptedIssuers() {  
            // X509Certificate[] acceptedIssuers = sunJSSEX509TrustManager.getAcceptedIssuers();  
            // return acceptedIssuers;  
            return null;  
        }  
    }  
