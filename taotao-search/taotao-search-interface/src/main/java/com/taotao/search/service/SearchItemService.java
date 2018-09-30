package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

public interface SearchItemService {

	TaotaoResult importAllItemsToIndex() throws Exception;
}
