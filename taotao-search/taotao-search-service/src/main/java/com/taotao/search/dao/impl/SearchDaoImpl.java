package com.taotao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.utils.SolrJUtil;

/**
 * 搜索Dao
 * @author   Shanks
 * @data     2018年5月3日 下午9:21:38
 * @version  V1.1
 */
@Repository
public class SearchDaoImpl implements SearchDao {

	@Override
	public SearchResult search(SolrQuery query) throws Exception {
		// 创建连接
		HttpSolrClient solrClient = SolrJUtil.getSolrClient();
		//根据query对象进行查询
		QueryResponse response = solrClient.query(query);
		//取查询结果  
        SolrDocumentList solrDocumentList = response.getResults();  
        //取查询结果总记录数  
        long numFound = solrDocumentList.getNumFound();  
        //初始化一个SolrResult对象并把总数属性进行赋值  
        SearchResult searchResult = new SearchResult();  
        searchResult.setTotalNumber(numFound);  
        List<SearchItem> itemList = new ArrayList<>();  
        //把查询结果封装到SearchItem当中  
        for (SolrDocument solrDocument : solrDocumentList) {  
            SearchItem searchItem = new SearchItem();  
            searchItem.setId((String)solrDocument.get("id"));  
            String image = (String)solrDocument.get("item_image");
            if (StringUtils.isNoneBlank(image)) {
            	image = image.split(",")[0];
            }
            searchItem.setImage(image);  
            searchItem.setCategory_name((String)solrDocument.get("item_category_name"));  
            searchItem.setItem_desc((String)solrDocument.get("item_desc"));  
            searchItem.setPrice((long)solrDocument.get("item_price"));  
            searchItem.setSell_point((String)solrDocument.get("item_sell_point"));  
            //取高亮显示  
            Map<String,Map<String,List<String>>> highlighting = response.getHighlighting();  
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");  
            String title = "";  
            if(list != null && list.size() > 0){  
                title = list.get(0);  
            } else {  
                title = (String)solrDocument.get("item_title");  
            }  
            searchItem.setTitle(title);  
            itemList.add(searchItem);  
        }  
        //把结果添加到SearchResult当中  
        searchResult.setItemList(itemList);  
        //返回  
        return searchResult;
	}

}
