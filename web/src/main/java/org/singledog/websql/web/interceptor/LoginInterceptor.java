package org.singledog.websql.web.interceptor;

import com.alibaba.fastjson.JSON;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.web.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Adam on 2017/9/12.
 */
@Service
public class LoginInterceptor implements HandlerInterceptor, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Value("${websql.login.exclude:}")
    private String excludePatterns;

    private Set<String> patterns = new HashSet<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
