package com.example.kafkasample.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ReadableRequestWrapper rereadableRequestWrapper = new ReadableRequestWrapper((HttpServletRequest) request);
        chain.doFilter(rereadableRequestWrapper, response);
    }

    @Override
    public void destroy() {

    }

}
