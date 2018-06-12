package org.singledog.websql.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Adam on 2017/9/20.
 */
//@Controller
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {
    @Override
    public String getErrorPath() {
        return "/404.html";
    }

    @RequestMapping("/error")
    public ModelAndView handleError() {
        return new ModelAndView(getErrorPath());
    }
}
