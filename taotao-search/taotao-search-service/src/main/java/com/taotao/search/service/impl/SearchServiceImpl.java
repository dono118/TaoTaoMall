package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;

/**
 * 搜索Service
 * @author   Shanks
 * @data     2018年5月3日 下午8:57:00
 * @version  V1.1
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String queryString, int page, int rows) throws Exception {
		//根据查询条件拼装查询对象  
        //创建一个SolrQuery对象  
        SolrQuery query = new SolrQuery();  
        //设置查询条件  
        query.setQuery(queryString);  
        //设置分页条件  
        if (page < 1) page = 1;  
        query.setStart((page-1)*rows);  
        if (rows < 1) rows = 10;  
        query.setRows(rows);  
        //设置默认搜索域，由于复制域查询不太准确，因此建议直接使用item_title域  
        query.set("df", "item_title");  
        //设置高亮显示  
        query.setHighlight(true);  
        query.addHighlightField("item_title");  
        query.setHighlightSimplePre("<em>");  
        query.setHighlightSimplePost("</em>");  
        //调用Dao执行查询  
        SearchResult searchResult = searchDao.search(query);  
        //计算查询结果的总页数  
        long totalNumber = searchResult.getTotalNumber();  
        long pages = totalNumber / rows;  
        if(totalNumber % rows > 0){  
            pages++;  
        }  
        searchResult.setTotalPages(pages);  
        //返回结果  
        return searchResult; 
	}

}
