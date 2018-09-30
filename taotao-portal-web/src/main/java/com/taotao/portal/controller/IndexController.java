package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;

/**
 * 访问首页Controller
 * @author   Shanks
 * @data     2018年5月1日 下午3:13:12
 * @version  V1.1
 */
@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;
	
	@Value("${AD1_CATEGORY_ID}")  
    private Long AD1_CATEGORY_ID;  
    @Value("${AD1_WIDTH}")  
    private Integer AD1_WIDTH;  
    @Value("${AD1_WIDTH_B}")  
    private Integer AD1_WIDTH_B;  
    @Value("${AD1_HEIGHT}")  
    private Integer AD1_HEIGHT;  
    @Value("${AD1_HEIGHT_B}")  
    private Integer AD1_HEIGHT_B;
    
	@RequestMapping("/index")
	public String showIndex(Model model) {
		//根据内容ID去查询内容列表  
        List<TbContent> list = contentService.getContentListByCid(AD1_CATEGORY_ID);  
        //将列表转换为AD1Node列表  
        List<AD1Node> adList = new ArrayList<>();  
        for(TbContent content : list){  
            AD1Node adNode = new AD1Node();  
            adNode.setAlt(content.getTitle());  
            adNode.setHeight(AD1_HEIGHT);  
            adNode.setHeightB(AD1_HEIGHT_B);  
            adNode.setWidth(AD1_WIDTH);  
            adNode.setWidthB(AD1_WIDTH_B);  
            adNode.setSrc(content.getPic());  
            adNode.setSrcB(content.getPic2());  
            adNode.setHref(content.getUrl());  
            adList.add(adNode);  
        }  
        String ad1 = JSON.toJSONString(adList);  
        model.addAttribute("ad1", ad1);
		return "index";
	}
}
