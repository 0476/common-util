package com.ailikes.util.crypto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.ArrayUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.Signature;

/**
 * 
 * @功能描述: RAS加密类
 * 
 * @version: 1.0.0
 * @author: ailikes
 * @Date:   2018年4月11日 下午4:14:03
 */
public class RSAUtil {

    private static Cipher cipher;
    
    private static Signature sig;

    static {
        try {
            cipher = Cipher.getInstance("RSA");
            sig = Signature.getInstance("SHA1withRSA");
            
            // cipher = Cipher.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @功能描述: 生成密钥对
     * 
     * @param filePath 生成密钥的路径
     * @return Map<String,String>
     * @version 1.0.0
     * @author ailikes
     * @Date:   2018年4月11日 下午4:14:20
     */
    public static Map<String, String> generateKeyPair(String filePath) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 密钥位数
            keyPairGen.initialize(1024);
            // 密钥对
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 公钥
            PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
            PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥字符串
            String publicKeyString = getKeyString(publicKey);
            // 得到私钥字符串
            String privateKeyString = getKeyString(privateKey);
            // 将密钥对写入到文件
            FileWriter pubfw = new FileWriter(filePath + "/pub.pem");
            FileWriter prifw = new FileWriter(filePath + "/prv.pem");
            BufferedWriter pubbw = new BufferedWriter(pubfw);
            BufferedWriter pribw = new BufferedWriter(prifw);
            pubbw.write(publicKeyString);
            pribw.write(privateKeyString);
            pubbw.flush();
            pubbw.close();
            pubfw.close();
            pribw.flush();
            pribw.close();
            prifw.close();
            // 将生成的密钥对返回
            Map<String, String> map = new HashMap<String, String>();
            map.put("publicKey", publicKeyString);
            map.put("privateKey", privateKeyString);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @功能描述: 获取公钥
     * 
     * @param key 密钥字符串（经过base64编码）
     * @return
     * @throws Exception PublicKey
     * @version 1.0.0
     * @author ailikes
     * @Date:   2018年4月11日 下午4:14:32
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 
     * @功能描述: 获取私钥
     * 
     * @param key 密钥字符串（经过base64编码）
     * @return
     * @throws Exception PrivateKey
     * @version 1.0.0
     * @author ailikes
     * @Date:   2018年4月11日 下午4:14:58
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 
     * @功能描述: 得到密钥字符串（经过base64编码）
     * 
     * @param key
     * @return
     * @throws Exception String
     * @version 1.0.0
     * @author ailikes
     * @Date:   2018年4月11日 下午4:15:14
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = (new BASE64Encoder()).encode(keyBytes);
        return s;
    }

    /**
     * 
     * @功能描述: 使用公钥对明文进行加密，返回BASE64编码的字符串
     * 
     * @param privateKey
     * @param plainText
     * @return String
     * @version 1.0.0
     * @author ailikes
     * @Date:   2018年4月11日 下午4:15:26
     */
    public static String encrypt(PrivateKey privateKey,
                                 String plainText) {
        try {
            sig.initSign(privateKey);
            sig.update(plainText.getBytes());
            byte[] sb = null;       
            sb = sig.sign();                    
            return new BASE64Encoder().encode(sb);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @功能描述: 使用keystore对明文进行加密
     * 
     * @param publicKeystore 公钥文件路径
     * @param plainText 明文
     * @return String
     * @version 1.0.0
     * @author ailikes
     * @Date:   2018年4月11日 下午4:15:44
     */
    public static String encrypt(String publicKeystore,
                                 String plainText) {
        try {
            FileReader fr = new FileReader(publicKeystore);
            BufferedReader br = new BufferedReader(fr);
            String publicKeyString = "";
            String str;
            while ((str = br.readLine()) != null) {
                publicKeyString += str;
            }
            br.close();
            fr.close();
            sig.initSign(getPrivateKey(publicKeystore));
            sig.update(plainText.getBytes());
            byte[] sb = null;       
            sb = sig.sign();                    
            return new BASE64Encoder().encode(sb);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @功能描述: 使用私钥对明文密文进行解密
     * 
     * @param privateKey
     * @param enStr
     * @return String
     * @version 1.0.0
     * @author ailikes
     * @Date:   2018年4月11日 下午4:16:04
     */
    public static String decrypt(PrivateKey privateKey,
                                 String enStr) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] data = (new BASE64Decoder()).decodeBuffer(enStr);
            String dataReturn = null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i += 128) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 128));
                sb.append(new String(doFinal));
            }
            dataReturn = sb.toString();
            // byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(dataReturn);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @功能描述: 使用keystore对密文进行解密
     * 
     * @param privateKeystore 私钥路径
     * @param enStr 密文
     * @return String
     * @version 1.0.0
     * @author ailikes
     * @Date:   2018年4月11日 下午4:16:13
     */
    public static String decrypt(String privateKeystore,
                                 String enStr) {
        try {
            FileReader fr = new FileReader(privateKeystore);
            BufferedReader br = new BufferedReader(fr);
            String privateKeyString = "";
            String str;
            while ((str = br.readLine()) != null) {
                privateKeyString += str;
            }
            br.close();
            fr.close();
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyString));
            byte[] data = (new BASE64Decoder()).decodeBuffer(enStr);
            String dataReturn = null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i += 128) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 128));
                sb.append(new String(doFinal));
            }
            dataReturn = sb.toString();

            // byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
            return new String(dataReturn);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
//    public static void main(String[] args) {
//       System.out.println( RSAUtil.encrypt("D:\\WORKSPACE-JRPT\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\jrpt-web\\WEB-INF\\classes\\conf\\prv.pem", "asdasd3456"));
//    }
}
