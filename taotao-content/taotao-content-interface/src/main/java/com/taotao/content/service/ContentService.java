package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {

	// 获取内容列表
	EasyUIDataGridResult getContentList(long categoryId,int page,int rows);
	// 添加内容
	TaotaoResult addContent(TbContent content);
	// 编辑内容
	TaotaoResult updateContent(TbContent content);
	// 删除内容
	TaotaoResult deleteContent(String ids);
	// 获取单个内容信息
	TaotaoResult getContent(long id);
	//根据内容分类id来获取内容列表  
    List<TbContent> getContentListByCid(long cid);
}
