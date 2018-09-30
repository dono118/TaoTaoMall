package com.taotao.order.service;  
  
import com.taotao.common.pojo.TaotaoResult;  
import com.taotao.order.pojo.OrderInfo;  
  
public interface OrderService {  
    //生成订单，OrderInfo当中包含了表单提交的所有数据。  
    TaotaoResult createOrder(OrderInfo orderInfo);  
}