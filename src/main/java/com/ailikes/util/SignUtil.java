package com.ailikes.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.ailikes.util.crypto.BASE64Decoder;
import com.ailikes.util.crypto.BASE64Encoder;


/**
 *类描述:签名工具
 *
 *@Author:李光伟
 *@date:2015-8-21
 *@Version:1.1.0
 */
public class SignUtil {
	
	 /**
     * @param source*待签名串儿* pemPath*私钥路径*
     * @return  
	 * @throws Exception 
     * @功能描述:签名 RSA+SHA1withRSA(签名)
     * @Author:李光伟
     * @date:2015-8-20  下午05:48:26
     * @Version:1.1.0
     */
    public static String encryptSign(String source,String pemPath) throws Exception{
    	PrivateKey pk = getPrivateKeyByPath(pemPath);
    	return sign(pk, source);
    }
    
    /**
     * @param source
     * @param keyCode 秘钥串儿
     * @return
     * @throws Exception  
     * @功能描述:根据秘钥串儿签名
     * @Author:李光伟
     * @date:2015-12-30  上午10:58:37
     * @Version:1.1.0
     */
    public static String encryptSign_(String source,String keyCode){
    	PrivateKey pk;
		try {
			pk = getPrivateKey(keyCode);
			return sign(pk, source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    /**
     * @param pk
     * @param source
     * @return
     * @throws Exception  
     * @功能描述:生成签名
     * @Author:李光伟
     * @date:2015-12-30  上午10:58:12
     * @Version:1.1.0
     */
    public static String sign(PrivateKey pk,String source) throws Exception{
    	byte[] sb = null;					
    	Signature sig = Signature.getInstance("SHA1withRSA");
    	sig.initSign(pk);
    	sig.update(source.getBytes());
    	sb = sig.sign();					
    	return (new BASE64Encoder()).encode(sb);
    }
    
    /**
     * @param source 签名前
     * @param merSign加密串
     * @param pemPath 公钥路径
     * @throws Exception 
     * @功能描述:验签
     * @Author:李光伟
     * @date:2015-8-21  下午02:49:33
     * @Version:1.1.0
     */
    public static Boolean checkSign(String source,String merSign,String pemPath){
    	try {
    		PublicKey publicKey = getPublicKeyByPath(pemPath);
			return check(source, merSign, publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }
    
    /**
     * @param source
     * @param merSign
     * @param keyCode 秘钥串儿
     * @throws Exception  
     * @功能描述:根据秘钥串儿验签
     * @Author:李光伟
     * @date:2015-12-30  上午11:01:40
     * @Version:1.1.0
     */
    public static Boolean checkSign_(String source,String merSign,String keyCode){
    	PublicKey publicKey;
		try {
			publicKey = getPublicKey(keyCode);
			return check(source, merSign, publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }
    
    /**
     * @param source
     * @param merSign
     * @param publicKey
     * @return
     * @throws Exception  
     * @功能描述:验证签名
     * @Author:李光伟
     * @date:2015-12-30  上午11:00:42
     * @Version:1.1.0
     */
    public static Boolean check(String source,String merSign,PublicKey publicKey){
		Signature verifyalg;
		try {
			verifyalg = Signature.getInstance("SHA1withRSA");
			verifyalg.initVerify(publicKey); 	
			verifyalg.update(source.getBytes());  			
			byte[] signbyte = (new BASE64Decoder()).decodeBuffer(merSign);
			boolean status = verifyalg.verify(signbyte);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}  		
		return false;
    }
    
    /**
     * @param source
     * @return 
     * @throws IOException  
     * @功能描述:base64 解密
     * @Author:李光伟
     * @date:2015-8-25  上午11:01:11
     * @Version:1.1.0
     */
    public static byte [] BASE64Decode(String source) throws IOException{
    	return new BASE64Decoder().decodeBuffer(source);
    }
    
    /**
     * @param pubPemPath
     * @return  
     * @功能描述:获取公钥
     * @Author:李光伟
     * @date:2015-9-22  下午04:02:42
     * @Version:1.1.0
     */
    public static PublicKey getPublicKeyByPath(String pubPemPath){
    	FileInputStream in = null;
		ByteArrayOutputStream byteOut = null;
    	try {  
        	File file = new File(pubPemPath);
        	if(file.exists())
        	{  
    	    	//获取密钥值
    	    	in = new FileInputStream(file);  
    	    	byteOut = new ByteArrayOutputStream();
    	    	int tempbyte;
    	    	while ((tempbyte = in.read()) != -1) {
    	    		byteOut.write(tempbyte);
    	    	}
               return getPublicKey(byteOut.toString()); 
        	}
        	
    	}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("获取公钥失败",e);
        }finally{
            try{
            	if(in!=null){
            		in.close();
            	}
            	if(byteOut!=null){
            		byteOut.close();
            	}
            }catch(Exception e){
            	e.printStackTrace();
            }
        }
        throw new RuntimeException("获取公钥失败");
    }
    
    /**
     * @param privatePemPath
     * @return  
     * @功能描述:获取私钥
     * @Author:李光伟
     * @date:2015-9-22  下午04:35:57
     * @Version:1.1.0
     */
    public  static PrivateKey getPrivateKeyByPath(String privatePemPath){
    	File file = new File(privatePemPath);
    	FileInputStream in = null;
		ByteArrayOutputStream byteOut = null;
		try{
			
    		//获取密钥值
    		in = new FileInputStream(file);  
    		byteOut = new ByteArrayOutputStream();
    		int tempbyte;
    		while ((tempbyte = in.read()) != -1) {
    			byteOut.write(tempbyte);
            }
    		return getPrivateKey(byteOut.toString());
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("获取私钥失败",e);
        }finally{
            try{
            	if(in!=null){
            		in.close();
            	}
            	if(byteOut!=null){
            		byteOut.close();
            	}
            }catch(Exception e){
            	e.printStackTrace();
            }
        }
    }
    
    public static PublicKey getPublicKey(String keyCode) throws Exception{
    	 byte[] keybyte = BASE64Decode(keyCode);  
         KeyFactory kf = KeyFactory.getInstance("RSA");  
         X509EncodedKeySpec  keySpec = new X509EncodedKeySpec(keybyte);  
         PublicKey publicKey = kf.generatePublic(keySpec); 
         return publicKey;
    }
    
    public  static PrivateKey getPrivateKey(String keyCode) throws Exception{
    	//配置私钥
		PrivateKey pk = null;
		PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(BASE64Decode(keyCode));
		KeyFactory kf = KeyFactory.getInstance("RSA");
		pk = kf.generatePrivate(peks);
		return pk;
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println( SignUtil.encryptSign("123456","D:\\WORKSPACE-JRPT\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\jrpt-web\\WEB-INF\\classes\\conf\\prv.pem"));
     }
}
