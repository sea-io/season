package com.season.interceptor;

import com.alibaba.fastjson.JSON;
import com.season.util.FunIMsgFormat;
import com.season.util.LogUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;


@Component
public class LogInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    private static final String APPLICATION_JSON = "application/json";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String interactionUUID = UUID.randomUUID().toString().replaceAll("-", "");
        LogUtil.registerProcessID(interactionUUID);

        logger.info(FunIMsgFormat.HEAD + "Content-type: {}" , request.getContentType());

        if(APPLICATION_JSON.equalsIgnoreCase(request.getContentType())){
            JSONObject jsonObject = parseJSON(request);
            logger.info(FunIMsgFormat.MsgStyle.DEFAULT_LOG.getFormat("access url[{}] invocation[{}] version[{}] with params[{}]"),
                    request.getRequestURL().toString(), interactionUUID, request.getHeader("User-Agent"), null == jsonObject ? "" : jsonObject.toJSONString());
        }else{
            logger.info(FunIMsgFormat.MsgStyle.DEFAULT_LOG.getFormat("access url[{}] invocation[{}] version[{}] with params[{}]"),
                    request.getRequestURL().toString(), interactionUUID, request.getHeader("User-Agent"), JSON.toJSONString(request.getParameterMap()));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("logInterceptor.afterCompletion");
    }
    
    private JSONObject parseJSON(HttpServletRequest request) {
        JSONObject paramsObj = new JSONObject();
        try (InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream(), "UTF-8") ;
             BufferedReader streamReader = new BufferedReader(inputStreamReader);){

            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            paramsObj = JSONObject.parseObject(responseStrBuilder.toString());
        } catch (Exception e) {
            logger.error(FunIMsgFormat.HEAD + "Conversion exception :{}", e.getMessage());
        }
        return paramsObj;
    }
