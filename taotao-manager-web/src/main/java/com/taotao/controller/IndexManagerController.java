package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;

/**
 * 导入数据到索引库Controller
 * @author   Shanks
 * @data     2018年5月3日 上午11:52:40
 * @version  V1.1
 */
@Controller
public class IndexManagerController {

	@Autowired  
    private SearchItemService searchItemService;  
      
    @RequestMapping("/index/import")  
    @ResponseBody  
    public TaotaoResult importIndex() throws Exception{  
        TaotaoResult result = searchItemService.importAllItemsToIndex();  
        return result;  
    }
}
