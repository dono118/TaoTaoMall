package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;

/**
 * 获取内容列表Controller
 * @author   Shanks
 * @data     2018年5月2日 下午1:56:46
 * @version  V1.1
 */
@Controller
@RequestMapping("/content")
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/query/list")  
    @ResponseBody  
    public EasyUIDataGridResult getContentList(Long categoryId,Integer page,Integer rows){  
        EasyUIDataGridResult result = contentService.getContentList(categoryId, page, rows);  
        return result;  
    }
	
	@RequestMapping("/save")  
    @ResponseBody  
    public TaotaoResult addContent(TbContent content){  
        TaotaoResult result = contentService.addContent(content);  
        return result;  
    }
	
	@RequestMapping("/edit")  
    @ResponseBody  
    public TaotaoResult updateContent(TbContent content){  
        TaotaoResult result = contentService.updateContent(content);  
        return result;  
    }  
      
    @RequestMapping("/delete")  
    @ResponseBody  
    public TaotaoResult deleteContent(String ids){  
        TaotaoResult result = contentService.deleteContent(ids);  
        return result;  
    }
	
	@RequestMapping("/getContent")
	@ResponseBody
	public TaotaoResult getContent(Long id) {
		TaotaoResult result = contentService.getContent(id);
		return result;
	}
}
