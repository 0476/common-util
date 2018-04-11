package com.ailikes.util.hessian;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.remoting.caucho.HessianServiceExporter;

/**
 * 
 * 功能描述: hessian服务器端
 * @version 1.0.0
 * @author 徐大伟
 */
public class ByHessianServiceExporter extends HessianServiceExporter {
    protected final Logger    logger    = LoggerFactory.getLogger(getClass());
    private static List<String> authorization = new ArrayList<String>();
    private static final Base64 base64  = new Base64();

    public void init() throws UnsupportedEncodingException {
        authorization.add("Basic " + new String(base64.encode("map:123".getBytes("utf-8")), "utf-8"));
        authorization.add("Basic " + new String(base64.encode("map-mobile:123".getBytes("utf-8")), "utf-8"));
    }

    @Override
    public void handleRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String callId = request.getHeader("callId");
        if (callId == null) {
            callId = UUID.randomUUID().toString().replace("-", "");
        }
        logger.info("callId:"+callId);
        MDC.put("callId", callId);
        String auth = request.getHeader("Authorization");
        try {
            if (authorization.contains(auth)) {
                super.handleRequest(request, response);
            } else {
                response.sendError(401, "unauthorized");
            }
        } finally {
            MDC.clear();
        }
    }
}
