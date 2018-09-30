package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

/**
 * 商品详情页面展示Controller
 * @author   Shanks
 * @data     2018年5月4日 下午3:09:23
 * @version  V1.1
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")  
    public String showItem(@PathVariable Long itemId,Model model){  
        //获取商品基本信息  
        TbItem tbItem = itemService.getItemById(itemId);  
        Item item = new Item(tbItem);  
        //获取商品描述信息  
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);  
        //返回给页面需要的对象  
        model.addAttribute("item", item);  
        model.addAttribute("itemDesc", tbItemDesc);  
        //返回逻辑视图  
        return "item";  
    }
}
