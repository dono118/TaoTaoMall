package com.taotao.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.taotao.search.utils.SolrJUtil;

public class TestSolrJ {

	@Test
	public void testAddDocument() throws Exception {
		// 创建连接
		HttpSolrClient solrClient = SolrJUtil.getSolrClient();
		// 创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		// 创建域（id是必须的，其他的要在我们solr服务器中配置的有）
		document.addField("id", "3333");  
        document.addField("item_title", "联想电脑");  
        document.addField("item_sell_point", "送鼠标一个,耳机一个");  
        document.addField("item_price", 30000);  
        document.addField("item_image", "http://www.123.jpg");  
        document.addField("item_category_name", "电器");  
        document.addField("item_desc", "这是一款最新的电脑，质量好，值得信赖！！");
		//// 添加到索引库
		solrClient.add(document);
		// 提交
		solrClient.commit();
	}

	@Test  
    public void testDeleteDocument() throws Exception{  
		// 创建连接
		HttpSolrClient solrClient = SolrJUtil.getSolrClient();  
        //通过id来删除文档  
		solrClient.deleteById("3333");  
        //提交  
		solrClient.commit();  
    }
	
	@Test  
	public void deleteDocumentByQuery() throws Exception{  
		// 创建连接
		HttpSolrClient solrClient = SolrJUtil.getSolrClient();  
		//通过id来删除文档  
		solrClient.deleteByQuery("item_price:10000");  
		//提交  
		solrClient.commit();  
	}
	
	@Test  
	public void testQueryDocument() throws Exception{  
		// 创建连接
		HttpSolrClient solrClient = SolrJUtil.getSolrClient(); 
	    //通过id来删除文档  
	    SolrQuery query = new SolrQuery();  
	    query.setQuery("id:3333");  
	    QueryResponse response = solrClient.query(query);  
	    SolrDocumentList list = response.getResults();  
	    for(SolrDocument document : list){  
	        String id = document.getFieldValue("id").toString();  
	        String title = document.getFieldValue("item_title").toString();  
	        System.out.println(id);  
	        System.out.println(title);  
	    }  
	}
	
	@Test  
    public void queryDocument() throws Exception{  
		// 创建连接
		HttpSolrClient solrClient = SolrJUtil.getSolrClient(); 
        //创建一个SolrQuery对象  
        SolrQuery query = new SolrQuery();  
        //设置查询条件、过滤条件、分页条件、排序条件、高亮  
        //query.set("q", "*:*");  
        query.setQuery("手机");  
        //分页条件  
        query.setStart(0);  
        query.setRows(3);  
        //设置默认搜索域  
        query.set("df", "item_keywords");  
        //设置高亮  
        query.setHighlight(true);  
        //高亮显示的域  
        query.addHighlightField("item_title");  
        query.setHighlightSimplePre("<em>");  
        query.setHighlightSimplePost("</em>");  
        //执行查询，得到一个Response对象  
        QueryResponse response = solrClient.query(query);  
        //取查询结果  
        SolrDocumentList solrDocumentList = response.getResults();  
        //取查询结果总记录数  
        System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());  
        for(SolrDocument document : solrDocumentList){  
            System.out.println(document.getFieldValue("id"));  
            //取高亮显示  
            Map<String,Map<String,List<String>>> highlighting = response.getHighlighting();  
            List<String> list = highlighting.get(document.getFieldValue("id")).get("item_title");  
            String itemTitle = "";  
            if(list != null && list.size() > 0){  
                itemTitle = list.get(0);  
            }else {  
                itemTitle = (String)document.get("item_title");  
            }  
            System.out.println(itemTitle);  
            System.out.println(document.get("item_sell_point"));  
            System.out.println(document.get("item_price"));  
            System.out.println(document.get("item_image"));  
            System.out.println(document.get("item_category_name"));  
            System.out.println("===============================================");  
        }  
    }
}
