package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * 单点登录及用户注册Controller
 * @author   Shanks
 * @data     2018年5月5日 下午4:39:36
 * @version  V1.1
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@RequestMapping("/check/{param}/{type}")  
    @ResponseBody  
    public TaotaoResult checkUserData(@PathVariable String param, @PathVariable Integer type){  
        TaotaoResult result = userService.checkUserData(param, type);  
        return result;  
    }
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
		TaotaoResult result = userService.register(user);
		return result;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)  
    @ResponseBody  
    public TaotaoResult login(String username,String password,  
            HttpServletRequest request,HttpServletResponse response) {  
        TaotaoResult result = userService.login(username, password);  
        if (result.getStatus() == 200) {
        	//把token写入到cookie  
        	CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());  
        }
        return result;  
    }
	
	@RequestMapping(value = "/token/{token}", method = RequestMethod.GET)  
    @ResponseBody  
    public String getUserByToken(@PathVariable String token, String callback) {  
        TaotaoResult result = userService.getUserByToken(token); 
        if (StringUtils.isNotBlank(callback)) {
        	return callback+"("+JSON.toJSONString(result)+");";
        }
        return JSON.toJSONString(result);  
    }
	
	@RequestMapping(value = "/logout/{token}", method = RequestMethod.GET)  
    @ResponseBody  
    public TaotaoResult logout(@PathVariable String token) {  
        TaotaoResult result = userService.logout(token);  
        return result;  
    }
}
