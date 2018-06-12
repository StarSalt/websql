package org.singledog.websql.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.singledog.websql.user.auth.UserAuthorityService;
import org.singledog.websql.user.email.EmailService;
import org.singledog.websql.user.entity.Functions;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.service.UserService;
import org.singledog.websql.user.util.RandomUtil;
import org.singledog.websql.web.param.LoginParam;
import org.singledog.websql.web.param.SendVerifyCodeParam;
import org.singledog.websql.web.param.UpdatePasswordParam;
import org.singledog.websql.web.result.CommonResult;
import org.singledog.websql.web.result.ResultCode;
import org.singledog.websql.web.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Adam on 2017/9/12.
 */
@RequestMapping("/user")
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthorityService userAuthorityService;

    /**
     * 发送邮件验证码
     * @param request
     * @param sendVerifyCodeParam
     * @return
     */
    @RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> sendVerifyCode(HttpServletRequest request, @Valid @RequestBody SendVerifyCodeParam sendVerifyCodeParam) {
        UserEntity userEntity = userService.getUserByEmail(sendVerifyCodeParam.getEmailOrUname());
        if (userEntity == null) {
            userEntity = userService.getUserByUserName(sendVerifyCodeParam.getEmailOrUname());
        }

        if (userEntity == null)
            return new CommonResult<String>().fillResult(ResultCode.USER_NOT_EXISTS);

        try {
            userService.checkStatus(userEntity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return CommonResult.newInstance(ResultCode.LOGIN_FAILED, String.class).setMessage(e.getMessage());
        }

        String verifyCode = RandomUtil.randomNumber(6);
        logger.info("sending verify code : {}, email : {}", verifyCode, userEntity.getEmail());
        request.getSession().setAttribute("login.verifyCode", verifyCode);
        Map<String, String> param = Collections.singletonMap("verifyCode", verifyCode);
        String flag = emailService.sendEmail(userEntity.getEmail(), Functions.login, param);
        if (flag == null)
            return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
        else
            return new CommonResult<String>().fillResult(ResultCode.SEND_EMAIL_ERROR).setMessage(flag);
    }

    /**
     * goto home page
     * @return
     */
    @RequestMapping("/tryLogin")
    public CommonResult<UserEntity> login(HttpServletRequest request, @Valid @RequestBody LoginParam loginParam) {
        String verifyCode = (String) request.getSession().getAttribute("login.verifyCode");
        if (StringUtils.isEmpty(verifyCode)) {
            return CommonResult.newInstance(ResultCode.VERIFY_CODE_EXPIRED, UserEntity.class);
        }

        if (!verifyCode.equals(loginParam.getVerifyCode())) {
            return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, UserEntity.class);
        }

        try {
            UserEntity userEntity = userAuthorityService.login(loginParam.getEmailOrUname(), loginParam.getPassword());
            HttpRequestUtil.setUser(request, userEntity);
            request.getSession().removeAttribute("login.verifyCode");
            UserEntity result = new UserEntity();
            result.setUserName(userEntity.getUserName());
            result.setAdmin(userEntity.getAdmin());
            return new CommonResult<UserEntity>().fillResult(ResultCode.SUCCESS).setData(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new CommonResult<UserEntity>().fillResult(ResultCode.LOGIN_FAILED).setMessage(e.getMessage());
        }
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        userService.logout();
        request.getSession().invalidate();
        return new ModelAndView("/login.html");
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public CommonResult<String> updatePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid UpdatePasswordParam passwordParam) {
        try {
            userService.updatePassword(passwordParam.getCurrentPassword(), passwordParam.getNewPassword());
            request.getSession().invalidate();
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
        } catch (Exception e) {
            return CommonResult.newInstance(ResultCode.LOGIN_FAILED, String.class).setMessage(e.getMessage());
        }
    }
}
