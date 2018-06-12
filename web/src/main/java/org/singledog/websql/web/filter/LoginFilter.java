package org.singledog.websql.web.filter;

import com.alibaba.fastjson.JSON;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.web.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Adam on 2017/9/13.
 */
@Order(20)
@WebFilter(urlPatterns = "/**")
@Service
public class LoginFilter implements Filter, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Value("${websql.login.exclude:}")
    private String excludePatterns;

    private Set<String> patterns = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;

        String requestUri = request.getRequestURI();
        if (requestUri.equals("/login.html")) {
            if (HttpRequestUtil.getUser(request) != null) {
                response.sendRedirect("/");
                return;
            }
        }

        for (String pattern : patterns) {
            if (requestUri.matches(pattern)) {
                logger.debug("ignore check user login status for request uri : {}", requestUri);
                chain.doFilter(req, rep);
                return;
            }
        }

        UserEntity entity = HttpRequestUtil.getUser(request);
        if (entity == null) {
            logger.info("user not login, redirect from {} to login page !", requestUri);
            response.sendRedirect("/login.html");
            return;
        }

        String uri = request.getRequestURI();
        if (uri.startsWith("/admin")) {
            if (entity.getAdmin() != null && entity.getAdmin() != UserEntity.ADMIN_Y) {
                logger.info("user is not admin, redirect from {} to 403 page !", requestUri);
                response.sendRedirect("/403.html");
                return;
            }
        }

        HttpInfoHolder.setUserEntity(entity);
        chain.doFilter(req, rep);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.isEmpty(excludePatterns)) {
            List<String> patternList = JSON.parseArray(excludePatterns, String.class);
            patterns.addAll(patternList);
        }
    }
}
