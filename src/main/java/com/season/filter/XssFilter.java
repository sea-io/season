package com.season.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @link: com.ph.bsc.filter.LoginStatusJudge
 * @ClassName: LoginStatusJudge
 * @Description: XSS Filter
 * @Author: xiaolong1066
 * @Date: 2020/09/11
 * @Version: V1.0
 **/
@Component
@Slf4j
@WebFilter(description = "",
        urlPatterns = {"/**"})
@Order(value = -1)
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String requestURI = req.getServletPath();
        if (!AccessPahtCheck.checkAccessUrl(requestURI) ) {
            XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
                    (HttpServletRequest) request);
            chain.doFilter(xssRequest, response);
        }else{
            XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
                    (HttpServletRequest) request);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

}