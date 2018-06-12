package org.singledog.websql.web.filter;

import org.jboss.logging.MDC;
import org.singledog.global.session.GlobalSessionFilter;
import org.singledog.global.session.WrappedRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Adam on 2017/9/12.
 */
@Order(0)
@WebFilter(urlPatterns = "/**")
@Service
public class GlobalFilter extends GlobalSessionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String rid = UUID.randomUUID().toString().replace("-","").toLowerCase();
        MDC.put("rid", rid);
        ((HttpServletResponse) response).addHeader("rid", rid);
        WrappedRequest wrappedRequest = new WrappedRequest((HttpServletRequest)request, (HttpServletResponse)response);
        chain.doFilter(wrappedRequest, response);
    }

    @Override
    public void destroy() {

    }
}
