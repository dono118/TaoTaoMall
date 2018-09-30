package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import com.taotao.search.utils.SolrJUtil;

/**
 * 导入商品数据到索引库Service
 * @author   Shanks
 * @data     2018年5月3日 上午10:40:24
 * @version  V1.1
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Override
	public TaotaoResult importAllItemsToIndex() throws Exception {
		// 1.查询所有商品数据
		List<SearchItem> searchItemList = searchItemMapper.getSearchItemList();
		// 2.获取solrClient
		HttpSolrClient solrClient = SolrJUtil.getSolrClient();
		for (SearchItem searchItem : searchItemList) {
			// 3.为每个商品创建一个SolrInputDocument对象
			SolrInputDocument document = new SolrInputDocument();
			// 4.为文档添加域
			document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());
            // 5、向索引库中添加文档。
            try {
				solrClient.add(document);
				// 提交
				solrClient.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 返回结果
		return TaotaoResult.ok();
	}

}
