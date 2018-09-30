package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {

	// 根据商品id来查询商品
	TbItem getItemById(long itemId);
	// 获取商品列表
	EasyUIDataGridResult getItemList(int page, int rows);
	// 添加商品
	TaotaoResult createItem(TbItem tbItem, String desc) throws Exception;
	// 根据商品id查询商品描述
	TbItemDesc getItemDescById(long itemId);
}
