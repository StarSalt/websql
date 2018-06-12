package org.singledog.websql.web.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Adam on 2017/9/14.
 */
@RequestMapping("/admin")
@Controller
public class AdminControllerHome {

    @RequestMapping({"/",""})
    public ModelAndView manage() {
        return new ModelAndView("/html/admin/dashboard.html");
    }

}
