package com.ailikes.util.aliyun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ailikes.util.bean.JacksonMapperUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;

/**
 * 
 * 功能描述:阿里云SMS工具类 
 * 
 * @version 1.0.0
 * @author 徐大伟
 */
public class ALiYunSmsUtil {
    private static Logger logger = LoggerFactory.getLogger(ALiYunSmsUtil.class);
    private  String accessKeyId = "";
    private  String accessKeySecret = "";
    private  String endpointName = "";
    private  String regionId = "";
    private  String product = ""; 
    private  String domain= "";
    private  String signName= "i金牛座";
    private  String appName= "i金牛座";
    
    //验证码发送需要对应相应的模板编码,内容由模板决定,不需要自己传递
    //身份验证验证码 SMS_25185024 验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！
    //登录确认验证码 SMS_25185022 验证码${code}，您正在登录${product}，若非本人操作，请勿泄露。
    //登录异常验证码 SMS_25185021 验证码${code}，您正尝试异地登录${product}，若非本人操作，请勿泄露。
    //用户注册验证码 SMS_25185020 验证码${code}，您正在注册成为${product}用户，感谢您的支持！
    //活动确认验证码 SMS_25185019 验证码${code}，您正在参加${product}的${item}活动，请确认系本人申请。
    //修改密码验证码 SMS_25185018 验证码${code}，您正在尝试修改${product}登录密码，请妥善保管账户信息。
    //信息变更验证码 SMS_25185017 验证码${code}，您正在尝试变更${product}重要信息，请妥善保管账户信息。
    public Boolean sendSms(String phone,String code,String item,String templateCode) throws ClientException{
//     IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIcnqJ2VvXgpjq", "KvgcZoWjzynsWf7yWKVtXbCixdL4UT");
//     DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",  "sms.aliyuncs.com");
       IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
       DefaultProfile.addEndpoint(endpointName, regionId, product, domain);
       IAcsClient client = new DefaultAcsClient(profile);
       SingleSendSmsRequest request = new SingleSendSmsRequest();
       try {
           request.setSignName(signName);
           request.setTemplateCode(templateCode);
           request.setParamString(new AliYunSms(code,appName).toString());
           request.setRecNum(phone);
           HttpResponse httpResponse = client.doAction(request, true, 3);
           logger.error(JacksonMapperUtil.beanToJson(httpResponse));
           if(200==httpResponse.getStatus()){
               return true;
           }else{
               return false;
           }
       } catch (ServerException e) {
           logger.error(e.getMessage(),e);
           return false;
       }catch (ClientException e) {
           logger.error(e.getMessage(),e);
           return false;
       }
    }
    
    /**
     * 
     * 功能描述: 发送注册短信验证码 
     *
     * @param phone
     * @param code
     * @return Boolean
     * date:   2018年4月11日 下午5:36:48
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public Boolean sendRegistSms(String phone,String code)  {
        try {
           return sendSms(phone,code,null,"SMS_25185020");
        } catch (Exception e) {
           logger.error(e.getMessage(),e);
           return false;
        }
    } 
    /**
     * 
     * 功能描述: 发送修改密码验证码
     *
     * @param phone
     * @param code
     * @return Boolean
     * date:   2018年4月11日 下午5:36:39
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public Boolean sendUpdatePasswordSms(String phone,String code)  {
        try {
            return sendSms(phone,code,null,"SMS_25185018");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return false;
        }
    } 
    /**
     * 
     * 功能描述: 绑银行卡验证码
     *
     * @param phone
     * @param code
     * @return Boolean
     * date:   2018年4月11日 下午5:36:30
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public Boolean sendBankCard(String phone,String code){
        try {
            return sendSms(phone,code,null,"SMS_25185024");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return false;
        }
    }
    
    public static void main(String[] args) {
        ALiYunSmsUtil aliyunsms = new ALiYunSmsUtil();
        aliyunsms.sendRegistSms("13520077078","123456");
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

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
