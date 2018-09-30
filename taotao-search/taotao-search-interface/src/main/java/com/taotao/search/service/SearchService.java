package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

public interface SearchService {

	// 搜索
	SearchResult search(String queryString, int page, int rows) throws Exception;
}
