package org.singledog.websql.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Adam on 2017/9/13.
 */
@Controller
public class HomeController {

    @RequestMapping({"/",""})
    public ModelAndView login(HttpServletRequest request) {
        return new ModelAndView("/html/portal.html");
    }

}
