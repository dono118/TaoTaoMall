package com.taotao.order.controller;  
  
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;  

/**
 * 生成订单Controller
 * @author   Shanks
 * @data     2018年5月6日 下午11:30:44
 * @version  V1.1
 */
@Controller  
public class OrderController {  
	
	@Autowired
	private OrderService orderService;
	
    @Value("${CART_KEY}")  
    private String CART_KEY;  
      
    @RequestMapping("/order/order-cart")  
    public String showOrderCart(HttpServletRequest request){  
        //用户必须是登录状态  
        //取用户ID  
        //根据用户ID取收获地址列表，这里就使用静态数据了  
        //把收货地址列表取出传递给页面  
        //从cookie中取购物车商品列表展示到页面  
        List<TbItem> cartList = getCartItemList(request);  
        request.setAttribute("cartList", cartList);  
        //返回逻辑视图  
        return "order-cart";  
    }  
      
    private List<TbItem> getCartItemList(HttpServletRequest request){  
        //从cookie中取购物车商品列表  
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);//为了防止乱码，统一下编码格式  
        if(StringUtils.isBlank(json)){  
            //说明cookie中没有商品列表，那么就返回一个空的列表  
            return new ArrayList<TbItem>();  
        }  
        List<TbItem> list = JSON.parseArray(json, TbItem.class);  
        return list;  
    }  
    
    @RequestMapping(value="/order/create",method=RequestMethod.POST)  
    public String createOrder(OrderInfo orderInfo,Model model){  
        //生成订单  
        TaotaoResult result = orderService.createOrder(orderInfo);  
        //返回逻辑视图  
        model.addAttribute("orderId", result.getData().toString());  
        model.addAttribute("payment", orderInfo.getPayment());  
        //得到3天后的日期  
        DateTime dateTime = new DateTime();  
        dateTime = dateTime.plusDays(3);  
        model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));  
        //返回逻辑视图  
        return "success";  
    }
}