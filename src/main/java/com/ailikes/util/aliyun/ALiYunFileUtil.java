package com.ailikes.util.aliyun;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketInfo;

/**
 * 
 * 功能描述:阿里云OSS工具类 
 * 
 * @version 1.0.0
 * @author 徐大伟
 */
public class ALiYunFileUtil {
    private static Logger logger = LoggerFactory.getLogger(ALiYunFileUtil.class);
    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
    private  String endpoint = "";
    // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
    // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
    // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
    private  String accessKeyId = "";
    private  String accessKeySecret = "";
    // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
    private  String bucketName = "";


    
    public String saveFile(String filePath,String fileName,byte[] buf){
        fileName = getFileName(fileName);
    	OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    	 // 判断Bucket是否存在
        if (ossClient.doesBucketExist(bucketName)) {
            logger.info("您已经创建Bucket：" + bucketName + "。");
        } else {
            logger.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
            // 创建Bucket。
            ossClient.createBucket(bucketName);
        }
        // 查看Bucket信息。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
        BucketInfo info = ossClient.getBucketInfo(bucketName);
        logger.info("Bucket " + bucketName + "的信息如下：");
        logger.info("\t数据中心：" + info.getBucket().getLocation());
        logger.info("\t创建时间：" + info.getBucket().getCreationDate());
        logger.info("\t用户标志：" + info.getBucket().getOwner());

        // 把字符串存入OSS，Object的名称为firstKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
        InputStream is = new ByteArrayInputStream(buf);
        ossClient.putObject(bucketName, filePath+fileName, is);
        return (endpoint+"/"+filePath+fileName);
    }
    
    private String getFileName(String fileName){
        try {
            String suffix = fileName.substring(fileName.lastIndexOf(".")+1); 
            return System.currentTimeMillis()+"."+suffix;
        } catch (Exception e) {
            return System.currentTimeMillis()+".png";
        }
    }
    
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    } 
    
    
}
