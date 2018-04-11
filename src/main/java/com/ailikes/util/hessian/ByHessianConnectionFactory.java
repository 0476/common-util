package com.ailikes.util.hessian;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import com.ailikes.util.filter.LogFilter;
import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianURLConnection;
import com.caucho.hessian.client.HessianURLConnectionFactory;

/**
 * 
 * 功能描述: hessian客户端
 * @version 1.0.0
 * @author 徐大伟
 */
public class ByHessianConnectionFactory extends HessianURLConnectionFactory {

    @Override
    public HessianConnection open(URL url) throws IOException {
        HessianURLConnection hessianURLConnection = (HessianURLConnection) super.open(url);
        // 向报文头中添加参数
        String callId = LogFilter.getCallId();//如果不等于空，过滤器中已经放完了
        if (null == callId) {
            callId = UUID.randomUUID().toString().replace("-", "");
        }
        hessianURLConnection.addHeader("callId", callId);
        // logger.info(callId);
        return hessianURLConnection;
    }

}
