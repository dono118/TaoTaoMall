package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 展示登录和注册页面的Controller
 * @author   Shanks
 * @data     2018年5月5日 下午9:15:03
 * @version  V1.1
 */
@Controller
public class PageController {

	@RequestMapping("/page/register")  
    public String showRegister(){  
        return "register";  
    }  
      
    @RequestMapping("/page/login")  
    public String showLogin(String url, Model model){  
    	model.addAttribute("redirect", url);
        return "login";  
    }
}
